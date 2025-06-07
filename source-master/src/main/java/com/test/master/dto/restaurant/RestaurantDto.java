package com.test.master.dto.restaurant;

import com.test.master.dto.ABasicAdminDto;
import com.test.master.dto.customer.CustomerDto;
import com.test.master.dto.provider.ServerProviderDto;
import lombok.Data;

@Data
public class RestaurantDto extends ABasicAdminDto {
  private Long id;
  private String tenantId;
  private CustomerDto customer;
  private String restaurantName;
  private String logoPath;
  private String bannerPath;
  private String hotline;
  private String lang;
  private String city;
  private String settings;
  private String address;
  private Integer status;
  private ServerProviderDto serverProviderDto;
  private String dbConfigTenantId;
}
