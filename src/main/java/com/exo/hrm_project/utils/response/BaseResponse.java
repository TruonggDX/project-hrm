package com.exo.hrm_project.utils.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {

  private int code;
  private String message;
  private T data;

  public static <T> BaseResponse<T> success(T data, String message) {
    BaseResponse<T> response = new BaseResponse<>();
    response.setCode(HttpStatus.OK.value());
    response.setMessage(message);
    response.setData(data);
    return response;
  }

  public static <T> BaseResponse<T> notFound(String message) {
    BaseResponse<T> response = new BaseResponse<>();
    response.setCode(HttpStatus.NOT_FOUND.value());
    response.setMessage(message);
    return response;
  }
}
