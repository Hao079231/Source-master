package com.test.master.model.criteria;

import com.test.master.model.Account;
import com.test.master.model.Customer;
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
public class CustomerCriteria {
  private String fullname;
  private Integer status;

  public Specification<Customer> getSpecification() {
    return new Specification<Customer>() {
      private static final long serialVersionUID = 1L;

      @Override
      public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (getFullname() != null) {
          Join<Customer, Account> customerJoin = root.join("account", JoinType.INNER);
          predicates.add(cb.like(cb.lower(customerJoin.get("fullName")), "%" + getFullname().toLowerCase() + "%"));
        }
        if (getStatus() != null) {
          predicates.add(cb.equal(root.get("status"), getStatus()));
        }
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    };
  }
}
