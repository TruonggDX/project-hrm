package com.exo.hrm_project.specification;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCriteria {

  private String key;
  private String operation;
  private Object value;
}
