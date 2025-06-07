package com.test.master.form.restaurant;

import com.test.master.validation.Phone;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRestaurantForm {
  @ApiModelProperty(name = "id", required = true)
  @NotNull(message = "Restaurant id cannot null")
  private Long id;
  @ApiModelProperty(name = "customer id")
  private Long customerId;
  @ApiModelProperty(name = "restaurant name")
  private String restaurantName;
  @ApiModelProperty(name = "logo_path")
  private String logoPath;
  @ApiModelProperty(name = "banner path")
  private String bannerPath;
  @ApiModelProperty(name = "hotline")
  @Phone(allowNull = true)
  private String hotline;
  @ApiModelProperty(name = "lang")
  private String lang;
  @ApiModelProperty(name = "settings", hidden = true)
  private String settings = "{}";
  @ApiModelProperty(name = "city")
  private String city;
  @ApiModelProperty(name = "address")
  private String address;
  @ApiModelProperty(name = "status")
  private Integer status;
}
