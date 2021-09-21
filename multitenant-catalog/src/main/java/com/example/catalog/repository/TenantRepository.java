package com.example.catalog.repository;

import com.example.catalog.entity.Tenant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenantRepository extends CrudRepository<Tenant, String> {

    @Override
    List<Tenant> findAll();
}
