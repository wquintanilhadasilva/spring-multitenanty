package com.example.catalog.controller;

import com.example.catalog.controller.viewmodel.TenantDTO;
import com.example.catalog.service.TenantSchemaManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TenantsApiController {

    @Autowired
    private TenantSchemaManagementService tenantManagementService;

    @PostMapping("/tenants")
    public ResponseEntity<Void> createTenant(@RequestBody TenantDTO tenantDTO) {
        tenantManagementService.createTenant(tenantDTO.getTenantId(), tenantDTO.getSchema());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/tenants/updateAll")
    public ResponseEntity<Void> createAllTenant() {
        tenantManagementService.updateAllTenantSchemas();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
