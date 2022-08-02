package com.example.security.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.security.service.CustomUserDetails;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String redirectURL = request.getContextPath();

        if (userDetails.hasRole("Admin")) {
            redirectURL = "admin_home";
        } else if (userDetails.hasRole("Coach")) {
            redirectURL = "coach_home";
        } else if (userDetails.hasRole("User")) {
            redirectURL = "user_home";
        }

        response.sendRedirect(redirectURL);
    }
}
