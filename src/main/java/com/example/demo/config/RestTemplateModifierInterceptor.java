package com.example.demo.config;

import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Log4j2
public class RestTemplateModifierInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest req, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String requestId = MDC.get("requestId");
        req.getHeaders().add("requestId", requestId);
        ClientHttpResponse response = execution.execute(req, body);
        return response;
    }
}
