package com.test.master.model.criteria;

import com.test.master.model.Customer;
import com.test.master.model.Restaurant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class RestaurantCriteria {
  private String restaurantName;
  private String city;
  private Long customerId;

  public Specification<Restaurant> getSpecification() {
    return new Specification<Restaurant>() {
      private static final long serialVersionUID = 1L;

      @Override
      public Predicate toPredicate(Root<Restaurant> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (getRestaurantName() != null) {
          predicates.add(cb.like(cb.lower(root.get("restaurantName")), "%" + getRestaurantName().toLowerCase() + "%"));
        }
        if (getCity() != null) {
          predicates.add(cb.like(cb.lower(root.get("city")), "%" + getCity().toLowerCase() + "%"));
        }
        if(getCustomerId() != null){
          Join<Restaurant, Customer> restaurantJoin = root.join("customer", JoinType.INNER);
          predicates.add(cb.equal(restaurantJoin.get("id"), getCustomerId()));
        }
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    };
  }
}
