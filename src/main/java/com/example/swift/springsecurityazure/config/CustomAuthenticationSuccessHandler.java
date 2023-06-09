package com.example.swift.springsecurityazure.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html><head><script>window.opener.location.href = 'https://lemon-desert-0097cd103.3.azurestaticapps.net/'; window.close();</script></head></html>");
        response.getWriter().flush();
    }
}
