package com.test.master.controller;

import com.test.master.constant.WinWinConstant;
import com.test.master.dto.ApiMessageDto;
import com.test.master.dto.ErrorCode;
import com.test.master.dto.ResponseListDto;
import com.test.master.dto.restaurant.RestaurantDto;
import com.test.master.exception.BadRequestException;
import com.test.master.exception.NotFoundException;
import com.test.master.exception.UnauthorizationException;
import com.test.master.form.restaurant.CreateRestaurantForm;
import com.test.master.form.restaurant.UpdateRestaurantForm;
import com.test.master.mapper.RestaurantMapper;
import com.test.master.model.Customer;
import com.test.master.model.DbConfig;
import com.test.master.model.Restaurant;
import com.test.master.model.ServerProvider;
import com.test.master.model.criteria.RestaurantCriteria;
import com.test.master.repository.CustomerRepository;
import com.test.master.repository.DbConfigRepository;
import com.test.master.repository.RestaurantRepository;
import com.test.master.repository.ServerProviderRepository;
import com.test.master.service.impl.UserServiceImpl;
import com.test.master.utils.TenantUtils;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/restaurant")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class RestaurantController extends ABasicController{
  @Autowired
  private RestaurantRepository restaurantRepository;
  @Autowired
  private RestaurantMapper restaurantMapper;
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private UserServiceImpl userService;
  @Autowired
  private DbConfigRepository dbConfigRepository;
  @Autowired
  private ServerProviderRepository serverProviderRepository;

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('RES_C')")
  public ApiMessageDto<String> create(@Valid @RequestBody CreateRestaurantForm createRestaurantForm, BindingResult bindingResult){
    if (!isSuperAdmin()){
      throw new UnauthorizationException("Restaurant is not allowed", ErrorCode.RESTAURANT_ERROR_UN_AUTHORIZE);
    }

    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Customer customer = customerRepository.findById(createRestaurantForm.getCustomerId()).orElseThrow(()
    -> new NotFoundException("Customer not found", ErrorCode.CUSTOMER_ERROR_NOT_FOUND));

    Restaurant restaurant = restaurantMapper.fromCreateRestaurantFormToEntity(createRestaurantForm);
    restaurant.setCustomer(customer);
    restaurantRepository.save(restaurant);
    apiMessageDto.setMessage("Create restaurant successfully");
    return apiMessageDto;
  }

  @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('RES_L')")
  public ApiMessageDto<ResponseListDto<List<RestaurantDto>>> getList(RestaurantCriteria restaurantCriteria, Pageable pageable){
    if (!isSuperAdmin()){
      throw new UnauthorizationException("Restaurant is not allowed", ErrorCode.RESTAURANT_ERROR_UN_AUTHORIZE);
    }

    ApiMessageDto<ResponseListDto<List<RestaurantDto>>> apiMessageDto = new ApiMessageDto<>();
    Page<Restaurant> restaurants = restaurantRepository.findAll(restaurantCriteria.getSpecification(), pageable);
    ResponseListDto<List<RestaurantDto>> responseListDto = new ResponseListDto<>(restaurantMapper.fromEntityListToDto(restaurants.getContent()), restaurants.getTotalElements(), restaurants.getTotalPages());
    apiMessageDto.setData(responseListDto);
    apiMessageDto.setMessage("Get list restaurant successfully");
    return apiMessageDto;
  }

  @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('RES_V')")
  public ApiMessageDto<RestaurantDto> get(@PathVariable("id") Long id){
    if (!isSuperAdmin()){
      throw new UnauthorizationException("Restaurant is not allowed", ErrorCode.RESTAURANT_ERROR_UN_AUTHORIZE);
    }

    ApiMessageDto<RestaurantDto> apiMessageDto = new ApiMessageDto<>();
    Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()
    -> new NotFoundException("Restaurant not found", ErrorCode.RESTAURANT_ERROR_NOT_FOUND));

    apiMessageDto.setData(restaurantMapper.fromEntityToRestaurantDto(restaurant));
    apiMessageDto.setMessage("Get restaurant successfully");
    return apiMessageDto;
  }

  @GetMapping(value = "/client-list", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('RES_C_L')")
  public ApiMessageDto<ResponseListDto<List<RestaurantDto>>> getClientList(RestaurantCriteria restaurantCriteria, Pageable pageable){
    ApiMessageDto<ResponseListDto<List<RestaurantDto>>> apiMessageDto = new ApiMessageDto<>();
    Customer customer = customerRepository.findById(getCurrentUser()).orElseThrow(()
    -> new NotFoundException("Customer not found", ErrorCode.CUSTOMER_ERROR_NOT_FOUND));

    restaurantCriteria.setCustomerId(customer.getId());
    Page<Restaurant> restaurants = restaurantRepository.findAll(restaurantCriteria.getSpecification(), pageable);
    ResponseListDto<List<RestaurantDto>> responseListDto = new ResponseListDto<>(restaurantMapper.fromEntityListToDtoForCustomer(restaurants.getContent()), restaurants.getTotalElements(), restaurants.getTotalPages());
    apiMessageDto.setData(responseListDto);
    apiMessageDto.setMessage("Get list restaurant successfully");
    return apiMessageDto;
  }

  @GetMapping(value = "/client-get/{tenantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('RES_C_V')")
  public ApiMessageDto<RestaurantDto> getRestaurantForCustomer(@PathVariable("tenantId") String tenantId){
    ApiMessageDto<RestaurantDto> apiMessageDto = new ApiMessageDto<>();
    Customer customer = customerRepository.findById(getCurrentUser()).orElseThrow(()
        -> new NotFoundException("Customer not found", ErrorCode.CUSTOMER_ERROR_NOT_FOUND));

    if (!WinWinConstant.STATUS_ACTIVE.equals(customer.getStatus())){
      throw new UnauthorizationException("Customer not active", ErrorCode.CUSTOMER_ERROR_UN_AUTHORIZE);
    }

    Restaurant restaurant = restaurantRepository.findFirstByTenantId(tenantId).orElseThrow(
        () -> new NotFoundException("Restaurant not found", ErrorCode.RESTAURANT_ERROR_NOT_FOUND));

    if (!WinWinConstant.STATUS_ACTIVE.equals(restaurant.getStatus())){
      throw new UnauthorizationException("Restaurant not active", ErrorCode.RESTAURANT_ERROR_UN_AUTHORIZE);
    }

    apiMessageDto.setData(restaurantMapper.fromEntityToRestaurantDtoForCustomer(restaurant));
    apiMessageDto.setMessage("Get restaurant successfully");
    return apiMessageDto;
  }

  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('RES_U')")
  public ApiMessageDto<String> update(@Valid @RequestBody UpdateRestaurantForm updateRestaurantForm, BindingResult bindingResult){
    if (!isSuperAdmin()){
      throw new UnauthorizationException("Restaurant is not allowed", ErrorCode.RESTAURANT_ERROR_UN_AUTHORIZE);
    }

    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Restaurant restaurant = restaurantRepository.findById(updateRestaurantForm.getId()).orElseThrow(()
    -> new NotFoundException("Restaurant not found", ErrorCode.RESTAURANT_ERROR_NOT_FOUND));

    if (updateRestaurantForm.getCustomerId() != null){
      Customer customer = customerRepository.findById(updateRestaurantForm.getCustomerId()).orElseThrow(()
          -> new NotFoundException("Customer not found", ErrorCode.CUSTOMER_ERROR_NOT_FOUND));

      if (WinWinConstant.STATUS_ACTIVE.equals(restaurant.getStatus()) && !WinWinConstant.STATUS_ACTIVE.equals(customer.getStatus())){
        throw new BadRequestException("Customer not active", ErrorCode.CUSTOMER_ERROR_NOT_ACTIVE);
      }
      restaurant.setCustomer(customer);
    }

    if (StringUtils.isNotBlank(updateRestaurantForm.getRestaurantName()) && !restaurant.getRestaurantName().equals(updateRestaurantForm.getRestaurantName())
    && restaurantRepository.existsByRestaurantName(updateRestaurantForm.getRestaurantName())){
        throw new BadRequestException("Restaurant name already exist", ErrorCode.RESTAURANT_ERROR_EXIST);
    }

    restaurantMapper.updateFromRestaurantFormToEntity(updateRestaurantForm, restaurant);
    restaurantRepository.save(restaurant);
    apiMessageDto.setMessage("Update restaurant successfully");
    return apiMessageDto;
  }

  @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('RES_D')")
  public ApiMessageDto<String> delete(@PathVariable("id") Long id){
    if (!isSuperAdmin()){
      throw new UnauthorizationException("Restaurant is not allowed", ErrorCode.RESTAURANT_ERROR_UN_AUTHORIZE);
    }

    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()
    -> new NotFoundException("Restaurant not found", ErrorCode.RESTAURANT_ERROR_NOT_FOUND));

    DbConfig dbConfig = dbConfigRepository.findByRestaurantId(id).orElse(null);
    if (dbConfig != null){
      ServerProvider serverProvider = dbConfig.getServerProvider();
      TenantUtils.removeTenantResources(dbConfig);
      dbConfigRepository.delete(dbConfig);
      if (serverProvider.getCurrentTenantCount() > 0){
        serverProvider.setCurrentTenantCount(serverProvider.getCurrentTenantCount() - 1);
        serverProviderRepository.save(serverProvider);
      }
    }

    restaurantRepository.delete(restaurant);
    apiMessageDto.setMessage("Delete restaurant successfully");
    return apiMessageDto;
  }
}
