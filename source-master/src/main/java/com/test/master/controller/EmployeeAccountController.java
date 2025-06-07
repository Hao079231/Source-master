package com.test.master.controller;


import com.test.master.constant.WinWinConstant;
import com.test.master.dto.ApiResponse;
import com.test.master.dto.ErrorCode;
import com.test.master.dto.ResponseListDto;
import com.test.master.dto.employee.EmployeeAccountAutoCompleteDto;
import com.test.master.dto.employee.EmployeeAccountDto;
import com.test.master.form.employee.CreateEmployeeAccountAdminForm;
import com.test.master.form.employee.UpdateEmployeeAccountAdminForm;
import com.test.master.mapper.EmployeeAccountMapper;
import com.test.master.model.Account;
import com.test.master.model.Group;
import com.test.master.model.criteria.EmployeeAccountCriteria;
import com.test.master.repository.AccountRepository;
import com.test.master.repository.GroupRepository;
import com.test.master.service.WinWinApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/v1/employee-account")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class EmployeeAccountController extends ABasicController{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private EmployeeAccountMapper employeeAccountMapper;

    @Autowired
    private WinWinApiService winWinApiService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EACC_L')")
    public ApiResponse<ResponseListDto<EmployeeAccountDto>> list(EmployeeAccountCriteria accountCriteria, Pageable pageable) {
        accountCriteria.setKind(WinWinConstant.USER_KIND_EMPLOYEE);
        ApiResponse<ResponseListDto<EmployeeAccountDto>> apiMessageDto = new ApiResponse<>();
        Page<Account> careerList = accountRepository.findAll(accountCriteria.getSpecification() , pageable);
        ResponseListDto<EmployeeAccountDto> responseListDto =
                new ResponseListDto(employeeAccountMapper.convertEmployeeAccountToDto(careerList.getContent()), careerList.getTotalElements(), careerList.getTotalPages());
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get career list success");
        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EACC_C')")
    public ApiResponse<String> createAdmin(@Valid @RequestBody CreateEmployeeAccountAdminForm createForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Account existUsernameAccount = accountRepository.findAccountByUsername(createForm.getUsername());
        if (existUsernameAccount != null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_USERNAME_EXIST);
            apiMessageDto.setMessage("Username existed");
            return apiMessageDto;
        }
        Group group = groupRepository.findById(createForm.getGroupId()).orElse(null);
        if (group == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_UNKNOWN);
            return apiMessageDto;
        }
        Account parent = accountRepository.findById(createForm.getParentId()).orElse(null);
        if (parent == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Parent not exist");
            return apiMessageDto;
        }
        if (!Objects.equals(parent.getKind(),WinWinConstant.USER_KIND_MANAGER)) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_ACCOUNT_IS_NOT_MANAGER);
            apiMessageDto.setMessage("Parent not valid");
            return apiMessageDto;
        }
        Account account = employeeAccountMapper.fromEmployeeAccountCreateFormToAccount(createForm);
        account.setPassword(passwordEncoder.encode(createForm.getPassword()));
        account.setKind(WinWinConstant.USER_KIND_EMPLOYEE);
        account.setGroup(group);
        account.setParent(parent);
        accountRepository.save(account);
        apiMessageDto.setMessage("Create employee success");
        return apiMessageDto;

    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EACC_U')")
    public ApiResponse<String> updateAdmin(@Valid @RequestBody UpdateEmployeeAccountAdminForm updateForm, BindingResult bindingResult) {

        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Account account = accountRepository.findByIdAndKind(updateForm.getId(),WinWinConstant.USER_KIND_EMPLOYEE).orElse(null);
        if (account == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        Group group = groupRepository.findById(updateForm.getGroupId()).orElse(null);
        if (group == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_UNKNOWN);
            return apiMessageDto;
        }
        Account parent = accountRepository.findById(updateForm.getParentId()).orElse(null);
        if (parent == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Parent not exist");
            return apiMessageDto;
        }
        if(!Objects.equals(parent.getKind(),WinWinConstant.USER_KIND_MANAGER)){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_ACCOUNT_IS_NOT_MANAGER);
            apiMessageDto.setMessage("Parent not valid");
            return apiMessageDto;
        }
        if (StringUtils.isNoneBlank(updateForm.getAvatarPath())) {
            if(!updateForm.getAvatarPath().equals(account.getAvatarPath())){
                //delete old image
                winWinApiService.deleteFile(account.getAvatarPath());
            }
        }
        employeeAccountMapper.fromUpdateFormToEntity(updateForm,account);
        if (StringUtils.isNoneBlank(updateForm.getPassword())) {
            account.setPassword(passwordEncoder.encode(updateForm.getPassword()));
        }
        account.setGroup(group);
        account.setParent(parent);
        accountRepository.save(account);
        apiMessageDto.setMessage("Update employee success");
        return apiMessageDto;

    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EACC_V')")
    public ApiResponse<EmployeeAccountDto> get(@PathVariable("id") Long id) {
        ApiResponse<EmployeeAccountDto> apiMessageDto = new ApiResponse<>();
        Account account = accountRepository.findByIdAndKind(id,WinWinConstant.USER_KIND_EMPLOYEE).orElse(null);
        if (account == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        apiMessageDto.setData(employeeAccountMapper.fromEmployeeAccountDto(account));
        apiMessageDto.setMessage("Get employee success");
        return apiMessageDto;

    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EACC_D')")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Account account = accountRepository.findByIdAndKind(id,WinWinConstant.USER_KIND_EMPLOYEE).orElse(null);
        if (account == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if (account.getIsSuperAdmin()) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_NOT_DELETE_SUPPER_ADMIN);
            return apiMessageDto;
        }
        //delete avatar file
        winWinApiService.deleteFile(account.getAvatarPath());
        accountRepository.deleteById(id);
        apiMessageDto.setMessage("Delete employee success");
        return apiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ResponseListDto<EmployeeAccountAutoCompleteDto>> list(EmployeeAccountCriteria accountCriteria) {
        ApiResponse<ResponseListDto<EmployeeAccountAutoCompleteDto>> apiMessageDto = new ApiResponse<>();
        accountCriteria.setStatus(WinWinConstant.STATUS_ACTIVE);
        accountCriteria.setKind(WinWinConstant.USER_KIND_EMPLOYEE);
        Pageable pageable = PageRequest.of(0,10);
        Page<Account> careerList = accountRepository.findAll(accountCriteria.getSpecification() , pageable);
        ResponseListDto<EmployeeAccountAutoCompleteDto> responseListDto =
                new ResponseListDto(employeeAccountMapper.convertEmployeeAccountToAutoCompleteDto(careerList.getContent()), careerList.getTotalElements(), careerList.getTotalPages());
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get employee success");
        return apiMessageDto;
    }


}
