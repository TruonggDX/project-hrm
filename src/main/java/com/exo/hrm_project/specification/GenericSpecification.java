package com.exo.hrm_project.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {

  private final List<SearchCriteria> criteriaList = new ArrayList<>();

  public void add(SearchCriteria criteria) {
    criteriaList.add(criteria);
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    List<Predicate> predicates = new ArrayList<>();

    for (SearchCriteria criteria : criteriaList) {
      switch (criteria.getOperation()) {
        case ":" -> predicates.add(cb.like(cb.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
        case "=" -> predicates.add(cb.equal(root.get(criteria.getKey()), criteria.getValue()));
      }
    }
    return cb.and(predicates.toArray(new Predicate[0]));
  }
}
