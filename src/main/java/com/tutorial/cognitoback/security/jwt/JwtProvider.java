package com.tutorial.cognitoback.security.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@Slf4j
public class JwtProvider {

    private static final String USERNAME_FIELD = "username";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    @Value("${com.tutorial.jwt.aws.identityPoolUrl}")
    private String identityPoolUrl;

    @Autowired
    ConfigurableJWTProcessor configurableJWTProcessor;

    public Authentication authenticate(HttpServletRequest request) throws Exception {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            JWTClaimsSet claims = configurableJWTProcessor.process(getToken(token), null);
            validateToken(claims);
            String username = getUsername(claims);
            if (username != null) {
                // TODO set roles
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                User user = new User(username, "", authorities);
                return new JwtAuthenticator(authorities, user, claims);
            }
        }
        return null;
    }

    private String getUsername(JWTClaimsSet claims) {
        return claims.getClaim(USERNAME_FIELD).toString();
    }

    private void validateToken(JWTClaimsSet claims) throws Exception {
        if(!claims.getIssuer().equals(identityPoolUrl))
            throw new Exception("JWT not valid");
    }

    private String getToken(String token) {
        return token.startsWith(BEARER) ? token.substring(BEARER.length()) : token;
    }
}
