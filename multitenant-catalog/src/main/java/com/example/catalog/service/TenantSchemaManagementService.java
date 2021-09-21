package com.example.catalog.service;

import com.example.catalog.entity.Tenant;
import com.example.catalog.repository.TenantRepository;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TenantSchemaManagementService {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final LiquibaseProperties liquibaseProperties;
    private final ResourceLoader resourceLoader;
    private final TenantRepository tenantRepository;

    @Autowired
    public TenantSchemaManagementService(DataSource dataSource,
                                         JdbcTemplate jdbcTemplate,
                                         @Qualifier("tenantLiquibaseProperties")
                                                     LiquibaseProperties liquibaseProperties,
                                         ResourceLoader resourceLoader,
                                         TenantRepository tenantRepository) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.liquibaseProperties = liquibaseProperties;
        this.resourceLoader = resourceLoader;
        this.tenantRepository = tenantRepository;
    }

    private static final String VALID_SCHEMA_NAME_REGEXP = "[A-Za-z0-9_]*";

    public void createTenant(String tenantId, String schema) {

        // Verify schema string to prevent SQL injection
        if (!schema.matches(VALID_SCHEMA_NAME_REGEXP)) {
            throw new TenantCreationException("Invalid schema name: " + schema);
        }

        try {
            if(!schemaExists(schema)) {
                createSchema(schema);
            }
            runLiquibase(dataSource, schema);
        } catch (DataAccessException e) {
            throw new TenantCreationException("Error when creating schema: " + schema, e);
        } catch (LiquibaseException e) {
            throw new TenantCreationException("Error when populating schema: ", e);
        }
        Tenant tenant = Tenant.builder()
                .tenantId(tenantId)
                .schema(schema)
                .build();
        tenantRepository.save(tenant);
    }

    public void updateAllTenantSchemas() {
        var tenants = this.loadAllSchemas();
        if(tenants != null){
            tenants.forEach(tenant -> createTenant(tenant.getTenantId(), tenant.getSchema()));
        }
    }

    private void createSchema(String schema) {
        jdbcTemplate.execute((StatementCallback<Boolean>) stmt -> stmt.execute("CREATE SCHEMA " + schema));
    }

    private boolean schemaExists(String schema) {
        String sql = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = ?";
        String schemaName = jdbcTemplate.query(sql, new SchemaMapExtractor(), schema);
        return null != schemaName && "" != schemaName;
    }

    private void runLiquibase(DataSource dataSource, String schema) throws LiquibaseException {
        SpringLiquibase liquibase = getSpringLiquibase(dataSource, schema);
        liquibase.afterPropertiesSet();
    }

    protected SpringLiquibase getSpringLiquibase(DataSource dataSource, String schema) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setResourceLoader(resourceLoader);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(schema);
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        liquibase.setLabels(liquibaseProperties.getLabels());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        return liquibase;
    }

    private List<Tenant>loadAllSchemas() {
        List<Tenant> tenants = tenantRepository.findAll();// jdbcTemplate.query(sql, new ListSchemaMapExtractor());
        return tenants;
    }

    private static final class SchemaMapExtractor implements ResultSetExtractor<String> {
        @Override
        public String extractData(ResultSet rs) throws SQLException, DataAccessException {
            String schema = null;
            while (rs.next()) {
                schema = rs.getString("schema_name");
            }
            return schema;
        }
    }

//    private static final class ListSchemaMapExtractor implements ResultSetExtractor<List<Tenant>> {
//        @Override
//        public List<Tenant> extractData(ResultSet rs) throws SQLException, DataAccessException {
//            List<Tenant> schemas = new ArrayList<>();
//            while (rs.next()) {
//                Tenant t = Tenant.builder()
//                    .tenantId(rs.getString("tenant_id"))
//                    .schema(rs.getString("schema"))
//                    .build();
//                schemas.add(t);
//            }
//            return schemas;
//        }
//    }

}
