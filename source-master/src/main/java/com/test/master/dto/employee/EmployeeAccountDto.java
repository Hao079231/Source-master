package com.test.master.dto.employee;

import com.test.master.dto.ABasicAdminDto;
import com.test.master.dto.account.AccountAutoCompleteDto;
import com.test.master.dto.group.GroupDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeAccountDto extends ABasicAdminDto {
    @ApiModelProperty(name = "kind")
    private int kind;
    @ApiModelProperty(name = "username")
    private String username;
    @ApiModelProperty(name = "phone")
    private String phone;
    @ApiModelProperty(name = "email")
    private String email;
    @ApiModelProperty(name = "fullName")
    private String fullName;
    @ApiModelProperty(name = "group")
    private GroupDto group;
    @ApiModelProperty(name = "lastLogin")
    private Date lastLogin;
    @ApiModelProperty(name = "avatar")
    private String avatar;
    private Boolean isSuperAdmin;
    @ApiModelProperty(name = "parent")
    private AccountAutoCompleteDto parent;
}
