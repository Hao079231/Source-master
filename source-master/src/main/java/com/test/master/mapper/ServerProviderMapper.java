package com.test.master.mapper;


import com.test.master.dto.provider.ServerProviderDto;
import com.test.master.form.provider.CreateServerProviderForm;
import com.test.master.form.provider.UpdateServerProviderForm;
import com.test.master.model.ServerProvider;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ServerProviderMapper {
    @Mapping(source = "url", target = "url")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "maxTenant", target = "maxTenant")
    @Mapping(source = "driverClassName", target = "driverClassName")
    @Mapping(source = "mySqlJdbcUrl", target = "mySqlJdbcUrl")
    @Mapping(source = "mySqlRootUser", target = "mySqlRootUser")
    @Mapping(source = "mySqlRootPassword", target = "mySqlRootPassword")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    ServerProvider fromCreateServerProviderFormToEntity(CreateServerProviderForm createServerProviderForm);

    @Mapping(source = "maxTenant", target = "maxTenant")
    @Mapping(source = "driverClassName", target = "driverClassName")
    @Mapping(source = "mySqlJdbcUrl", target = "mySqlJdbcUrl")
    @Mapping(source = "mySqlRootUser", target = "mySqlRootUser")
    @Mapping(source = "mySqlRootPassword", target = "mySqlRootPassword")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateFormToEntity(UpdateServerProviderForm updateServerProviderForm, @MappingTarget ServerProvider serverProvider);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "currentTenantCount", target = "currentTenantCount")
    @Mapping(source = "maxTenant", target = "maxTenant")
    @Mapping(source = "mySqlJdbcUrl", target = "mySqlJdbcUrl")
    @Mapping(source = "driverClassName", target = "driverClassName")
    @Mapping(source = "mySqlRootUser", target = "mySqlRootUser")
    @Mapping(source = "mySqlRootPassword", target = "mySqlRootPassword")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetServerProviderDto")
    ServerProviderDto fromEntityToServerProviderDto(ServerProvider serverProvider);

    @IterableMapping(elementTargetType = ServerProviderDto.class, qualifiedByName = "adminGetServerProviderDto")
    List<ServerProviderDto> fromEntityToServerProviderDtoList(List<ServerProvider> serverProviderList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "url", target = "url")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToPublicServerProviderDto")
    ServerProviderDto fromEntityToPublicServerProviderDto(ServerProvider serverProvider);
}
