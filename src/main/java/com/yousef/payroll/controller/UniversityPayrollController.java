package com.yousef.payroll.controller;

import com.yousef.payroll.model.Academic;
import com.yousef.payroll.model.AcademicLeave;
import com.yousef.payroll.model.Payment;
import com.yousef.payroll.model.UserType;
import com.yousef.payroll.model.types.Gender;
import com.yousef.payroll.model.types.LeaveType;
import com.yousef.payroll.model.types.PaymentMethodType;
import com.yousef.payroll.repositories.AcademicRepository;
import com.yousef.payroll.repositories.LeavesRepository;
import com.yousef.payroll.repositories.PaymentRepository;
import com.yousef.payroll.repositories.PersonnelEmployeeRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/university-payroll")
public class UniversityPayrollController {

    private final PersonnelEmployeeRepository personnelEmployeeRepository;
    private final PaymentRepository paymentRepository;
    private final AcademicRepository academicRepository;
    private final LeavesRepository leavesRepository;

    public UniversityPayrollController(PersonnelEmployeeRepository personnelEmployeeRepository, PaymentRepository paymentRepository, AcademicRepository academicRepository, LeavesRepository leavesRepository) {
        this.personnelEmployeeRepository = personnelEmployeeRepository;
        this.paymentRepository = paymentRepository;
        this.academicRepository = academicRepository;
        this.leavesRepository = leavesRepository;
    }

    @GetMapping("/dashboard")
    public String adminGeneralView() {
        return "redirect:/university-payroll/dashboard/employees-list";
    }

    @GetMapping("/dashboard/employees-list")
    public String listAcademicsView(Model model) {
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
    public String deleteAcademic(RedirectAttributes redirectAttributes, Model model, @PathVariable long id) {
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

    @PostMapping("/dashboard/add-employee")
    public String addFullTimeAcademic(RedirectAttributes redirectAttributes, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("email") String email,
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

    @PostMapping("/dashboard/addPartTimeAcademic")
    public String addPartTimeAcademic(RedirectAttributes redirectAttributes, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                      @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("phoneNumber") String phoneNumber,
                                      @RequestParam("salary") double flatSalary, @RequestParam("address") String address, @RequestParam("department") String department) {
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
            academic.setType(UserType.CasualAcademics);

            System.out.println(academic);
            academicRepository.save(academic);
            redirectAttributes.addFlashAttribute("message", "Successfully added a new academic");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
        }

        return "redirect:/university-payroll/dashboard/employees-list";
    }

    @PostMapping("/dashboard/Academic/{id}/addAcademicLeave")
    public String addAcademicLeave(RedirectAttributes redirectAttributes, @PathVariable long id, @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate, @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate, @RequestParam("reason") String reason) {
        Academic academic = academicRepository.findById(id).orElse(null);

        if (academic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard/employees-list";
        }

        if (academic.getLeaveBalance() == 0) {
            redirectAttributes.addFlashAttribute("error", "Academic leave balance is 0.");
            return "redirect:/university-payroll/dashboard/employees/edit/" + id;
        }

        try {
            AcademicLeave academicLeave = new AcademicLeave();
            academicLeave.setFromDate(fromDate);
            academicLeave.setToDate(toDate);
            academicLeave.setReason(reason);
            academicLeave.setAcademic(academic);
            academicLeave.setType(LeaveType.SICK_LEAVE);

            System.out.println(academicLeave);
            leavesRepository.save(academicLeave);

            academic.setLeaveBalance(academic.getLeaveBalance() - 1);
            academicRepository.save(academic);

            redirectAttributes.addFlashAttribute("message", "Successfully added a new leave");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
        }

        return "redirect:/university-payroll/dashboard/employees/edit/" + id;
    }


    @PostMapping("/dashboard/Academic/{id}/addPayment")
    public String addPayment(RedirectAttributes redirectAttributes, @PathVariable long id, @RequestParam("paymentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date paymentDate,
                             @RequestParam("salary") double salary, @RequestParam("tax") double tax) {
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
}
