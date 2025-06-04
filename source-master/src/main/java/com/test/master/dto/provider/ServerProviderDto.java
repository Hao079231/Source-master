package com.test.master.dto.provider;

import lombok.Data;

@Data
public class ServerProviderDto {
    private Long id;

    private String name;
    private String url;
    private Integer maxTenant;
    private Integer currentTenantCount;

    private String mySqlRootUser;
    private String mySqlRootPassword;
    private String mySqlJdbcUrl;
    private String driverClassName;
}