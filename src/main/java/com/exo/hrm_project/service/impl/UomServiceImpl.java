package com.exo.hrm_project.service.impl;


import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.common.CommonListResponseDto;
import com.exo.hrm_project.service.IUomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UomServiceImpl implements IUomService {

  private final RestTemplate restTemplate;

  @Override
  public CommonDto getUomById(Long id) {
    String url =
        "https://product-manufactor-service.dev.apusplatform.com/api/v1/unit/list?page=0&size=20&ids="
            + id;
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set("Authorization",
        "Bearer eyJraWQiOiJkZWY3ZDIxZi1lMTY5LTRiYzEtODYyMi05NDY0OTIxZWU1ZTAiLCJhbGciOiJSUzI1NiJ9.eyJ0ZW5hbnRfaWQiOiIyMDEiLCJzdWIiOiJhcG9kaW9AZ21haWwuY29tIiwiaXNfb3duZXIiOnRydWUsImlzcyI6Imh0dHA6Ly8xMC4wLjEuOTY6ODA4MSIsImF1dGhvcml0aWVzIjpbIk1hbnVmYWN0b3J5IiwiQkxETk0iXSwiYXVkIjoidGVzdCIsIm5iZiI6MTc0NzEwNzI4NywidXNlcl9pZCI6NTgsIm9yZ19pZCI6MSwic2NvcGUiOlsiZ2F0ZXdheSJdLCJleHAiOjE3NDc5NzEyODcsImlhdCI6MTc0NzEwNzI4NywianRpIjoiYTE0ZDk5ZjMtZDE4MC00NzI3LTg5NDQtOWQ3ZDJhNDgyMTJjIn0.Z13K01YTQMn92enty4z5PQjW7HDBHsHobsaRRv2faCJfk8qRHKah8YITzaJJfUBdRFhEZQFfuZppu6PnyqW0roMS_5ZXiEvmkil-NL17V96lQ-P5jTNJgY2HYmUHT5UDXPn_CM2KhyoicnVyG7Eicdjavk0BPZlMu87r5ry5sbfA6tfxQMgNUoNDr-d-Q7XBQj26bZDuQ64qa0Nblumfq3hlXhMFBipoNaeICpNX3qtMsj9Z_A1Hgn7VWBEUu5tYME0jfbPwbGJyD4n1EThfo_RNpnaV-0gKsql9_sd7_d-hB1OP0LYmCmNJvTk2IjTBBI4WdgQL1egpMfN-i2AJ2A"); // <-- Thay token thật
    headers.set("X-TenantId", "201");
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<CommonListResponseDto> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        CommonListResponseDto.class
    );
    List<CommonDto> content = response.getBody()
        .getData()
        .getContent();
    return (content != null && !content.isEmpty()) ? content.get(0) : null;
  }
}
