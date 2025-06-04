package com.test.master.controller;


import com.test.master.dto.ApiMessageDto;
import com.test.master.dto.ErrorCode;
import com.test.master.dto.ResponseListDto;
import com.test.master.dto.provider.ServerProviderDto;
import com.test.master.exception.UnauthorizationException;
import com.test.master.form.provider.CreateServerProviderForm;
import com.test.master.form.provider.UpdateServerProviderForm;
import com.test.master.mapper.ServerProviderMapper;
import com.test.master.model.DbConfig;
import com.test.master.model.ServerProvider;
import com.test.master.repository.DbConfigRepository;
import com.test.master.repository.ServerProviderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/server-provider")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ServerProviderController extends ABasicController{
    @Autowired
    ServerProviderRepository serverProviderRepository;

    @Autowired
    DbConfigRepository dbConfigRepository;

    @Autowired
    ServerProviderMapper serverProviderMapper;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SVP_V')")
    public ApiMessageDto<ServerProviderDto> get(@PathVariable("id") Long id) {
        ApiMessageDto<ServerProviderDto> result = new ApiMessageDto<>();
        if(!isSuperAdmin()){
            result.setResult(false);
            result.setCode(ErrorCode.SERVER_PROVIDER_ERROR_UNAUTHORIZED);
            return result;
        }
        ServerProvider serverProvider = serverProviderRepository.findById(id).orElse(null);
        if(serverProvider == null) {
            result.setResult(false);
            result.setCode(ErrorCode.SERVER_PROVIDER_ERROR_UNAUTHORIZED);
            return result;
        }
        result.setData(serverProviderMapper.fromEntityToServerProviderDto(serverProvider));
        result.setMessage("Get server provider success");
        return result;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SVP_AU')")
    public ApiMessageDto<ResponseListDto<ServerProviderDto>> list() {
        ApiMessageDto<ResponseListDto<ServerProviderDto>> apiMessageDto = new ApiMessageDto<>();
        if(!isSuperAdmin()){
            throw new UnauthorizationException("Not allowed for get list server provider.");
        }

        List<ServerProvider> serverProviders = serverProviderRepository.findAll();
        ResponseListDto<ServerProviderDto> responseListDto = new ResponseListDto(serverProviderMapper.fromEntityToServerProviderDtoList(serverProviders),0L,0);
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list server provider success");
        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SVP_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateServerProviderForm createServerProviderForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        if(!isSuperAdmin()){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SERVER_PROVIDER_ERROR_UNAUTHORIZED);
            return apiMessageDto;
        }
        ServerProvider serverProvider = serverProviderRepository.findFirstByUrl(createServerProviderForm.getUrl());
        if(serverProvider != null){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SERVER_PROVIDER_ERROR_URL_EXISTED);
            return apiMessageDto;
        }
        serverProvider = serverProviderMapper.fromCreateServerProviderFormToEntity(createServerProviderForm);

        serverProviderRepository.save(serverProvider);

        apiMessageDto.setMessage("Create server provider success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SVP_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateServerProviderForm updateServerProviderForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        if(!isSuperAdmin()){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SERVER_PROVIDER_ERROR_UNAUTHORIZED);
            return apiMessageDto;
        }
        ServerProvider serverProvider = serverProviderRepository.findById(updateServerProviderForm.getId()).orElse(null);
        if(serverProvider == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SERVER_PROVIDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        serverProviderMapper.fromUpdateFormToEntity(updateServerProviderForm, serverProvider);
        serverProviderRepository.save(serverProvider);
        apiMessageDto.setMessage("Update server provider success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('SVP_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        if(!isSuperAdmin()){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SERVER_PROVIDER_ERROR_UNAUTHORIZED);
            return apiMessageDto;
        }

        ServerProvider serverProvider = serverProviderRepository.findById(id).orElse(null);
        if(serverProvider == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SERVER_PROVIDER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        List<DbConfig> dbConfigList = dbConfigRepository.findAllByServerProviderId(serverProvider.getId());
        dbConfigRepository.deleteAll(dbConfigList);
        serverProviderRepository.delete(serverProvider);
        apiMessageDto.setMessage("Delete server provider success");
        return apiMessageDto;
    }
}
