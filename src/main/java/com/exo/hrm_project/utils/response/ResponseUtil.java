package com.exo.hrm_project.utils.response;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ResponseUtil {

  private final MessageSource messageSource;

  private Locale getCurrentLocale() {
    ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attrs != null) {
      HttpServletRequest request = attrs.getRequest();
      LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
      if (localeResolver != null) {
        return localeResolver.resolveLocale(request);
      }
    }
    return Locale.getDefault();
  }

  public <T> BaseResponse<T> success(T data, String messageKey) {
    String message = messageSource.getMessage(messageKey, null, getCurrentLocale());
    return new BaseResponse<>(HttpStatus.OK.value(), message, data);
  }

  public <T> BaseResponse<T> notFound(String messageKey) {
    String message = messageSource.getMessage(messageKey, null, getCurrentLocale());
    return new BaseResponse<>(HttpStatus.NOT_FOUND.value(), message, null);
  }
}
