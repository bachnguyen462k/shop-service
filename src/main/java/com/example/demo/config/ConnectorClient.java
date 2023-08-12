package com.example.demo.config;

import com.example.demo.exception.TimeoutRestTemplateException;
import com.example.demo.model.base.BaseRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@Log4j2
public class ConnectorClient {

    @Autowired
    private RestTemplate restTemplate;

    public <T> List<T> exchangeAsList(String uri, ParameterizedTypeReference<List<T>> responseType) {
        return restTemplate.exchange(uri, HttpMethod.POST, null, responseType).getBody();
    }

    public <T> T post(final BaseRequest request, String path, ParameterizedTypeReference<T> responseType)  {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(restTemplate.getUriTemplateHandler().expand("") + path, HttpMethod.POST,
                    new HttpEntity<>(request, buildHeaders(request)), responseType);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            if(e.getMessage().contains("java.net.SocketTimeoutException")){
                throw new TimeoutRestTemplateException(path);
            }
            return null;
        }
    }

    private HttpHeaders buildHeaders(BaseRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
