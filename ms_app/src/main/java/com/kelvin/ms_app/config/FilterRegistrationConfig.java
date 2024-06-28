package com.kelvin.ms_app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterRegistrationConfig {

    private static final Logger logger = LoggerFactory.getLogger(FilterRegistrationConfig.class);

    @Value("${api-logging.url-patterns}")
    private String[] urlPattern;

    @Bean
    public FilterRegistrationBean<AddForwardedHeaderAttributeFilter> forwardedHeaderFilter(){
        FilterRegistrationBean<AddForwardedHeaderAttributeFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AddForwardedHeaderAttributeFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(0);
        logger.info("AddForwardedHeaderAttributeFilter is initialized.");
        return registrationBean;
    }

    @Bean
    @ConditionalOnProperty(value="api-logging.request-response-filter.enabled",
            havingValue = "true", matchIfMissing = false)
    public FilterRegistrationBean<RequestResponseLogFilter> loggingFilter(RequestResponseLogFilter requestResponseLogFilter){
        FilterRegistrationBean<RequestResponseLogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(requestResponseLogFilter);
        registrationBean.addUrlPatterns(urlPattern); // URL
        registrationBean.setOrder(1);
        logger.info("RequestResponseLogFilter is initialized.");
        return registrationBean;
    }

    @Bean
    public RequestResponseLogFilter requestResponseLogFilter() {
        return new RequestResponseLogFilter();
    }
}
