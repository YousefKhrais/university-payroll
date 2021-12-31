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

    @GetMapping("/dashboard")
    public String adminGeneral() {
        return "AcademicKiosk/employees/employee-leaves";
    }
}
