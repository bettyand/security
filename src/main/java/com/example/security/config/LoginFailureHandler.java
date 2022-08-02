package com.example.security.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import com.example.security.service.UserService;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        User user = userRepo.findByEmail(email);

        if (user != null) {
            if (user.isEnabled() && user.isAccountNonLocked()) {
                if (user.getFailedAttempts() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(user);
                } else {
                    userService.lock(user);
                    exception = new LockedException("Your account has been locked due to " + UserService.MAX_FAILED_ATTEMPTS + " failed attempts." 
                        + "It will be unlocked after 24 hours.");
                }
            } else if (!user.isAccountNonLocked()) {
                if (userService.unlockWhenTimeExpired(user)) {
                    exception = new LockedException("Your account has been unlocked. Please try to log in again.");
                }
            }
        }

        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
