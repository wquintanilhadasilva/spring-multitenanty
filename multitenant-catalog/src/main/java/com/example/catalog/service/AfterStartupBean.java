package com.example.catalog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AfterStartupBean implements CommandLineRunner {

    private final TenantSchemaManagementService tenantSchemaManagementService;

    @Override
    public void run(String... args) throws Exception {
        tenantSchemaManagementService.updateAllTenantSchemas();
    }
}
