package com.test.master.mapper;

import com.test.master.dto.service.ServiceDto;
import com.test.master.dto.service.ServicePublicDto;
import com.test.master.form.service.CreateServiceForm;
import com.test.master.form.service.UpdateServiceByCustomerForm;
import com.test.master.form.service.UpdateServiceForm;
import com.test.master.model.Service;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class, ServerProviderMapper.class})
public interface ServiceMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "serviceName", target = "serviceName")
    @Mapping(source = "logoPath", target = "logoPath")
    @Mapping(source = "bannerPath", target = "bannerPath")
    @Mapping(source = "hotline", target = "hotline")
    @Mapping(source = "settings", target = "settings")
    @Mapping(source = "lang", target = "lang")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "dbConfig.name", target = "tenantId")
    @Mapping(source = "tenantId", target = "serviceTenantId")
    @Mapping(source = "dbConfig.serverProvider", target = "serverProviderDto", qualifiedByName = "adminGetServerProviderDto")
    @Mapping(source = "account", target = "accountDto", qualifiedByName = "fromAccountToDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromServiceToDto")
    ServiceDto fromServiceToDto(Service service);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "serviceName", target = "serviceName")
    @Mapping(source = "logoPath", target = "logoPath")
    @Mapping(source = "bannerPath", target = "bannerPath")
    @Mapping(source = "hotline", target = "hotline")
    @Mapping(source = "settings", target = "settings")
    @Mapping(source = "lang", target = "lang")
    @Named("fromServiceToPublicDto")
    @BeanMapping(ignoreByDefault = true)
    ServicePublicDto fromServiceToPublicDto(Service service);


    @Mapping(source = "serviceName", target = "serviceName")
    @Mapping(source = "logoPath", target = "logoPath")
    @Mapping(source = "bannerPath", target = "bannerPath")
    @Mapping(source = "hotline", target = "hotline")
    @Mapping(source = "settings", target = "settings")
    @Mapping(source = "lang", target = "lang")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    Service fromServiceFromToEntity(CreateServiceForm form);


    @Mapping(source = "serviceName", target = "serviceName")
    @Mapping(source = "logoPath", target = "logoPath")
    @Mapping(source = "bannerPath", target = "bannerPath")
    @Mapping(source = "hotline", target = "hotline")
    @Mapping(source = "settings", target = "settings")
    @Mapping(source = "lang", target = "lang")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "tenantId", target = "tenantId")
    @BeanMapping(ignoreByDefault = true)
    void fromAdminUpdateFormToEntity(UpdateServiceForm updateServiceForm, @MappingTarget Service service);


    @Mapping(source = "serviceName", target = "serviceName")
    @Mapping(source = "bannerPath", target = "bannerPath")
    @Mapping(source = "hotline", target = "hotline")
    @Mapping(source = "settings", target = "settings")
    @BeanMapping(ignoreByDefault = true)
    void fromCustomerUpdateFormToEntity(UpdateServiceByCustomerForm updateServiceByCustomerForm, @MappingTarget Service service);

    @IterableMapping(elementTargetType = ServiceDto.class, qualifiedByName = "fromServiceToDto")
    List<ServiceDto> fromEntityToCustomerDtoList(List<Service> list);
}
