package com.kelvin.ms_app.config;

import java.io.IOException;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class AddForwardedHeaderAttributeFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        {

            // spring's thread local requestContext
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

            try {

                String xForwardFor = request.getHeader("x-forwarded-for");

                if (ObjectUtils.isEmpty(xForwardFor)) {
                    // is empty then set remote address as the header
                    xForwardFor = request.getRemoteAddr();
                }

                if(!ObjectUtils.isEmpty(requestAttributes)) {
                    requestAttributes.setAttribute("xForwardFor", xForwardFor, RequestAttributes.SCOPE_REQUEST);
                }

                // go to next filter chain
                filterChain.doFilter(request, response);

            } finally {
                if(!ObjectUtils.isEmpty(requestAttributes)) {
                    requestAttributes.removeAttribute("xForwardFor", RequestAttributes.SCOPE_REQUEST);
                }
            }

        }
    }
}
