package com.test.master.repository;


import com.test.master.model.ServerProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServerProviderRepository extends JpaRepository<ServerProvider, Long>, JpaSpecificationExecutor<ServerProvider> {
    ServerProvider findFirstByUrl(String url);
}
