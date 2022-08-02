package com.example.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.security.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/users").hasAnyAuthority("Admin", "Coach")
            .antMatchers("/admin_home", "/users/**").hasAuthority("Admin")
            .antMatchers("/coach_home").hasAuthority("Coach") 
            .antMatchers("/user_home").hasAuthority("User")        
            .antMatchers(
                "/",
                "/register", 
                "/process_register", 
                "/login", 
                "/webjars/**"
                ).permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
                .usernameParameter("email")
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler)
                .permitAll()
            .and()
            .rememberMe()
            .and()
            .logout().logoutSuccessUrl("/login?logout").permitAll()
            .and()
            .exceptionHandling().accessDeniedPage("/403");
    }
}
