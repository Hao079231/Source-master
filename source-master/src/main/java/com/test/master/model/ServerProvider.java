package com.test.master.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_master_server_provider")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ServerProvider extends Auditable<String>{
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.test.master.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;

    private String name;
    private String url;
    private Integer maxTenant;

    @Column(name = "current_tenant_count")
    private Integer currentTenantCount = 0;

    private String mySqlJdbcUrl;
    private String mySqlRootUser;
    private String mySqlRootPassword;

    @Column(name = "driver_class_name")
    private String driverClassName;
}
