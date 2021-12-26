package com.yousef.payroll.controller;

import com.yousef.payroll.model.Academic;
import com.yousef.payroll.model.UserType;
import com.yousef.payroll.repositories.AcademicRepository;
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
@RequestMapping("/academic-kiosk")
public class AcademicKioskController {

    private final AcademicRepository academicRepository;

    public AcademicKioskController(AcademicRepository academicRepository) {
        this.academicRepository = academicRepository;
    }

    @GetMapping("/login")
    public String login() {
        if (isAuthenticated()) {
            return "redirect:AcademicKiosk/dashboard";
        }
        return "AcademicKiosk/auth/login";
    }
    //*********************************************************************************

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new Academic());
        return "AcademicKiosk/auth/register";
    }
    //*********************************************************************************

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "AcademicKiosk/auth/forgot-password";
    }
    //*********************************************************************************

    @GetMapping("/reset-password")
    public String resetPassword(Model model) {
        return "AcademicKiosk/auth/reset-password";
    }

    //*********************************************************************************

    @PostMapping("/process_register")
    public String processRegister(Academic user) {
        System.out.println(user.toString());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user.setType(UserType.CasualAcademics);
        academicRepository.save(user);

        return "AcademicKiosk/auth/register-success";
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
        return "AcademicKiosk/employees/employee-leaves";
    }
}
