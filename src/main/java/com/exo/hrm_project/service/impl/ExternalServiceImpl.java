package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.common.CommonListResponseDto;
import com.exo.hrm_project.service.IExternalService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements IExternalService {

  private final RestTemplate restTemplate;
  private static final String TOKEN = "Bearer eyJraWQiOiJkZWY3ZDIxZi1lMTY5LTRiYzEtODYyMi05NDY0OTIxZWU1ZTAiLCJhbGciOiJSUzI1NiJ9.eyJ0ZW5hbnRfaWQiOiIyMDEiLCJzdWIiOiJhcG9kaW9AZ21haWwuY29tIiwiaXNfb3duZXIiOnRydWUsImlzcyI6Imh0dHA6Ly8xMC4wLjEuOTY6ODA4MSIsImF1dGhvcml0aWVzIjpbIk1hbnVmYWN0b3J5IiwiQkxETk0iXSwiYXVkIjoidGVzdCIsIm5iZiI6MTc0NzEwNzI4NywidXNlcl9pZCI6NTgsIm9yZ19pZCI6MSwic2NvcGUiOlsiZ2F0ZXdheSJdLCJleHAiOjE3NDc5NzEyODcsImlhdCI6MTc0NzEwNzI4NywianRpIjoiYTE0ZDk5ZjMtZDE4MC00NzI3LTg5NDQtOWQ3ZDJhNDgyMTJjIn0.Z13K01YTQMn92enty4z5PQjW7HDBHsHobsaRRv2faCJfk8qRHKah8YITzaJJfUBdRFhEZQFfuZppu6PnyqW0roMS_5ZXiEvmkil-NL17V96lQ-P5jTNJgY2HYmUHT5UDXPn_CM2KhyoicnVyG7Eicdjavk0BPZlMu87r5ry5sbfA6tfxQMgNUoNDr-d-Q7XBQj26bZDuQ64qa0Nblumfq3hlXhMFBipoNaeICpNX3qtMsj9Z_A1Hgn7VWBEUu5tYME0jfbPwbGJyD4n1EThfo_RNpnaV-0gKsql9_sd7_d-hB1OP0LYmCmNJvTk2IjTBBI4WdgQL1egpMfN-i2AJ2A";
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String TENANT_ID_HEADER = "X-TenantId";
  private static final String TENANT_ID = "201";

  @Override
  public CommonDto getUomById(Long id) {
    String url =
        "https://product-manufactor-service.dev.apusplatform.com/api/v1/unit/list?page=0&size=20&ids="
            + id;
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set(AUTHORIZATION_HEADER, TOKEN);
    headers.set(TENANT_ID_HEADER, TENANT_ID);
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<CommonListResponseDto> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        CommonListResponseDto.class
    );
    CommonListResponseDto body = response.getBody();
    if (body != null && body.getData() != null && body.getData().getContent() != null) {
      List<CommonDto> content = body.getData().getContent();
      return !content.isEmpty() ? content.get(0) : null;
    }
    return null;
  }

  @Override
  public CommonDto getCurrencyById(Long id) {
    String url =
        "https://resources-service.dev.apusplatform.com/api/v1/currency/list?page=0&size=20&currencyIds="
            + id;
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set(AUTHORIZATION_HEADER, TOKEN);
    headers.set(TENANT_ID_HEADER, TENANT_ID);
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<CommonListResponseDto> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        CommonListResponseDto.class
    );
    CommonListResponseDto body = response.getBody();
    if (body != null && body.getData() != null && body.getData().getContent() != null) {
      List<CommonDto> content = body.getData().getContent();
      return !content.isEmpty() ? content.get(0) : null;
    }
    return null;
  }

  @Override
  public List<CommonDto> getEmployeeInfoByIds(List<Long> employeeIds) {
    if (employeeIds == null || employeeIds.isEmpty()) {
      return List.of();
    }
    try {
      String idsParam = employeeIds.stream()
          .map(String::valueOf)
          .collect(Collectors.joining(","));

      String url =
          "https://resources-service.dev.apusplatform.com/api/v1/employee/list?page=0&size=20&ids="
              + idsParam;

      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
      headers.set(AUTHORIZATION_HEADER, TOKEN);
      headers.set(TENANT_ID_HEADER, TENANT_ID);
      HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
      ResponseEntity<CommonListResponseDto> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          requestEntity,
          CommonListResponseDto.class
      );
      CommonListResponseDto body = response.getBody();
      if (body != null && body.getData() != null && body.getData().getContent() != null) {
        return body.getData().getContent();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return List.of();
  }

  @Override
  public List<CommonDto> getDepartmentInfoByIds(List<Long> departmentIds) {
    if (departmentIds == null || departmentIds.isEmpty()) {
      return List.of();
    }
    try {
      String idsParam = departmentIds.stream()
          .map(String::valueOf)
          .collect(Collectors.joining(","));
      String url =
          "https://resources-service.dev.apusplatform.com/api/v1/department/list?page=0&size=20&ids="
              + idsParam;
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
      headers.set(AUTHORIZATION_HEADER, TOKEN);
      headers.set(TENANT_ID_HEADER, TENANT_ID);
      HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
      ResponseEntity<CommonListResponseDto> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          requestEntity,
          CommonListResponseDto.class
      );
      CommonListResponseDto body = response.getBody();
      if (body != null && body.getData() != null && body.getData().getContent() != null) {
        return body.getData().getContent();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return List.of();
  }

  @Override
  public List<CommonDto> getPositionInfoByIds(List<Long> positionIds) {
    if (positionIds == null || positionIds.isEmpty()) {
      return List.of();
    }
    try {
      String idsParam = positionIds.stream()
          .map(String::valueOf)
          .collect(Collectors.joining(","));
      String url =
          "https://resources-service.dev.apusplatform.com/api/v1/position/list?page=0&size=20&ids="
              + idsParam;
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
      headers.set(AUTHORIZATION_HEADER, TOKEN);
      headers.set(TENANT_ID_HEADER, TENANT_ID);
      HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
      ResponseEntity<CommonListResponseDto> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          requestEntity,
          CommonListResponseDto.class
      );
      CommonListResponseDto body = response.getBody();
      if (body != null && body.getData() != null && body.getData().getContent() != null) {
        return body.getData().getContent();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return List.of();
  }

}
