package com.exo.hrm_project.utils.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class ResponsePage<T> {

  private List<T> content;
  @JsonProperty("page")
  private int pageNumber;
  @JsonProperty("size")
  private int pageSize;
  private long totalElements;
  private int totalPages;
  private int numberOfElements;
  private String sort;
}
