package com.kelvin.ms_app.config;

import com.kelvin.ms_app.util.Common;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.ServletRequestPathUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

@Component
public class RequestResponseLogFilter extends OncePerRequestFilter {

    private static final Logger apiLogger = LoggerFactory.getLogger("ApiLogger");

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping handlerMapping;


    Common com = new Common();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        HandlerMethod currentHandlerMethod = null;

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);

            // Attempt to get the current handler method after the request has been processed
            currentHandlerMethod = this.getCurrentHandlerMethod(wrappedRequest);

            // Log the request and response
            byte[] requestBytes = wrappedRequest.getContentAsByteArray();
            String requestBody = this.getContentAsString(requestBytes, request.getCharacterEncoding());

            byte[] responseBytes = wrappedResponse.getContentAsByteArray();
            String responseBody = getContentAsString(responseBytes, response.getCharacterEncoding());

            String xForwardedFor = com.Null2String(wrappedRequest.getHeader("x-forwarded-for"));
            String requestParams = wrappedRequest.getParameterMap().entrySet().stream().map(e -> e.getKey() + "=" + request.getParameter(e.getKey())).collect(Collectors.joining(", "));

            String actionName = getRequestActionName(currentHandlerMethod);
            apiLogger.info("\n"
                            + "X-Forwarded-For : {}\n"
                            + "Remote Addr     : {}\n"
                            + "Action          : {}\n"
                            + "Request URL     : {}\n"
                            + "Request Method  : {}\n"
                            + "Request Params  : {}\n"
                            + "Request Body    : {}\n"
                            + "Response Code   : {}\n"
                            + "Response Body   : {}\n",
                    xForwardedFor,
                    request.getRemoteAddr(),
                    actionName,
                    wrappedRequest.getRequestURI(),
                    wrappedRequest.getMethod(),
                    requestParams,
                    requestBody,
                    Integer.toString(wrappedResponse.getStatus()),
                    responseBody);

        } finally {
            wrappedResponse.copyBodyToResponse();
        }
    }

    private String getContentAsString(byte[] buf, String charsetName) {
        if (buf == null || buf.length == 0)
            return "";
        try {
            return new String(buf, 0, buf.length, charsetName);
        } catch (UnsupportedEncodingException ex) {
            return "Unsupported Encoding";
        }
    }

    private HandlerMethod getCurrentHandlerMethod(HttpServletRequest request) {
        HandlerMethod currentHandlerMethod = null;

        try {
            if (!ServletRequestPathUtils.hasParsedRequestPath(request)) {
                ServletRequestPathUtils.parseAndCache(request);
            }

            HandlerExecutionChain handlerExecutionChain = handlerMapping.getHandler(request);

            if (ObjectUtils.isEmpty(handlerExecutionChain)) {
                return null;
            }

            Object handler = handlerExecutionChain.getHandler();

            if (handler instanceof HandlerMethod) {
                currentHandlerMethod = (HandlerMethod) handler;
                apiLogger.debug("HandlerMethod found: " + currentHandlerMethod.getMethod().getName());
            }

        } catch (Exception e) {
            apiLogger.error("getCurrentHandlerMethod error", e);
        }

        return currentHandlerMethod;
    }

    private String getRequestActionName(HandlerMethod currentHandlerMethod) {
        String actionName = "";

        try {
            if (ObjectUtils.isEmpty(currentHandlerMethod)) {
                return actionName;
            }

            ReqResLogging reqResLogging = currentHandlerMethod.getMethodAnnotation(ReqResLogging.class);

            if (!ObjectUtils.isEmpty(reqResLogging) && !ObjectUtils.isEmpty(reqResLogging.name())) {
                actionName = reqResLogging.name();
                apiLogger.debug("ReqResLogging action name: " + actionName);
            } else {
                actionName = currentHandlerMethod.getMethod().getName();
                apiLogger.debug("Default action name: " + actionName);
            }
        } catch (Exception e) {
            apiLogger.error("getRequestActionName Error", e);
        }

        return actionName;
    }
}
