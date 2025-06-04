package com.test.master.form.provider;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateServerProviderForm {
    @NotEmpty(message = "url cannot be null")
    @ApiModelProperty(required = true)
    private String url;

    @NotEmpty(message = "name cannot be null")
    @ApiModelProperty(required = true)
    private String name;

    @NotNull(message = "maxTenant cannot be null")
    @ApiModelProperty(required = true)
    @Min(0)
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

    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    private Integer status;
}
