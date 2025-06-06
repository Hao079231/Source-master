package com.test.master.dto.customer;

import com.test.master.dto.ABasicAdminDto;
import com.test.master.dto.account.AccountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto extends ABasicAdminDto {
  private String logoPath;
  private AccountDto account;
}
