package com.test.master.dto.employee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EmployeeAccountAutoCompleteDto {
    @ApiModelProperty(name = "id")
    private long id;
    @ApiModelProperty(name = "fullName")
    private String fullName;
    @ApiModelProperty(name = "avatarPath")
    private String avatarPath;
}
