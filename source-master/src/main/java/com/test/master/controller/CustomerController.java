package com.test.master.controller;

import com.test.master.constant.WinWinConstant;
import com.test.master.dto.ApiMessageDto;
import com.test.master.dto.ErrorCode;
import com.test.master.dto.ResponseListDto;
import com.test.master.dto.customer.CustomerDto;
import com.test.master.exception.BadRequestException;
import com.test.master.exception.NotFoundException;
import com.test.master.form.customer.CreateCustomerForm;
import com.test.master.form.customer.UpdateCustomerForm;
import com.test.master.form.customer.UpdateProfileCustomerForm;
import com.test.master.mapper.AccountMapper;
import com.test.master.mapper.CustomerMapper;
import com.test.master.model.Account;
import com.test.master.model.Customer;
import com.test.master.model.Group;
import com.test.master.model.criteria.CustomerCriteria;
import com.test.master.repository.AccountRepository;
import com.test.master.repository.CustomerRepository;
import com.test.master.repository.GroupRepository;
import com.test.master.repository.RestaurantRepository;
import com.test.master.service.WinWinApiService;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequestMapping("/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CustomerController extends ABasicController{
  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  CustomerMapper customerMapper;
  @Autowired
  AccountRepository accountRepository;
  @Autowired
  AccountMapper accountMapper;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  GroupRepository groupRepository;
  @Autowired
  RestaurantRepository restaurantRepository;
  @Autowired
  WinWinApiService winWinApiService;

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('CUS_C')")
  public ApiMessageDto<String> create(@Valid @RequestBody CreateCustomerForm createCustomerForm, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Account account = accountRepository.findAccountByUsername(createCustomerForm.getUsername());
    if (account != null){
      throw new BadRequestException("Username already exist!", ErrorCode.ACCOUNT_ERROR_USERNAME_EXIST);
    }
    account = accountRepository.findAccountByEmail(createCustomerForm.getEmail());
    if (account != null){
      throw new BadRequestException("Email already exist!", ErrorCode.ACCOUNT_ERROR_USERNAME_EXIST);
    }
    Group group = groupRepository.findFirstByKindAndIsSystemRole(WinWinConstant.GROUP_KIND_MANAGER, true).orElse(null);
    if (group == null){
      throw new BadRequestException("Group not found", ErrorCode.GROUP_ERROR_NOT_FOUND);
    }

    account = accountMapper.fromCustomerFormToEntity(createCustomerForm);
    account.setPassword(passwordEncoder.encode(createCustomerForm.getPassword()));
    account.setGroup(group);
    account.setKind(WinWinConstant.GROUP_KIND_MANAGER);
    accountRepository.save(account);

    Customer customer = customerMapper.fromCreateCustomerFormToEntity(createCustomerForm);
    customer.setAccount(account);
    customerRepository.save(customer);

    apiMessageDto.setMessage("Create customer successfully");
    return apiMessageDto;
  }

  @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('CUS_L')")
  public ApiMessageDto<ResponseListDto<List<CustomerDto>>> getList(CustomerCriteria customerCriteria, Pageable pageable){
    ApiMessageDto apiMessageDto = new ApiMessageDto();
    Page<Customer> customers = customerRepository.findAll(customerCriteria.getSpecification(), pageable);
    ResponseListDto<List<CustomerDto>> responseListDto = new ResponseListDto<>(customerMapper.fromEntityToCustomerDtoList(customers.getContent()), customers.getTotalElements(), customers.getTotalPages());
    apiMessageDto.setData(responseListDto);
    apiMessageDto.setMessage("Get list customer successfully");
    return apiMessageDto;
  }

  @GetMapping(value = "/get/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('CUS_V')")
  public ApiMessageDto<CustomerDto> get(@PathVariable(value = "customerId") Long id){
    ApiMessageDto apiMessageDto = new ApiMessageDto();
    Customer customer = customerRepository.findById(id).orElseThrow(()
    -> new NotFoundException("Customer not found", ErrorCode.CUSTOMER_ERROR_NOT_FOUND));
    apiMessageDto.setData(customerMapper.fromEntityToCustomerDto(customer));
    apiMessageDto.setMessage("Get customer successfully");
    return apiMessageDto;
  }

  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
//  @PreAuthorize("hasRole('CUS_C_P')")
  public ApiMessageDto<CustomerDto> getProfile(){
    ApiMessageDto<CustomerDto> apiMessageDto = new ApiMessageDto<>();
    Customer customer = customerRepository.findById(getCurrentUser()).orElseThrow(() ->
        new NotFoundException("Customer not found", ErrorCode.CUSTOMER_ERROR_NOT_FOUND));
    apiMessageDto.setData(customerMapper.fromEntityToProfileCustomerDto(customer));
    apiMessageDto.setMessage("Get profile successfully");
    return apiMessageDto;
  }

  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('CUS_U')")
  public ApiMessageDto<String> update(@Valid @RequestBody UpdateCustomerForm updateCustomerForm, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Customer customer = customerRepository.findById(updateCustomerForm.getId()).orElseThrow(()
    -> new NotFoundException("Customer not found", ErrorCode.CUSTOMER_ERROR_NOT_FOUND));

    if (StringUtils.isNotBlank(updateCustomerForm.getNewPassword())){
      customer.getAccount().setPassword(passwordEncoder.encode(updateCustomerForm.getNewPassword()));
    }

    if (StringUtils.isNotBlank(updateCustomerForm.getUsername()) && !customer.getAccount().getUsername().equals(updateCustomerForm.getUsername())
    && accountRepository.findAccountByUsername(updateCustomerForm.getUsername()) != null){
      throw new BadRequestException("Username already exist!", ErrorCode.CUSTOMER_ERROR_EXIST);
    }

    if (StringUtils.isNotBlank(updateCustomerForm.getEmail()) && !customer.getAccount().getEmail().equals(updateCustomerForm.getEmail())
    && accountRepository.findAccountByEmail(updateCustomerForm.getEmail()) != null){
      throw new BadRequestException("Email already exist!", ErrorCode.CUSTOMER_ERROR_EXIST);
    }

    if (StringUtils.isNotBlank(updateCustomerForm.getAvatarPath())){
      if (!updateCustomerForm.getAvatarPath().equals(customer.getAccount().getAvatarPath())){
        winWinApiService.deleteFile(customer.getAccount().getAvatarPath());
      }
      customer.getAccount().setAvatarPath(updateCustomerForm.getAvatarPath());
    }

    if (StringUtils.isNotBlank(updateCustomerForm.getLogoPath())){
      if (!updateCustomerForm.getLogoPath().equals(customer.getLogoPath())){
        winWinApiService.deleteFile(customer.getLogoPath());
      }
      customer.setLogoPath(updateCustomerForm.getLogoPath());
    }

    accountMapper.updateCustomerFormToEntity(updateCustomerForm, customer.getAccount());
    customerMapper.updateCustomerFormToEntity(updateCustomerForm, customer);
    accountRepository.save(customer.getAccount());
    customerRepository.save(customer);
    apiMessageDto.setMessage("Update customer successfully");
    return apiMessageDto;
  }

  @PutMapping(value = "/client-update", produces = MediaType.APPLICATION_JSON_VALUE)
//  @PreAuthorize("hasRole('CUS_C_U')")
  public ApiMessageDto<String> updateProfile(@Valid @RequestBody UpdateProfileCustomerForm updateProfileCustomerForm, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Customer customer = customerRepository.findById(getCurrentUser()).orElseThrow(()
    -> new NotFoundException("Customer not found",ErrorCode.CUSTOMER_ERROR_NOT_FOUND));

    if (!WinWinConstant.STATUS_ACTIVE.equals(customer.getStatus())){
      throw new BadRequestException("Customer not active", ErrorCode.CUSTOMER_ERROR_NOT_ACTIVE);
    }

    if (StringUtils.isNotBlank(updateProfileCustomerForm.getEmail()) && !customer.getAccount().getEmail().equals(updateProfileCustomerForm.getEmail())
    && accountRepository.findAccountByEmail(updateProfileCustomerForm.getEmail()) != null){
      throw new BadRequestException("Email already exist", ErrorCode.CUSTOMER_ERROR_EXIST);
    }

    if (StringUtils.isNotBlank(updateProfileCustomerForm.getOldPassword()) && StringUtils.isNotBlank(updateProfileCustomerForm.getNewPassword())){
      if (!passwordEncoder.matches(updateProfileCustomerForm.getOldPassword(), customer.getAccount().getPassword())){
        throw new BadRequestException("Password invalid", ErrorCode.ACCOUNT_ERROR_WRONG_PASSWORD);
      }

      if (updateProfileCustomerForm.getOldPassword().equals(updateProfileCustomerForm.getNewPassword())){
        throw new BadRequestException("New password must be different from old password");
      }
      customer.getAccount().setPassword(passwordEncoder.encode(updateProfileCustomerForm.getNewPassword()));
    }

    if (StringUtils.isNotBlank(updateProfileCustomerForm.getAvatarPath())){
      if (!updateProfileCustomerForm.getAvatarPath().equals(customer.getAccount().getAvatarPath())){
        winWinApiService.deleteFile(customer.getAccount().getAvatarPath());
      }
      customer.getAccount().setAvatarPath(updateProfileCustomerForm.getAvatarPath());
    }

    if (StringUtils.isNotBlank(updateProfileCustomerForm.getLogoPath())){
      if (!updateProfileCustomerForm.getLogoPath().equals(customer.getLogoPath())){
        winWinApiService.deleteFile(customer.getLogoPath());
      }
      customer.setLogoPath(updateProfileCustomerForm.getLogoPath());
    }

    accountMapper.updateProfileCustomerFormToEntity(updateProfileCustomerForm, customer.getAccount());
    customerMapper.updateProfileCustomerFormToEntity(updateProfileCustomerForm, customer);
    accountRepository.save(customer.getAccount());
    customerRepository.save(customer);
    apiMessageDto.setMessage("Update profile successfully");
    return apiMessageDto;
  }

  @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('CUS_D')")
  public ApiMessageDto<String> delete(@PathVariable("id") Long id){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Customer customer = customerRepository.findById(id).orElseThrow(()
    -> new BadRequestException("Customer not found", ErrorCode.CUSTOMER_ERROR_NOT_FOUND));

    if (restaurantRepository.existsByCustomerId(customer.getId())){
      throw new BadRequestException("The restaurant has served this customer", ErrorCode.RESTAURANT_ERROR_EXIST);
    }

    if (StringUtils.isNotBlank(customer.getAccount().getAvatarPath())){
      winWinApiService.deleteFile(customer.getAccount().getAvatarPath());
    }

    if (StringUtils.isNotBlank(customer.getLogoPath())){
      winWinApiService.deleteFile(customer.getLogoPath());
    }

    customerRepository.deleteById(id);
    accountRepository.deleteById(id);
    apiMessageDto.setMessage("Delete customer successfully");
    return apiMessageDto;
  }

}
