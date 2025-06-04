package com.test.master.form.provider;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateServerProviderForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

    @NotNull(message = "maxTenant cannot be null")
    @ApiModelProperty(required = true)
    private Integer maxTenant;

    @NotEmpty(message = "driverClassName cannot be null")
    @ApiModelProperty(required = true)
    private String driverClassName;

    @NotEmpty(message = "mySqlJdbcUrl cannot be null")
    @ApiModelProperty(required = true)
    private String mySqlJdbcUrl;

    @NotEmpty(message = "mySqlRootUser cannot be null")
    @ApiModelProperty(required = true)
    private String mySqlRootUser;

    @NotEmpty(message = "mySqlRootPassword cannot be null")
    @ApiModelProperty(required = true)
    private String mySqlRootPassword;
}
