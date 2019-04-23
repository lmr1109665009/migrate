package com.chen.common.component;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * restTemplate拦截器
 * @user 子华
 * @created 2018/8/16
 */
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

 
  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
      throws IOException {
    HttpHeaders headers = request.getHeaders();
    ServletRequestAttributes requestAttributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (requestAttributes!=null){
      HttpServletRequest req=requestAttributes.getRequest();
      Enumeration<String> headerNames = req.getHeaderNames();
      while (headerNames.hasMoreElements()) {
        String key = (String) headerNames.nextElement();
        String value = req.getHeader(key);
        headers.add(key, value);
      }
    }
    // 保证请求继续被执行
    return execution.execute(request, body);
  }
}
