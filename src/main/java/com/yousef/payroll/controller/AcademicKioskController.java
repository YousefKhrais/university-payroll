package com.yousef.payroll.controller;

import com.yousef.payroll.model.users.Academic;
import com.yousef.payroll.model.users.FullTimeAcademic;
import com.yousef.payroll.model.users.PartTimeAcademic;
import com.yousef.payroll.repositories.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AcademicKioskController {

    private final AcademicRepository academicRepository;
    private final FullTimeAcademicRepository fullTimeAcademicRepository;
    private final PartTimeAcademicRepository partTimeAcademicRepository;
    private final LeavesRepository leavesRepository;
    private final TimeCardRepository timeCardRepository;
    private final PaymentRepository paymentRepository;

    public AcademicKioskController(PaymentRepository paymentRepository, AcademicRepository academicRepository, FullTimeAcademicRepository fullTimeAcademicRepository, PartTimeAcademicRepository partTimeAcademicRepository, LeavesRepository leavesRepository, TimeCardRepository timeCardRepository) {
        this.paymentRepository = paymentRepository;
        this.academicRepository = academicRepository;
        this.fullTimeAcademicRepository = fullTimeAcademicRepository;
        this.partTimeAcademicRepository = partTimeAcademicRepository;
        this.leavesRepository = leavesRepository;
        this.timeCardRepository = timeCardRepository;
    }

    @GetMapping("/academic-kiosk/dashboard")
    public String dashboardGeneral(@AuthenticationPrincipal FullTimeAcademic fullTimeAcademic, @AuthenticationPrincipal PartTimeAcademic partTimeAcademic, Model model) {
        if (fullTimeAcademic != null) {
            System.out.println(fullTimeAcademic);

            model.addAttribute(fullTimeAcademic);
            return "academic-kiosk/fullTime/dashboard";
        } else if (partTimeAcademic != null) {
            System.out.println(partTimeAcademic);

            model.addAttribute(partTimeAcademic);
            return "academic-kiosk/partTime/dashboard";
        } else {
            return "error/error500";
        }
    }

    @PostMapping("/academic-kiosk/fullTime/{id}/edit")
    public String editFullTimeAcademic(RedirectAttributes redirectAttributes, FullTimeAcademic fullTimeAcademic, @PathVariable long id) {
        FullTimeAcademic temp = fullTimeAcademicRepository.findById(id).orElse(null);

        if (temp == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard";
        }

        if (!temp.getId().equals(fullTimeAcademic.getId())) {
            redirectAttributes.addFlashAttribute("error", "Academic id doesn't match");
            return "redirect:/university-payroll/dashboard";
        }

        Academic academic = fullTimeAcademic.getAcademic();
        temp.getAcademic().setEmail(academic.getEmail());
        temp.getAcademic().setFirstName(academic.getFirstName());
        temp.getAcademic().setLastName(academic.getLastName());
        temp.getAcademic().setDepartment(academic.getDepartment());
        temp.getAcademic().setPhoneNumber(academic.getPhoneNumber());
        temp.getAcademic().setAddress(academic.getAddress());
        temp.getAcademic().setGender(academic.getGender());
        temp.getAcademic().setJobTitle(academic.getJobTitle());
        temp.getAcademic().setPaymentMethodType(academic.getPaymentMethodType());
        temp.getAcademic().setBirthDate(academic.getBirthDate());
        temp.getAcademic().setFlatSalary(academic.getFlatSalary());
        temp.setLeaveBalance(fullTimeAcademic.getLeaveBalance());

        fullTimeAcademicRepository.save(temp);

        redirectAttributes.addFlashAttribute("message", "Academic has been updated.");
        return "redirect:/university-payroll/dashboard";
    }

    @PostMapping("/academic-kiosk/partTime/{id}/edit")
    public String editPartTimeAcademic(RedirectAttributes redirectAttributes, PartTimeAcademic partTimeAcademic, @PathVariable long id) {
        PartTimeAcademic temp = partTimeAcademicRepository.findById(id).orElse(null);

        if (temp == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard";
        }

        if (!temp.getId().equals(partTimeAcademic.getId())) {
            redirectAttributes.addFlashAttribute("error", "Academic id doesn't match");
            return "redirect:/university-payroll/dashboard";
        }

        Academic academic = partTimeAcademic.getAcademic();
        temp.getAcademic().setEmail(academic.getEmail());
        temp.getAcademic().setFirstName(academic.getFirstName());
        temp.getAcademic().setLastName(academic.getLastName());
        temp.getAcademic().setDepartment(academic.getDepartment());
        temp.getAcademic().setPhoneNumber(academic.getPhoneNumber());
        temp.getAcademic().setAddress(academic.getAddress());
        temp.getAcademic().setGender(academic.getGender());
        temp.getAcademic().setJobTitle(academic.getJobTitle());
        temp.getAcademic().setPaymentMethodType(academic.getPaymentMethodType());
        temp.getAcademic().setBirthDate(academic.getBirthDate());
        temp.getAcademic().setFlatSalary(academic.getFlatSalary());

        partTimeAcademicRepository.save(temp);

        redirectAttributes.addFlashAttribute("message", "Academic has been updated.");
        return "redirect:/university-payroll/dashboard";
    }
}
