package com.hyuseinlesho.powerlog.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    private String secretKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
    private long rememberMeRefreshTokenExpiration;
    private long accessTokenCookieMaxAge;
    private long refreshTokenCookieMaxAge;
    private long rememberMeRefreshTokenCookieMaxAge;
}
