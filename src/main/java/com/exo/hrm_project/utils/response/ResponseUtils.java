package com.exo.hrm_project.utils.response;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public class ResponseUtils {

  public static <T> BaseResponse<ResponsePage<T>> toPageResponse(Page<?> page, List<T> content,
      String message) {
    ResponsePage<T> responsePage = new ResponsePage<>();
    responsePage.setPageNumber(page.getNumber());
    responsePage.setPageSize(page.getSize());
    responsePage.setTotalElements(page.getTotalElements());
    responsePage.setTotalPages(page.getTotalPages());
    responsePage.setContent(content);
    responsePage.setNumberOfElements(page.getNumberOfElements());
    responsePage.setSort(page.getSort().toString());
    BaseResponse<ResponsePage<T>> response = new BaseResponse<>();
    response.setData(responsePage);
    response.setMessage(message);
    response.setCode(HttpStatus.OK.value());

    return response;
  }

}
