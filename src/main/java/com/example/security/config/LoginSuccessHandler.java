package com.example.security.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.security.model.User;
import com.example.security.service.CustomUserDetails;
import com.example.security.service.UserService;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        String redirectURL = request.getContextPath();

        if (user.getFailedAttempts() > 0) {
            userService.resetFailedAttempts(user.getEmail());
        }

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
