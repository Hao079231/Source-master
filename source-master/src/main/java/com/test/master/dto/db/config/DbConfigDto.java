package com.test.master.dto.db.config;

import com.test.master.dto.provider.ServerProviderDto;
import lombok.Data;

@Data
public class DbConfigDto {
    private Long id;
    private String name;
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private boolean initialize;
    private Integer updateStatus;
    private Integer maxConnection;
    private ServerProviderDto serverProviderDto;
}
