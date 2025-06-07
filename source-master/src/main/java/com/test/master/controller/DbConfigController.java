package com.test.master.controller;


import com.test.master.constant.WinWinConstant;
import com.test.master.dto.ApiMessageDto;
import com.test.master.dto.ErrorCode;
import com.test.master.dto.db.config.DbConfigDto;
import com.test.master.form.db.config.CreateDbConfigForm;
import com.test.master.form.db.config.UpdateDbConfigForm;
import com.test.master.mapper.DbConfigMapper;
import com.test.master.model.Restaurant;
import com.test.master.model.DbConfig;
import com.test.master.model.ServerProvider;
import com.test.master.repository.RestaurantRepository;
import com.test.master.repository.DbConfigRepository;
import com.test.master.repository.ServerProviderRepository;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/v1/db-config")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class DbConfigController extends ABasicController{
    @Autowired
    DbConfigRepository dbConfigRepository;

    @Autowired
    DbConfigMapper dbConfigMapper;

    @Autowired
    DataSource dataSource;

    @Autowired
    ServerProviderRepository serverProviderRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @GetMapping(value = "/get/{careerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DB_V_ST')")
    public ApiMessageDto<DbConfigDto> get(@PathVariable("careerId") Long careerId) {
        ApiMessageDto<DbConfigDto> result = new ApiMessageDto<>();
        if(!isSuperAdmin()){
            result.setResult(false);
            result.setCode(ErrorCode.DB_CONFIG_ERROR_UNAUTHORIZED);
            return result;
        }
        DbConfig dbConfig = dbConfigRepository.findByRestaurantId(careerId).orElse(null);
        if(dbConfig == null) {
            result.setResult(false);
            result.setCode(ErrorCode.DB_CONFIG_ERROR_NOT_FOUND);
            return result;
        }
        result.setData(dbConfigMapper.fromEntityToDto(dbConfig));
        result.setMessage("Get db config success");
        return result;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DB_L')")
    @ApiIgnore
    public ApiMessageDto<List<DbConfigDto>> list() {
        ApiMessageDto<List<DbConfigDto>> result = new ApiMessageDto<>();
        if(!isSuperAdmin()){
            result.setResult(false);
            result.setCode(ErrorCode.DB_CONFIG_ERROR_UNAUTHORIZED);
            return result;
        }
        List<DbConfig> dbConfigList = dbConfigRepository.findAllByInitialize(true);
        result.setData(dbConfigMapper.fromEntityListToDtoList(dbConfigList));
        result.setMessage("Get list db config success");
        return result;
    }

    @GetMapping(value = "/get_by_name", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DB_V_NAME')")
    public ApiMessageDto<DbConfigDto> get(@RequestParam("name") String name) {
        ApiMessageDto<DbConfigDto> result = new ApiMessageDto<>();
        DbConfig dbConfig = dbConfigRepository.findByName(name);
        if(dbConfig == null) {
            result.setResult(false);
            result.setCode(ErrorCode.DB_CONFIG_ERROR_NOT_FOUND);
            return result;
        }
        result.setData(dbConfigMapper.fromEntityToDto(dbConfig));
        result.setMessage("Get db config success");
        return result;
    }

    private String parseDatabaseNameFromConnectionString(String url) {
        String cleanString = url.substring("jdbc:mysql://".length(), url.indexOf("?"));
        return cleanString.substring(cleanString.indexOf("/") + 1);
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DB_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateDbConfigForm createDbConfigForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        if(!isSuperAdmin()){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.DB_CONFIG_ERROR_UNAUTHORIZED);
            return apiMessageDto;
        }
        DbConfig dbConfig = dbConfigRepository.findFirstByRestaurantId(createDbConfigForm.getRestaurantId());
        if(dbConfig != null){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.DB_CONFIG_SHOP_EXISTED);
            return apiMessageDto;
        }
        Restaurant store = restaurantRepository.findById(createDbConfigForm.getRestaurantId()).orElse(null);
        if (store == null || store.getStatus() != WinWinConstant.STATUS_ACTIVE) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SERVICE_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        dbConfig = dbConfigMapper.fromCreateFormToEntity(createDbConfigForm);
        dbConfig.setName("tenant_id_" + store.getId());//tenant_id_[id nha hang]
        store.setTenantId(dbConfig.getName());

        ServerProvider serverProvider = serverProviderRepository.findById(createDbConfigForm.getServerProviderId()).orElse(null);
        if(serverProvider == null){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.SERVER_PROVIDER_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Server not found");
            return apiMessageDto;
        }
        dbConfig.setServerProvider(serverProvider);

        String databaseName = parseDatabaseNameFromConnectionString(dbConfig.getUrl());
        DataSource dataSource = getRootDatasource(serverProvider);
        // Open a connection
        try(
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
        ) {
            statement.execute("CREATE DATABASE " + databaseName  + " CHARACTER SET utf8;");
            statement.execute("CREATE USER '" + dbConfig.getUsername() + "' IDENTIFIED BY '" + dbConfig.getPassword() + "';");
            statement.execute("CREATE USER '" + dbConfig.getUsername() + "'@'localhost' IDENTIFIED BY '" + dbConfig.getPassword() + "';");
            statement.execute("GRANT ALL PRIVILEGES ON " + databaseName +".* TO '" + dbConfig.getUsername() + "';");
            statement.execute("FLUSH PRIVILEGES;");

            System.out.println("Tenant database created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.DB_CONFIG_ERROR_CANNOT_CREATE_DB);
            return apiMessageDto;
        }

        restaurantRepository.save(store);
        dbConfigRepository.save(dbConfig);

        apiMessageDto.setMessage("Create db config success");
        return apiMessageDto;
    }

    private DataSource getRootDatasource(ServerProvider serverProvider){
        HikariDataSource ds = new HikariDataSource();
        ds.setUsername(serverProvider.getMySqlRootUser());
        ds.setPassword(serverProvider.getMySqlRootPassword());
        ds.setJdbcUrl(serverProvider.getMySqlJdbcUrl());
        ds.setDriverClassName(serverProvider.getDriverClassName());
        return ds;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DB_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateDbConfigForm updateDbConfigForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        if(!isSuperAdmin()){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.DB_CONFIG_ERROR_UNAUTHORIZED);
            return apiMessageDto;
        }
        DbConfig dbConfig = dbConfigRepository.findById(updateDbConfigForm.getId()).orElse(null);
        if(dbConfig == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.DB_CONFIG_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        dbConfigMapper.fromUpdateFormToEntity(updateDbConfigForm, dbConfig);
        dbConfigRepository.save(dbConfig);
        apiMessageDto.setMessage("Update db config success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('DB_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        if(!isSuperAdmin()){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.DB_CONFIG_ERROR_UNAUTHORIZED);
            return apiMessageDto;
        }

        DbConfig dbConfig = dbConfigRepository.findById(id).orElse(null);
        if(dbConfig == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.DB_CONFIG_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        dbConfigRepository.delete(dbConfig);
        apiMessageDto.setMessage("Delete db config success");
        return apiMessageDto;
    }
}
