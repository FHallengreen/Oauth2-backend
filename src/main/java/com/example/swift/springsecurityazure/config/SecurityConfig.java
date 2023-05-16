package com.example.swift.springsecurityazure.config;

import com.example.swift.springsecurityazure.config.CustomAuthenticationSuccessHandler;
import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> {
                    auth.requestMatchers(req -> req.getRequestURI().equals("/") || req.getRequestURI().equals("/auth-status") || req.getRequestURI().startsWith("/oauth2/authorization/")).permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginProcessingUrl("/login/oauth2/code/*") // Add this line
//                        .defaultSuccessUrl("http://127.0.0.1:5500", true)
//                        .defaultSuccessUrl("http://127.0.0.1:5500", true)
                        .successHandler(new CustomAuthenticationSuccessHandler())) // Use the custom success handler
                .logout(logout -> logout
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // Remove the cookie when logging out
                            Cookie cookie = new Cookie("AUTHORIZATION", null);
                            cookie.setPath("/");
                            cookie.setMaxAge(0);
                            cookie.setHttpOnly(true);
                            cookie.setSecure(true);
                            response.addCookie(cookie);
                            response.setStatus(HttpStatus.OK.value());
                        }))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(request -> "/logout".equals(request.getRequestURI()))) // Disable CSRF protection for /logout endpoint
                .cors(cors -> cors
                        .configurationSource(request -> {
                            var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                            corsConfiguration.setAllowedOrigins(List.of("https://lemon-desert-0097cd103.3.azurestaticapps.net"));
                            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
                            corsConfiguration.setAllowCredentials(true);
                            corsConfiguration.setExposedHeaders(List.of("Set-Cookie"));

                            return corsConfiguration;
                        })) // Enable CORS
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .expiredUrl("/"))
                .build();
    }
}


