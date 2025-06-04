package com.test.master.mapper;

import com.test.master.dto.employee.EmployeeAccountAutoCompleteDto;
import com.test.master.dto.employee.EmployeeAccountDto;
import com.test.master.form.employee.CreateEmployeeAccountAdminForm;
import com.test.master.form.employee.UpdateEmployeeAccountAdminForm;
import com.test.master.model.Account;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {GroupMapper.class, AccountMapper.class})
public interface EmployeeAccountMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "group", target = "group", qualifiedByName = "fromEntityToGroupDto")
    @Mapping(source = "parent", target = "parent", qualifiedByName = "fromAccountToAutoCompleteDto")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "avatarPath", target = "avatar")
    @Mapping(source = "isSuperAdmin", target = "isSuperAdmin")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToAdminDto")
    EmployeeAccountDto fromEmployeeAccountDto(Account account);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateFormToEntity")
    Account fromEmployeeAccountCreateFormToAccount(CreateEmployeeAccountAdminForm createEmployeeAccountAdminForm);


    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateFormToEntity(UpdateEmployeeAccountAdminForm updateEmployeeAccountAdminForm, @MappingTarget Account account);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "fullName", target = "fullName")
    @Named("fromEntityToAutoCompleteDto")
    EmployeeAccountAutoCompleteDto fromEmployeeAccountToAutoCompleteDto(Account account);


    @IterableMapping(elementTargetType = EmployeeAccountAutoCompleteDto.class, qualifiedByName = "fromEntityToAutoCompleteDto")
    List<EmployeeAccountAutoCompleteDto> convertEmployeeAccountToAutoCompleteDto(List<Account> list);

    @IterableMapping(elementTargetType = EmployeeAccountDto.class, qualifiedByName = "fromEntityToAdminDto")
    List<EmployeeAccountDto> convertEmployeeAccountToDto(List<Account> list);
}
