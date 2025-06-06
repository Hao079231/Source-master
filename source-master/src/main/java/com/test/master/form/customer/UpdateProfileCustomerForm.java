package com.test.master.form.customer;

import com.test.master.validation.Email;
import com.test.master.validation.Password;
import com.test.master.validation.Phone;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileCustomerForm {
  @ApiModelProperty(name = "Email")
  @Email(allowNull = true)
  private String email;
  @ApiModelProperty(name = "old password")
  @Password(allowNull = true)
  private String oldPassword;
  @ApiModelProperty(name = "new password")
  @Password(allowNull = true)
  private String newPassword;
  @ApiModelProperty(name = "fullname")
  private String fullName;
  @ApiModelProperty(name = "phone")
  @Phone(allowNull = true)
  private String phone;
  @ApiModelProperty(name = "avatar_path")
  private String avatarPath;
  @ApiModelProperty(name = "logo_path")
  private String logoPath;
}
