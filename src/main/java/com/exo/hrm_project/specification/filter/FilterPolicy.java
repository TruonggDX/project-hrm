package com.exo.hrm_project.specification.filter;

import com.exo.hrm_project.dto.common.FilterRequest;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.SearchCriteria;

public class FilterPolicy {

  public static <T> GenericSpecification<T> build(FilterRequest filterRequest) {
    GenericSpecification<T> spec = new GenericSpecification<>();

    if (filterRequest.getCode() != null && !filterRequest.getCode().isEmpty()) {
      spec.add(new SearchCriteria("code", ":", filterRequest.getCode()));
    }
    if (filterRequest.getName() != null && !filterRequest.getName().isEmpty()) {
      spec.add(new SearchCriteria("name", ":", filterRequest.getName()));
    }
    if (filterRequest.getApplicableType() != null
        && !filterRequest.getApplicableType().isEmpty()) {
      spec.add(
          new SearchCriteria("applicableType", "=", filterRequest.getApplicableType()));
    }
    if (filterRequest.getType() != null && !filterRequest.getType().isEmpty()) {
      spec.add(new SearchCriteria("type", ":", filterRequest.getType()));
    }
    if (filterRequest.getStartDate() != null) {
      spec.add(new SearchCriteria("startDate", ":", filterRequest.getStartDate()));
    }
    if (filterRequest.getEndDate() != null) {
      spec.add(new SearchCriteria("endDate", "=", filterRequest.getEndDate()));
    }
    if (filterRequest.getState() != null && !filterRequest.getState().isEmpty()) {
      spec.add(new SearchCriteria("state", "=", filterRequest.getState()));
    }
    return spec;
  }
}
