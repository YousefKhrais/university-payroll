package com.yousef.payroll.controller;

import com.yousef.payroll.model.PersonnelEmployee;
import com.yousef.payroll.repositories.PersonnelEmployeeRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/university-payroll")
public class UniversityPayrollController {

    private final PersonnelEmployeeRepository personnelEmployeeRepository;

    public UniversityPayrollController(PersonnelEmployeeRepository personnelEmployeeRepository) {
        this.personnelEmployeeRepository = personnelEmployeeRepository;
    }

    @GetMapping("/login")
    public String login() {
        if (isAuthenticated()) {
            return "redirect:university-payroll/dashboard";
        }
        return "universityPayrollSystem/auth/login";
    }
    //*********************************************************************************

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new PersonnelEmployee());
        return "universityPayrollSystem/auth/register";
    }
    //*********************************************************************************

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "universityPayrollSystem/auth/forgot-password";
    }
    //*********************************************************************************

    @GetMapping("/reset-password")
    public String resetPassword(Model model) {
        return "universityPayrollSystem/auth/reset-password";
    }

    //*********************************************************************************

    @PostMapping("/process_register")
    public String processRegister(PersonnelEmployee user) {
        System.out.println(user.toString());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        personnelEmployeeRepository.save(user);

        return "universityPayrollSystem/auth/register-success";
    }

    //*********************************************************************************

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("/dashboard")
    public String adminGeneral() {
        return "universityPayrollSystem/admin/admin-general";
    }
}

