package com.yousef.payroll.controller;

import com.yousef.payroll.model.Academic;
import com.yousef.payroll.model.PersonnelEmployee;
import com.yousef.payroll.model.UserType;
import com.yousef.payroll.model.types.PaymentMethodType;
import com.yousef.payroll.repositories.AcademicRepository;
import com.yousef.payroll.repositories.PersonnelEmployeeRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/university-payroll")
public class UniversityPayrollController {

    private final PersonnelEmployeeRepository personnelEmployeeRepository;
    private final AcademicRepository academicRepository;

    public UniversityPayrollController(PersonnelEmployeeRepository personnelEmployeeRepository, AcademicRepository academicRepository) {
        this.personnelEmployeeRepository = personnelEmployeeRepository;
        this.academicRepository = academicRepository;
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

    @GetMapping("/dashboard/employees-list")
    public String listEmployees(Model model) {
        List<Academic> academicsList = academicRepository.findAll();

        long fullTimeAcademicsCount = academicsList.stream().filter(academic -> academic.getType().equals(UserType.FullTimeAcademics)).count();
        long partTimeAcademicsCount = academicsList.size() - fullTimeAcademicsCount;

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("academicsList", academicsList);
        attributes.put("totalAcademicsCount", academicsList.size());
        attributes.put("fullTimeAcademicsCount", fullTimeAcademicsCount);
        attributes.put("partTimeAcademicsCount", partTimeAcademicsCount);

        model.addAllAttributes(attributes);

        return "universityPayrollSystem/admin/employees/hr-emplist";
    }

    @GetMapping("/dashboard/employees/edit/{id}")
    public String editEmployeeView(RedirectAttributes redirectAttributes, Model model, @PathVariable long id) {
        Academic academic = academicRepository.findById(id).orElse(null);

        if (academic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard/employees-list";
        }

        model.addAttribute(academic);

        return "universityPayrollSystem/admin/employees/hr-empview";
    }

    @PostMapping("/dashboard/add-employee")
    public String addEmployee(RedirectAttributes redirectAttributes, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("email") String email,
                              @RequestParam("password") String password, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("leaveBalance") int leaveBalance,
                              @RequestParam("flatSalary") double flatSalary, @RequestParam("address") String address, @RequestParam("department") String department) {
        try {
            Academic academic = new Academic();

            academic.setFirstName(firstName);
            academic.setLastName(lastName);
            academic.setEmail(email);
            academic.setPhoneNumber(phoneNumber);
            academic.setLeaveBalance(leaveBalance);
            academic.setFlatSalary(flatSalary);
            academic.setAddress(address);
            academic.setDepartment(department);
            academic.setType(UserType.FullTimeAcademics);
            academic.setPaymentDetails(PaymentMethodType.BANK_DEPOSIT.toString());
            academic.setPassword(new BCryptPasswordEncoder().encode(password));

            System.out.println(academic);
            academicRepository.save(academic);
            redirectAttributes.addFlashAttribute("message", "Successfully added a new academic");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
        }

        return "redirect:/university-payroll/dashboard/employees-list";
    }
}

