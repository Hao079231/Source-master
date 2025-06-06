package com.test.master.form.customer;

import com.test.master.validation.Email;
import com.test.master.validation.Password;
import com.test.master.validation.Phone;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateCustomerForm {
  @NotEmpty(message = "username cannot null")
  @ApiModelProperty(name = "username", required = true)
  private String username;
  @ApiModelProperty(name = "email")
  @Email
  private String email;
  @ApiModelProperty(name = "password", required = true)
  @Password
  private String password;
  @NotEmpty(message = "fullName cannot null")
  @ApiModelProperty(name = "fullName", required = true)
  private String fullName;
  @ApiModelProperty(name = "phone", required = true)
  @Phone
  private String phone;
  @ApiModelProperty(name = "avatarPath")
  private String avatarPath;
  @ApiModelProperty(name = "logoPath")
  private String logoPath;
  @ApiModelProperty(name = "status")
  private Integer status;
}
