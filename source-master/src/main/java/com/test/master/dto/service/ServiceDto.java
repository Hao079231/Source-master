package com.test.master.dto.service;

import com.test.master.dto.ABasicAdminDto;
import com.test.master.dto.account.AccountDto;
import com.test.master.dto.provider.ServerProviderDto;
import com.test.master.model.Group;
import lombok.Data;

@Data
public class ServiceDto extends ABasicAdminDto {
    private String serviceName;
    private Group group;
    private String logoPath;
    private String bannerPath;
    private String hotline;
    private String settings;
    private String lang;
    private ServerProviderDto serverProviderDto;
    private Integer status;
    private AccountDto accountDto;

    private String tenantId;
    private String serviceTenantId;
}
