package com.example.catalog.controller;

import com.example.catalog.controller.viewmodel.TenantDTO;
import com.example.catalog.service.TenantManagementService;
import com.example.catalog.service.TenantSchemaDatabaseManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class TenantsApiController {

    @Autowired
    private TenantSchemaDatabaseManagementService tenantManagementService;

    @PostMapping("/tenants")
    public ResponseEntity<Void> createTenant(@RequestBody TenantDTO tenantDTO) { //(@RequestParam String tenantId, @RequestParam String db, @RequestParam String password) {
        tenantManagementService.createTenant(tenantDTO.getTenantId(), tenantDTO.getDb(), tenantDTO.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
