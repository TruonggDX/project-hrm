package com.exo.hrm_project.specification.filter;

import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.SearchCriteria;

public class FilterGroupAllowance {

  public static <T> GenericSpecification<T> build(String code, String name, Boolean isActive) {
    GenericSpecification<T> spec = new GenericSpecification<>();

    if (code != null && !code.isEmpty()) {
      spec.add(new SearchCriteria("code", ":", code));
    }
    if (name != null && !name.isEmpty()) {
      spec.add(new SearchCriteria("name", ":", name));
    }
    if (isActive != null) {
      spec.add(new SearchCriteria("isActive", "=", isActive));
    }
    return spec;
  }
}
