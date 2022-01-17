package com.yousef.payroll.config;

import com.yousef.payroll.service.PersonnelEmployeeDetailsService;
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
@Order(1)
public class UniversityPayrollSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonnelEmployeeDetailsService personnelEmployeeDetailsService;

    public UniversityPayrollSecurityConfig(PersonnelEmployeeDetailsService personnelEmployeeDetailsService) {
        super();
        this.personnelEmployeeDetailsService = personnelEmployeeDetailsService;
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(personnelEmployeeDetailsService);
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
                .antMatcher("/university-payroll/**")
                .authorizeRequests()
                .antMatchers("/university-payroll/register", "/university-payroll/process_register")
                .permitAll()
                .anyRequest()
                .hasAuthority("ADMIN")

                .and()
                .formLogin()
                .loginPage("/university-payroll/login")
                .defaultSuccessUrl("/university-payroll/dashboard/employees-list")
                .failureUrl("/university-payroll/login?error")
                .permitAll()

                .and()
                .exceptionHandling()
                .accessDeniedPage("/error/error403")

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/university-payroll/logout"))
                .logoutSuccessUrl("/university-payroll/login");
    }
}