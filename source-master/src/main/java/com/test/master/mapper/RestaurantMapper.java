package com.test.master.mapper;

import com.test.master.dto.restaurant.RestaurantDto;
import com.test.master.form.restaurant.CreateRestaurantForm;
import com.test.master.form.restaurant.UpdateRestaurantForm;
import com.test.master.model.Restaurant;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {CustomerMapper.class})
public interface RestaurantMapper {
  @Mapping(source = "restaurantName", target = "restaurantName")
  @Mapping(source = "logoPath", target = "logoPath")
  @Mapping(source = "bannerPath", target = "bannerPath")
  @Mapping(source = "hotline", target = "hotline")
  @Mapping(source = "settings", target = "settings")
  @Mapping(source = "lang", target = "lang")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "address", target = "address")
  @Mapping(source = "city", target = "city")
  @BeanMapping(ignoreByDefault = true)
  Restaurant fromCreateRestaurantFormToEntity(CreateRestaurantForm createRestaurantForm);

  @Mapping(source = "tenantId", target = "tenantId")
  @Mapping(source = "restaurantName", target = "restaurantName")
  @Mapping(source = "logoPath", target = "logoPath", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
  @Mapping(source = "bannerPath", target = "bannerPath", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
  @Mapping(source = "hotline", target = "hotline")
  @Mapping(source = "settings", target = "settings")
  @Mapping(source = "lang", target = "lang")
  @Mapping(source = "address", target = "address")
  @Mapping(source = "city", target = "city")
  @BeanMapping(ignoreByDefault = true)
  @Named("fromEntityToRestaurantDtoForCustomer")
  RestaurantDto fromEntityToRestaurantDtoForCustomer(Restaurant restaurant);

  @IterableMapping(elementTargetType = RestaurantDto.class, qualifiedByName = "fromEntityToRestaurantDto")
  List<RestaurantDto> fromEntityListToDto(List<Restaurant> restaurants);

  @IterableMapping(elementTargetType = RestaurantDto.class, qualifiedByName = "fromEntityToRestaurantDtoForCustomer")
  List<RestaurantDto> fromEntityListToDtoForCustomer(List<Restaurant> restaurants);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "customer", target = "customer", qualifiedByName = "fromEntityToCustomerDto")
  @Mapping(source = "dbConfig.serverProvider", target = "serverProviderDto")
  @Mapping(source = "dbConfig.name", target = "dbConfigTenantId")
  @Mapping(source = "tenantId", target = "tenantId")
  @Mapping(source = "restaurantName", target = "restaurantName")
  @Mapping(source = "logoPath", target = "logoPath")
  @Mapping(source = "bannerPath", target = "bannerPath")
  @Mapping(source = "hotline", target = "hotline")
  @Mapping(source = "settings", target = "settings")
  @Mapping(source = "lang", target = "lang")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "address", target = "address")
  @Mapping(source = "city", target = "city")
  @BeanMapping(ignoreByDefault = true)
  @Named("fromEntityToRestaurantDto")
  RestaurantDto fromEntityToRestaurantDto(Restaurant restaurant);

  @Mapping(source = "restaurantName", target = "restaurantName")
  @Mapping(source = "logoPath", target = "logoPath")
  @Mapping(source = "bannerPath", target = "bannerPath")
  @Mapping(source = "hotline", target = "hotline")
  @Mapping(source = "lang", target = "lang")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "address", target = "address")
  @Mapping(source = "city", target = "city")
  void updateFromRestaurantFormToEntity(UpdateRestaurantForm updateRestaurantForm, @MappingTarget Restaurant restaurant);
}
