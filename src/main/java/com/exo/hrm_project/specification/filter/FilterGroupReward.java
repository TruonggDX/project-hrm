package com.exo.hrm_project.specification.filter;

import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.SearchCriteria;

public class FilterGroupReward {

  public static <T> GenericSpecification<T> build(String code, String name) {
    GenericSpecification<T> spec = new GenericSpecification<>();

    if (code != null && !code.isEmpty()) {
      spec.add(new SearchCriteria("code", ":", code));
    }
    if (name != null && !name.isEmpty()) {
      spec.add(new SearchCriteria("name", ":", name));
    }
    return spec;
  }
}
