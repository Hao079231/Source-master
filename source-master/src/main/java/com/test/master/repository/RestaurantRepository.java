package com.test.master.repository;

import com.test.master.model.Restaurant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>,
    JpaSpecificationExecutor<Restaurant> {

  Optional<Restaurant> findFirstByTenantId(String tenantId);

  boolean existsByCustomerId(Long id);

  Optional<Restaurant> findFirstByCustomerId(Long customerId);

  boolean existsByRestaurantName(String restaurantName);
}
