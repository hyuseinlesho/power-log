package com.hyuseinlesho.powerlog.security.jwt;

import com.hyuseinlesho.powerlog.security.jwt.refreshtoken.RefreshToken;
import com.hyuseinlesho.powerlog.security.jwt.refreshtoken.RefreshTokenService;
import com.hyuseinlesho.powerlog.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver,
            JwtProperties jwtProperties, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtProperties = jwtProperties;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = getCookieValue(request, "accessToken");
        String refreshToken = getCookieValue(request, "refreshToken");

        if (accessToken != null) {
            try {
                String username = jwtService.extractUsername(accessToken);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtService.isTokenValid(accessToken, userDetails)) {
                        setAuthentication(userDetails, request);
                    }
                }
                filterChain.doFilter(request, response);
            } catch (ExpiredJwtException e) {
                if (refreshToken != null) {
                    try {
                        handleExpiredAccessToken(refreshToken, response, request);
                        filterChain.doFilter(request, response);
                    } catch (RuntimeException re) {
                        CookieUtil.invalidateCookie(response, "accessToken");
                        CookieUtil.invalidateCookie(response, "refreshToken");
                        response.sendRedirect("/auth/login");
                    }
                } else {
                    handlerExceptionResolver.resolveException(request, response, null, e);
                }
            } catch (Exception exception) {
                handlerExceptionResolver.resolveException(request, response, null, exception);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void handleExpiredAccessToken(
            String refreshToken,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .ifPresent(user -> {
                    String newAccessToken = jwtService.generateToken(user);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                    setAuthentication(userDetails, request);

                    ResponseCookie newAccessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                            .httpOnly(true)
                            .secure(true)
                            .sameSite("Strict")
                            .path("/")
                            .maxAge(jwtProperties.getAccessTokenCookieMaxAge()).build();
                    response.addHeader(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString());
                });
    }
}
