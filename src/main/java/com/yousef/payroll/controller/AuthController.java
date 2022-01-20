package com.yousef.payroll.controller;

import com.yousef.payroll.model.users.PersonnelEmployee;
import com.yousef.payroll.repositories.PersonnelEmployeeRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final PersonnelEmployeeRepository personnelEmployeeRepository;

    public AuthController(PersonnelEmployeeRepository personnelEmployeeRepository) {
        this.personnelEmployeeRepository = personnelEmployeeRepository;
    }

    @GetMapping("/academic-kiosk/login")
    public String academicKioskLoginView() {
        if (isAuthenticated())
            return "redirect:academic-kiosk/dashboard";
        return "academic-kiosk/auth/login";
    }

    @GetMapping("/university-payroll/login")
    public String universityPayrollLoginView() {
        if (isAuthenticated())
            return "redirect:dashboard/employees-list";
        return "universityPayrollSystem/auth/login";
    }

    @GetMapping("/university-payroll/register")
    public String registerView(Model model) {
        model.addAttribute("user", new PersonnelEmployee());
        return "universityPayrollSystem/auth/register";
    }

    @PostMapping("/university-payroll/process_register")
    public String processRegister(PersonnelEmployee user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        personnelEmployeeRepository.save(user);

        return "universityPayrollSystem/auth/register-success";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass()))
            return false;
        return authentication.isAuthenticated();
    }
}
