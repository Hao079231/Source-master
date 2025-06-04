package com.test.master.form.db.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateDbConfigForm {
    @NotNull(message = "serverProviderId cannot be null")
    @ApiModelProperty(required = true)
    private Long serverProviderId;

    @NotEmpty(message = "url cannot be null")
    @ApiModelProperty(required = true)
    private String url;

    private Integer maxConnection;

    @NotEmpty(message = "username cannot be null")
    @ApiModelProperty(required = true)
    private String username;

    @NotEmpty(message = "password cannot be null")
    @ApiModelProperty(required = true)
    private String password;

    @NotEmpty(message = "driverClassName cannot be null")
    @ApiModelProperty(required = true)
    private String driverClassName;

    @NotNull(message = "initialize cannot be null")
    @ApiModelProperty(required = true)
    private boolean initialize;

    @NotNull(message = "serviceId cannot be null")
    @ApiModelProperty(required = true)
    private Long serviceId;
}
