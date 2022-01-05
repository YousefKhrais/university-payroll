package com.yousef.payroll.config;

import com.yousef.payroll.service.AcademicDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Order(2)
public class AcademicKioskSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AcademicDetailsService academicDetailsService;

    public AcademicKioskSecurityConfig(AcademicDetailsService academicDetailsService) {
        super();
        this.academicDetailsService = academicDetailsService;
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(academicDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        http
                .antMatcher("/academic-kiosk/**")
                .authorizeRequests()
                .anyRequest()
                .hasAuthority("ACADEMIC")

                .and()
                .formLogin()
                .loginPage("/academic-kiosk/login")
                .defaultSuccessUrl("/academic-kiosk/dashboard")
                .failureUrl("/academic-kiosk/login?error")
                .permitAll()

                .and()
                .exceptionHandling()
                .accessDeniedPage("/error/error403")

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/academic-kiosk/logout"))
                .logoutSuccessUrl("/academic-kiosk/login");
    }
}