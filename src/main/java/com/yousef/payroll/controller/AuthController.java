package com.yousef.payroll.controller;

import com.yousef.payroll.model.PersonnelEmployee;
import com.yousef.payroll.repositories.AcademicRepository;
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

    private final AcademicRepository academicRepository;
    private final PersonnelEmployeeRepository personnelEmployeeRepository;

    public AuthController(AcademicRepository academicRepository, PersonnelEmployeeRepository personnelEmployeeRepository) {
        this.academicRepository = academicRepository;
        this.personnelEmployeeRepository = personnelEmployeeRepository;
    }

    //*********************************************************************************
    //*********************************************************************************
    //*********************************************************************************

    @GetMapping("/academic-kiosk/login")
    public String login() {
        if (isAuthenticated()) {
            return "redirect:AcademicKiosk/dashboard";
        }
        return "AcademicKiosk/auth/login";
    }

    @GetMapping("/academic-kiosk/forgot-password")
    public String forgotPassword(Model model) {
        return "AcademicKiosk/auth/forgot-password";
    }

    @GetMapping("/academic-kiosk/reset-password")
    public String resetPassword(Model model) {
        return "AcademicKiosk/auth/reset-password";
    }

    //*********************************************************************************
    //*********************************************************************************
    //*********************************************************************************

    @GetMapping("/university-payroll/login")
    public String loginView() {
        if (isAuthenticated()) {
            return "redirect:dashboard/employees-list";
        }
        return "universityPayrollSystem/auth/login";
    }

    @GetMapping("/university-payroll/register")
    public String registerView(Model model) {
        model.addAttribute("user", new PersonnelEmployee());
        return "universityPayrollSystem/auth/register";
    }

    @PostMapping("/university-payroll/process_register")
    public String processRegister(PersonnelEmployee user) {
        System.out.println(user.toString());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        personnelEmployeeRepository.save(user);

        return "universityPayrollSystem/auth/register-success";
    }

    //*********************************************************************************
    //*********************************************************************************
    //*********************************************************************************

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}
