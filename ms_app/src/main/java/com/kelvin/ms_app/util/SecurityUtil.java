package com.kelvin.ms_app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    public static Map<String, Object> getCurrentUserDecodedClaims() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> claims = new HashMap<>();

        if (ObjectUtils.isEmpty(authentication)) {
            return claims;
        }

        if (authentication instanceof BearerTokenAuthentication) {
            claims = ((BearerTokenAuthentication) authentication).getTokenAttributes();
        }else if (authentication instanceof JwtAuthenticationToken) {
            claims = ((JwtAuthenticationToken) authentication).getToken().getClaims();
        }else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            claims = ((DefaultOidcUser) authentication.getPrincipal()).getAttributes();
        }

        return claims;
    }

    public static String getCurrentUserUsername() {
        return (String) getCurrentUserDecodedClaims().getOrDefault("preferred_username", null);
    }


}
