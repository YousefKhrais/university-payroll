package com.yousef.payroll.controller;

import com.yousef.payroll.model.Academic;
import com.yousef.payroll.model.AcademicLeave;
import com.yousef.payroll.model.Payment;
import com.yousef.payroll.model.TimeCard;
import com.yousef.payroll.model.types.AcademicType;
import com.yousef.payroll.model.types.Gender;
import com.yousef.payroll.model.types.LeaveType;
import com.yousef.payroll.model.types.PaymentMethodType;
import com.yousef.payroll.repositories.*;
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
    private final LeavesRepository leavesRepository;
    private final TimeCardRepository timeCardRepository;

    public UniversityPayrollController(PersonnelEmployeeRepository personnelEmployeeRepository, PaymentRepository paymentRepository, AcademicRepository academicRepository, LeavesRepository leavesRepository, TimeCardRepository timeCardRepository) {
        this.personnelEmployeeRepository = personnelEmployeeRepository;
        this.paymentRepository = paymentRepository;
        this.academicRepository = academicRepository;
        this.leavesRepository = leavesRepository;
        this.timeCardRepository = timeCardRepository;
    }

    @GetMapping("/dashboard")
    public String adminGeneralView() {
        return "redirect:/university-payroll/dashboard/employees-list";
    }

    @GetMapping("/dashboard/employees-list")
    public String listAcademicsView(Model model, Academic fullTimeAcademic) {
        List<Academic> academicsList = academicRepository.findAll();

        long fullTimeAcademicsCount = academicsList.stream().filter(academic -> academic.getType().equals(AcademicType.FULL_TIME_ACADEMIC)).count();
        long partTimeAcademicsCount = academicsList.size() - fullTimeAcademicsCount;

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("academicsList", academicsList);
        attributes.put("totalAcademicsCount", academicsList.size());
        attributes.put("fullTimeAcademicsCount", fullTimeAcademicsCount);
        attributes.put("partTimeAcademicsCount", partTimeAcademicsCount);

        //temp
        attributes.put("fullTimeAcademic", fullTimeAcademic);

        model.addAllAttributes(attributes);

        return "universityPayrollSystem/admin/employees/hr-emplist";
    }

    @PostMapping("/dashboard/add-employee")
    public String addFullTimeAcademic(@Valid Academic fullTimeAcademic, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            for (int i = 0; i < bindingResult.getFieldErrors().size(); i++) {
                errorMessage.append("[").append(i).append("] ").append(bindingResult.getFieldErrors().get(i).getDefaultMessage()).append(" | ");
            }

            redirectAttributes.addFlashAttribute("error", "ERROR: " + errorMessage);
            return "redirect:/university-payroll/dashboard/employees-list";
        }

        try {
            fullTimeAcademic.setType(AcademicType.FULL_TIME_ACADEMIC);
            fullTimeAcademic.setPaymentDetails(PaymentMethodType.BANK_DEPOSIT.toString());
            fullTimeAcademic.setPassword(new BCryptPasswordEncoder().encode(fullTimeAcademic.getPassword()));

            System.out.println(fullTimeAcademic);
            academicRepository.save(fullTimeAcademic);
            redirectAttributes.addFlashAttribute("message", "Successfully added a new academic");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
        }

        return "redirect:/university-payroll/dashboard/employees-list";
    }

    @PostMapping("/dashboard/addPartTimeAcademic")
    public String addPartTimeAcademic(RedirectAttributes redirectAttributes, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                      @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("phoneNumber") String phoneNumber,
                                      @RequestParam("salary") double flatSalary, @RequestParam("address") String address,
                                      @RequestParam("department") String department) {
        try {
            Academic academic = new Academic();

            academic.setFirstName(firstName);
            academic.setLastName(lastName);
            academic.setEmail(email);
            academic.setPassword(new BCryptPasswordEncoder().encode(password));
            academic.setProfilePicLink("big no no yes");//todo:edit
            academic.setDepartment(department);
            academic.setJobTitle("yoooooooo");//todo:edit
            academic.setPhoneNumber(phoneNumber);
            academic.setAddress(address);
            academic.setPaymentDetails(PaymentMethodType.BANK_DEPOSIT.toString());
            academic.setFlatSalary(flatSalary);            //TODO: Change it to an hourly salary...++++++++++++++
            academic.setLeaveBalance(0);
            academic.setSendEmailNotification(true);
            academic.setActive(true);
            academic.setGender(Gender.FEMALE);//todo:edit
            academic.setBirthDate(new Date());//todo:edit
            academic.setType(AcademicType.PART_TIME_ACADEMIC);

            System.out.println(academic);
            academicRepository.save(academic);
            redirectAttributes.addFlashAttribute("message", "Successfully added a new academic");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
        }

        return "redirect:/university-payroll/dashboard/employees-list";
    }

    @GetMapping("/dashboard/employees/edit/{id}")
    public String editAcademicView(RedirectAttributes redirectAttributes, Model model, @PathVariable long id) {
        Academic academic = academicRepository.findById(id).orElse(null);

        if (academic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard/employees-list";
        }

        model.addAttribute(academic);

        return "universityPayrollSystem/admin/employees/hr-empview";
    }

    @GetMapping("/dashboard/employees/delete/{id}")
    public String deleteAcademic(RedirectAttributes redirectAttributes, @PathVariable long id) {
        Academic academic = academicRepository.findById(id).orElse(null);

        if (academic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard/employees-list";
        }

        try {
            academicRepository.delete(academic);
            redirectAttributes.addFlashAttribute("message", "Academic has been deleted.");

            return "redirect:/university-payroll/dashboard/employees-list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/university-payroll/dashboard/employees-list";
        }
    }

    @PostMapping("/dashboard/Academic/{id}/addAcademicLeave")
    public String addAcademicLeave(RedirectAttributes redirectAttributes, @PathVariable long id, @RequestParam("reason") String reason,
                                   @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                   @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        Academic academic = academicRepository.findById(id).orElse(null);

        if (academic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard/employees-list";
        }

        int totalDays = (int) TimeUnit.MILLISECONDS.toDays(toDate.getTime() - fromDate.getTime()) + 1;

        if (totalDays <= 0) {
            redirectAttributes.addFlashAttribute("error", "Error: 'To Date' can't be less than 'From Date'");
            return "redirect:/university-payroll/dashboard/employees/edit/" + id;
        }

        if (academic.getLeaveBalance() < totalDays) {
            redirectAttributes.addFlashAttribute("error", "Academic leave balance is insufficient");
            return "redirect:/university-payroll/dashboard/employees/edit/" + id;
        }

        try {
            AcademicLeave academicLeave = new AcademicLeave();
            academicLeave.setFromDate(fromDate);
            academicLeave.setToDate(toDate);
            academicLeave.setDays(totalDays);
            academicLeave.setReason(reason);
            academicLeave.setType(LeaveType.SICK_LEAVE);
            academicLeave.setAcademic(academic);

            System.out.println(academicLeave);
            leavesRepository.save(academicLeave);

            academic.setLeaveBalance(academic.getLeaveBalance() - totalDays);
            academicRepository.save(academic);

            redirectAttributes.addFlashAttribute("message", "Successfully added a new academic leave");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
        }

        return "redirect:/university-payroll/dashboard/employees/edit/" + id;
    }

    @PostMapping("/dashboard/Academic/{id}/addPayment")
    public String addPayment(RedirectAttributes redirectAttributes, @PathVariable long id, @RequestParam("salary") double salary,
                             @RequestParam("tax") double tax, @RequestParam("paymentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date paymentDate) {
        Academic academic = academicRepository.findById(id).orElse(null);

        if (academic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard/employees-list";
        }

        try {
            Payment payment = new Payment();
            payment.setSalary(salary);
            payment.setTax(tax);
            payment.setAcademic(academic);
            payment.setDate(paymentDate);

            System.out.println(payment);
            paymentRepository.save(payment);

            redirectAttributes.addFlashAttribute("message", "Successfully added a new payment");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
        }

        return "redirect:/university-payroll/dashboard/employees/edit/" + id;
    }

    @PostMapping("/dashboard/Academic/{id}/addTimeCard")
    public String addTimeCard(RedirectAttributes redirectAttributes, @PathVariable long id, @RequestParam("hoursCount") Integer hoursCount,
                              @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        Academic academic = academicRepository.findById(id).orElse(null);

        if (academic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard/employees-list";
        }

        try {
            TimeCard timeCard = new TimeCard();
            timeCard.setHoursCount(hoursCount);
            timeCard.setDate(date);
            timeCard.setAcademic(academic);

            System.out.println(timeCard);
            timeCardRepository.save(timeCard);

            redirectAttributes.addFlashAttribute("message", "Successfully added a new time card");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
        }

        return "redirect:/university-payroll/dashboard/employees/edit/" + id;
    }

    @ExceptionHandler(Exception.class)
    private RedirectView handleMyException(Exception ex, HttpServletRequest request) {
        RedirectView redirectView = new RedirectView("/university-payroll/dashboard/employees-list");
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