package com.example.leaverequest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    private String userLogin;
    
    private String adminLogin;
    
    private String userPassword;
    
    private String adminPassword;
    
    private String userRole;
    
    private String adminRole;
    
    @Value("${app.auth.user.role}")
    public void setUserRole(String userRole)
    {
        this.userRole = userRole;
    }
    
    @Value("${app.auth.user.login}")
    public void setUserLogin(String userLogin)
    {
        this.userLogin = userLogin;
    }
    
    @Value("${app.auth.user.password}")
    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }
    
    @Value("${app.auth.admin.role}")
    public void setAdminRole(String adminRole)
    {
        this.adminRole = adminRole;
    }
    
    @Value("${app.auth.admin.login}")
    public void setAdminLogin(String adminLogin)
    {
        this.adminLogin = adminLogin;
    }
    
    @Value("${app.auth.admin.password}")
    public void setAdminPassword(String adminPassword)
    {
        this.adminPassword = adminPassword;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/login")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                // We filter the api/login requests
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                // And filter other requests to check the presence of JWT in header
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(userLogin).password(userPassword).authorities(userRole)
                .and()
                .withUser(adminLogin).password(adminPassword).authorities(adminRole, userRole);
    }
}
