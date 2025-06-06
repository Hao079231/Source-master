package com.test.master.form.restaurant;

import com.test.master.validation.Phone;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRestaurantForm {
  @ApiModelProperty(name = "customer id", required = true)
  @NotNull(message = "Customer id cannot null")
  private Long customerId;
  @ApiModelProperty(name = "restaurant name", required = true)
  @NotEmpty(message = "Restaurant name cannot null")
  private String restaurantName;
  @ApiModelProperty(name = "logo_path", required = true)
  @NotEmpty(message = "Logo path cannot null")
  private String logoPath;
  @ApiModelProperty(name = "banner path")
  private String bannerPath;
  @ApiModelProperty(name = "hotline")
  @Phone(allowNull = true)
  private String hotline;
  @ApiModelProperty(name = "lang", required = true)
  @NotEmpty(message = "lang cannot null")
  private String lang;
  @ApiModelProperty(name = "settings", hidden = true)
  private String settings = "{}";
  @ApiModelProperty(name = "city", required = true)
  @NotEmpty(message = "City cannot null")
  private String city;
  @ApiModelProperty(name = "address", required = true)
  @NotEmpty(message = "Address cannot null")
  private String address;
  @ApiModelProperty(name = "status")
  private Integer status;
}
