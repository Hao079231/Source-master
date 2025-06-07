package com.test.master.repository;


import com.test.master.model.DbConfig;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DbConfigRepository extends JpaRepository<DbConfig, Long>, JpaSpecificationExecutor<DbConfig> {
    DbConfig findByName(String name);
    List<DbConfig> findAllByServerProviderId(Long id);
    List<DbConfig> findAllByInitialize(boolean isInit);

    DbConfig findFirstByRestaurantTenantId(String name);

//    @Query("SELECT d" +
//            " FROM DbConfig d" +
//            " JOIN Restaurant r ON d.restaurant = r" +
//            " WHERE r.id = :restaurantId")
//    DbConfig findByRestaurantId(@Param("restaurantId") Long restaurantId);

    Optional<DbConfig> findByRestaurantId(Long id);

    DbConfig findFirstByRestaurantId(Long restaurantId);
}
