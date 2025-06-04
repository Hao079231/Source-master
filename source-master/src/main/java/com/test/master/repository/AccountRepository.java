package com.test.master.repository;

import com.test.master.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    public Account findAccountByUsername(String username);
    public Account findAccountByEmail(String email);
    public Account findAccountByResetPwdCode(String resetPwdCode);
    public Account findAccountByEmailOrUsername(String email, String username);
    public Page<Account> findAllByKind(int kind, Pageable pageable);
    public Optional<Account> findByIdAndKind(Long id, Integer kind);
    public List<Account> findByParentId(Long parentId);
    @Modifying
    @Transactional
    @Query
    public void deleteAllByParentId(Long parentId);
}
