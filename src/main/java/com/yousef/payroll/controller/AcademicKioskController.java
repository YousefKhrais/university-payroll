package com.yousef.payroll.controller;

import com.yousef.payroll.repositories.AcademicRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
