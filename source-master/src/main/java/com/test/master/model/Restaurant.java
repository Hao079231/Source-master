package com.test.master.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "db_master_restaurant")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant extends Auditable<String>{
  @Id
  @GenericGenerator(name = "idGenerator", strategy = "com.test.master.service.id.IdGenerator")
  @GeneratedValue(generator = "idGenerator")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  private String tenantId;

  @Column(name = "restaurant_name")
  private String restaurantName;

  @Column(name = "logo_path")
  private String logoPath;

  @Column(name = "banner_path")
  private String bannerPath;

  @Column(name = "hotline")
  private String hotline;

  @Column(name = "settings" ,  columnDefinition = "TEXT")
  private String settings;

  @Column(name = "lang")
  private String lang;

  @OneToOne(mappedBy = "restaurant", fetch = FetchType.LAZY)
  private DbConfig dbConfig;

  @Column(name = "city")
  private String city;

  @Column(name = "address")
  private String address;
}
