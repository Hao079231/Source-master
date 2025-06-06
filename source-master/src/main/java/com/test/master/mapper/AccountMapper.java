package com.test.master.mapper;

import com.test.master.form.customer.CreateCustomerForm;
import com.test.master.form.customer.UpdateCustomerForm;
import com.test.master.form.customer.UpdateProfileCustomerForm;
import com.test.master.model.Account;
import org.mapstruct.*;
import com.test.master.dto.account.AccountAutoCompleteDto;
import com.test.master.dto.account.AccountDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {GroupMapper.class})
public interface AccountMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "group", target = "group", qualifiedByName = "fromEntityToGroupDto")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "avatarPath", target = "avatar")
    @Mapping(source = "isSuperAdmin", target = "isSuperAdmin")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromAccountToDto")
    AccountDto fromAccountToDto(Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "fullName", target = "fullName")
    @Named("fromAccountToAutoCompleteDto")
    AccountAutoCompleteDto fromAccountToAutoCompleteDto(Account account);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatar")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromAccountToDtoShort")
    AccountDto fromAccountToDtoShort(Account account);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "avatarPath", target = "avatarPath", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @BeanMapping(ignoreByDefault = true)
    Account fromCustomerFormToEntity(CreateCustomerForm createCustomerForm);

    @IterableMapping(elementTargetType = AccountAutoCompleteDto.class)
    List<AccountAutoCompleteDto> convertAccountToAutoCompleteDto(List<Account> list);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    void updateCustomerFormToEntity(UpdateCustomerForm updateCustomerForm, @MappingTarget Account account);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @BeanMapping(ignoreByDefault = true)
    void updateProfileCustomerFormToEntity(UpdateProfileCustomerForm updateProfileCustomerForm, @MappingTarget Account account);

}
