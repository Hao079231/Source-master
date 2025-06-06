package com.test.master.mapper;

import com.test.master.dto.customer.CustomerDto;
import com.test.master.form.customer.CreateCustomerForm;
import com.test.master.form.customer.UpdateCustomerForm;
import com.test.master.form.customer.UpdateProfileCustomerForm;
import com.test.master.model.Customer;
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
    uses = {AccountMapper.class})
public interface CustomerMapper {
  @Mapping(source = "logoPath", target = "logoPath", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
  @Mapping(source = "status", target = "status")
  @BeanMapping(ignoreByDefault = true)
  Customer fromCreateCustomerFormToEntity(CreateCustomerForm createCustomerForm);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "logoPath", target = "logoPath")
  @Mapping(source = "account", target = "account", qualifiedByName = "fromAccountToDto")
  @Mapping(source = "status", target = "status")
  @BeanMapping(ignoreByDefault = true)
  @Named("fromEntityToCustomerDto")
  CustomerDto fromEntityToCustomerDto(Customer customer);

  @IterableMapping(elementTargetType = CustomerDto.class, qualifiedByName = "fromEntityToCustomerDto")
  List<CustomerDto> fromEntityToCustomerDtoList(List<Customer> customers);

  @Mapping(source = "logoPath", target = "logoPath")
  @Mapping(source = "account", target = "account", qualifiedByName = "fromAccountToDtoShort")
  @BeanMapping(ignoreByDefault = true)
  @Named("fromEntityToProfileCustomerDto")
  CustomerDto fromEntityToProfileCustomerDto(Customer customer);

  @Mapping(source = "logoPath", target = "logoPath")
  @Mapping(source = "status", target = "status")
  @BeanMapping(ignoreByDefault = true)
  void updateCustomerFormToEntity(UpdateCustomerForm updateCustomerForm, @MappingTarget Customer customer);

  @Mapping(source = "logoPath", target = "logoPath")
  @BeanMapping(ignoreByDefault = true)
  void updateProfileCustomerFormToEntity(UpdateProfileCustomerForm updateProfileCustomerForm, @MappingTarget Customer customer);
}
