package com.kelvin.ms_app.config;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class ApiLogInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger("ExternalApiLogger");

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        ClientHttpResponse response = execution.execute(request, body);
        logRequestAndResponse(request, body, response);

        return response;
    }

    private void logRequestAndResponse(HttpRequest request, byte[] requestBody, ClientHttpResponse response) throws IOException {
        logger.info("\nURI      : {} {} {}\n"
                        + "Params   : {}\n"
                        + "Response : {}\n",
                request.getMethod() ,request.getURI(), response.getStatusCode(),
                new String(requestBody, "UTF-8"),
                StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()) );
    }
}
