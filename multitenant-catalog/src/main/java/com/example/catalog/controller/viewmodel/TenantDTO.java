package com.example.catalog.controller.viewmodel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
public class TenantDTO {
    private String tenantId;
    private String db;
    private String password;
    private String schema;
}
