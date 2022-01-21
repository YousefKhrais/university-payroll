package com.yousef.payroll.controller;

import com.yousef.payroll.model.AcademicLeave;
import com.yousef.payroll.model.TimeCard;
import com.yousef.payroll.model.types.AcademicType;
import com.yousef.payroll.model.types.LeaveType;
import com.yousef.payroll.model.types.PaymentMethodType;
import com.yousef.payroll.model.users.Academic;
import com.yousef.payroll.model.users.FullTimeAcademic;
import com.yousef.payroll.model.users.PartTimeAcademic;
import com.yousef.payroll.repositories.*;
import com.yousef.payroll.service.PaymentsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/university-payroll")
public class UniversityPayrollController {

    private final PersonnelEmployeeRepository personnelEmployeeRepository;
    private final PaymentRepository paymentRepository;
    private final AcademicRepository academicRepository;
    private final FullTimeAcademicRepository fullTimeAcademicRepository;
    private final PartTimeAcademicRepository partTimeAcademicRepository;
    private final LeavesRepository leavesRepository;
    private final TimeCardRepository timeCardRepository;

    public UniversityPayrollController(PersonnelEmployeeRepository personnelEmployeeRepository, PaymentRepository paymentRepository, AcademicRepository academicRepository, FullTimeAcademicRepository fullTimeAcademicRepository, PartTimeAcademicRepository partTimeAcademicRepository, LeavesRepository leavesRepository, TimeCardRepository timeCardRepository) {
        this.personnelEmployeeRepository = personnelEmployeeRepository;
        this.paymentRepository = paymentRepository;
        this.academicRepository = academicRepository;
        this.fullTimeAcademicRepository = fullTimeAcademicRepository;
        this.partTimeAcademicRepository = partTimeAcademicRepository;
        this.leavesRepository = leavesRepository;
        this.timeCardRepository = timeCardRepository;
    }

    @GetMapping("/dashboard")
    public String listAcademicsView(Model model) {
        List<FullTimeAcademic> fullTimeAcademicsList = fullTimeAcademicRepository.findAll();
        List<PartTimeAcademic> partTimeAcademicsList = partTimeAcademicRepository.findAll();

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("fullTimeAcademicsList", fullTimeAcademicsList);
        attributes.put("partTimeAcademicsList", partTimeAcademicsList);
        attributes.put("totalAcademicsCount", fullTimeAcademicsList.size() + partTimeAcademicsList.size());
        attributes.put("fullTimeAcademicsCount", fullTimeAcademicsList.size());
        attributes.put("partTimeAcademicsCount", partTimeAcademicsList.size());

        Academic tempAcademic = new Academic();
        FullTimeAcademic tempFullTimeAcademic = new FullTimeAcademic();
        tempFullTimeAcademic.setAcademic(tempAcademic);

        PartTimeAcademic tempPartTimeAcademic = new PartTimeAcademic();
        tempPartTimeAcademic.setAcademic(tempAcademic);

        attributes.put("academic", tempAcademic);
        attributes.put("fullTimeAcademic", tempFullTimeAcademic);
        attributes.put("partTimeAcademic", tempPartTimeAcademic);

        model.addAllAttributes(attributes);
        return "universityPayrollSystem/admin/employees/hr-academic-list";
    }

    @GetMapping("/dashboard/academics")
    public String adminGeneralView() {
        return "redirect:/university-payroll/dashboard";
    }

    @PostMapping("/dashboard/academic/fullTime")
    public String addFullTimeAcademic(@Valid FullTimeAcademic fullTimeAcademic, Academic academic, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                System.out.println(objectError.toString());
            });
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult.getFieldErrors());
            return "redirect:/university-payroll/dashboard";
        }

        try {
            academic.setType(AcademicType.FULL_TIME_ACADEMIC);
            academic.setPaymentDetails(PaymentMethodType.BANK_DEPOSIT.toString());
            academic.setPassword(new BCryptPasswordEncoder().encode(academic.getPassword()));

            academicRepository.save(academic);

            fullTimeAcademic.setAcademic(academic);
            fullTimeAcademic.setRemainingLeaveBalance(fullTimeAcademic.getLeaveBalance());
            fullTimeAcademicRepository.save(fullTimeAcademic);

            redirectAttributes.addFlashAttribute("message", "Successfully added a new academic");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/university-payroll/dashboard";
    }

    @PostMapping("/dashboard/academic/partTime")
    public String addPartTimeAcademic(@Valid PartTimeAcademic partTimeAcademic, Academic academic, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                System.out.println(objectError.toString());
            });

            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult.getFieldErrors());
            return "redirect:/university-payroll/dashboard";
        }

        try {
            academic.setType(AcademicType.PART_TIME_ACADEMIC);
            academic.setPaymentDetails(PaymentMethodType.BANK_DEPOSIT.toString());
            academic.setPassword(new BCryptPasswordEncoder().encode(academic.getPassword()));

            partTimeAcademic.setAcademic(academic);

            academicRepository.save(academic);
            partTimeAcademicRepository.save(partTimeAcademic);

            redirectAttributes.addFlashAttribute("message", "Successfully added a new academic");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
        }

        return "redirect:/university-payroll/dashboard";
    }

    @GetMapping("/dashboard/academic/delete/{id}")
    public String deleteAcademic(RedirectAttributes redirectAttributes, @PathVariable long id) {
        Academic academic = academicRepository.findById(id).orElse(null);

        if (academic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard";
        }

        try {
            if (academic.getType().equals(AcademicType.FULL_TIME_ACADEMIC)) {
                FullTimeAcademic fullTimeAcademic = fullTimeAcademicRepository.findByAcademicId(id);
                fullTimeAcademicRepository.delete(fullTimeAcademic);
            } else if (academic.getType().equals(AcademicType.PART_TIME_ACADEMIC)) {
                PartTimeAcademic partTimeAcademic = partTimeAcademicRepository.findByAcademicId(id);
                partTimeAcademicRepository.delete(partTimeAcademic);
            }

            academicRepository.delete(academic);
            redirectAttributes.addFlashAttribute("message", "Academic has been deleted.");

            return "redirect:/university-payroll/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/university-payroll/dashboard";
        }
    }

    @GetMapping("/dashboard/academic/edit/{id}")
    public String editAcademicView(RedirectAttributes redirectAttributes, Model model, @PathVariable long id) {
        Academic academic = academicRepository.findById(id).orElse(null);

        if (academic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard";
        }

        if (academic.getType().equals(AcademicType.FULL_TIME_ACADEMIC)) {
            FullTimeAcademic fullTimeAcademic = fullTimeAcademicRepository.findByAcademicId(id);

            model.addAttribute(fullTimeAcademic);
            return "universityPayrollSystem/admin/employees/hr-fulltime-academic-edit";
        } else if (academic.getType().equals(AcademicType.PART_TIME_ACADEMIC)) {
            PartTimeAcademic partTimeAcademic = partTimeAcademicRepository.findByAcademicId(id);

            model.addAttribute(partTimeAcademic);
            return "universityPayrollSystem/admin/employees/hr-parttime-academic-edit";
        } else {
            return "error/error500";
        }
    }

    @PostMapping("/dashboard/academic/fullTime/{id}/edit")
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

    @PostMapping("/dashboard/academic/partTime/{id}/edit")
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

    @ExceptionHandler(Exception.class)
    private RedirectView handleMyException(Exception ex, HttpServletRequest request) {
        RedirectView redirectView = new RedirectView("/university-payroll/dashboard");
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);

        FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);

        if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException e = (MethodArgumentTypeMismatchException) ex;
            System.out.println(e.getName() + " " + e.getParameter());
            outputFlashMap.put("error", "Error: " + e.getName() + " is missing or empty");
        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            outputFlashMap.put("error", "Error: " + e.getParameter() + " is missing or empty");
        } else {
            outputFlashMap.put("error", "Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        return redirectView;
    }
}