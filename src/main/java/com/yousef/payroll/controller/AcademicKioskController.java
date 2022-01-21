package com.yousef.payroll.controller;

import com.yousef.payroll.model.AcademicLeave;
import com.yousef.payroll.model.TimeCard;
import com.yousef.payroll.model.types.LeaveType;
import com.yousef.payroll.model.users.Academic;
import com.yousef.payroll.model.users.FullTimeAcademic;
import com.yousef.payroll.model.users.PartTimeAcademic;
import com.yousef.payroll.repositories.*;
import com.yousef.payroll.service.PaymentsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public String dashboardView(@AuthenticationPrincipal FullTimeAcademic fullTimeAcademic, @AuthenticationPrincipal PartTimeAcademic partTimeAcademic, Model model) {
        if (fullTimeAcademic != null) {
            List<AcademicLeave> academicLeaves = leavesRepository.findByFullTimeAcademicId(fullTimeAcademic.getId());

            model.addAttribute("fullTimeAcademic", fullTimeAcademic);
            model.addAttribute("academicLeaves", academicLeaves);

            return "academic-kiosk/fullTime/dashboard";
        } else if (partTimeAcademic != null) {
            List<TimeCard> timeCards = timeCardRepository.findByPartTimeAcademicId(partTimeAcademic.getId());

            model.addAttribute("partTimeAcademic", partTimeAcademic);
            model.addAttribute("timeCards", timeCards);

            return "academic-kiosk/partTime/dashboard";
        } else {
            return "error/error500";
        }
    }

    @PostMapping("/academic-kiosk/fullTime/{id}/edit")
    public String editFullTimeAcademic(@AuthenticationPrincipal FullTimeAcademic currentAcademic, RedirectAttributes redirectAttributes, FullTimeAcademic fullTimeAcademic, @PathVariable long id) {
        if (currentAcademic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/academic-kiosk/dashboard";
        }

        if (!currentAcademic.getId().equals(fullTimeAcademic.getId())) {
            redirectAttributes.addFlashAttribute("error", "Academic id doesn't match");
            return "redirect:/academic-kiosk/dashboard";
        }

        Academic academic = fullTimeAcademic.getAcademic();
        currentAcademic.getAcademic().setEmail(academic.getEmail());
        currentAcademic.getAcademic().setFirstName(academic.getFirstName());
        currentAcademic.getAcademic().setLastName(academic.getLastName());
        currentAcademic.getAcademic().setPhoneNumber(academic.getPhoneNumber());
        currentAcademic.getAcademic().setAddress(academic.getAddress());
        currentAcademic.getAcademic().setBirthDate(academic.getBirthDate());
        currentAcademic.getAcademic().setSendEmailNotification(academic.isSendEmailNotification());
        currentAcademic.getAcademic().setPaymentMethodType(academic.getPaymentMethodType());

        fullTimeAcademicRepository.save(currentAcademic);

        redirectAttributes.addFlashAttribute("message", "Academic has been updated.");
        return "redirect:/academic-kiosk/dashboard";
    }

    @PostMapping("/academic-kiosk/partTime/{id}/edit")
    public String editPartTimeAcademic(@AuthenticationPrincipal PartTimeAcademic currentAcademic, RedirectAttributes redirectAttributes, PartTimeAcademic partTimeAcademic, @PathVariable long id) {
        if (currentAcademic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/academic-kiosk/dashboard";
        }

        if (!currentAcademic.getId().equals(partTimeAcademic.getId())) {
            redirectAttributes.addFlashAttribute("error", "Academic id doesn't match");
            return "redirect:/academic-kiosk/dashboard";
        }

        Academic academic = partTimeAcademic.getAcademic();
        currentAcademic.getAcademic().setEmail(academic.getEmail());
        currentAcademic.getAcademic().setFirstName(academic.getFirstName());
        currentAcademic.getAcademic().setLastName(academic.getLastName());
        currentAcademic.getAcademic().setPhoneNumber(academic.getPhoneNumber());
        currentAcademic.getAcademic().setAddress(academic.getAddress());
        currentAcademic.getAcademic().setBirthDate(academic.getBirthDate());
        currentAcademic.getAcademic().setSendEmailNotification(academic.isSendEmailNotification());
        currentAcademic.getAcademic().setPaymentMethodType(academic.getPaymentMethodType());

        partTimeAcademicRepository.save(currentAcademic);

        redirectAttributes.addFlashAttribute("message", "Academic has been updated.");
        return "redirect:/academic-kiosk/dashboard";
    }

    @PostMapping("/academic-kiosk/addAcademicLeave")
    public String addAcademicLeave(@AuthenticationPrincipal FullTimeAcademic fullTimeAcademic, RedirectAttributes redirectAttributes, @RequestParam("reason") String reason, @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate, @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        if (fullTimeAcademic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/academic-kiosk/dashboard";
        }

        int totalDays = (int) TimeUnit.MILLISECONDS.toDays(toDate.getTime() - fromDate.getTime()) + 1;

        if (totalDays <= 0) {
            redirectAttributes.addFlashAttribute("error", "Error: 'To Date' can't be less than 'From Date'");
            return "redirect:/academic-kiosk/dashboard/";
        }

        if (fullTimeAcademic.getRemainingLeaveBalance() < totalDays) {
            redirectAttributes.addFlashAttribute("error", "Academic leave balance is insufficient");
            return "redirect:/academic-kiosk/dashboard/";
        }

        try {
            AcademicLeave academicLeave = new AcademicLeave();
            academicLeave.setFromDate(fromDate);
            academicLeave.setToDate(toDate);
            academicLeave.setDays(totalDays);
            academicLeave.setReason(reason);
            academicLeave.setType(LeaveType.SICK_LEAVE);
            academicLeave.setAcademic(fullTimeAcademic);

            System.out.println(academicLeave);
            leavesRepository.save(academicLeave);

            fullTimeAcademic.setRemainingLeaveBalance(fullTimeAcademic.getRemainingLeaveBalance() - totalDays);
            fullTimeAcademicRepository.save(fullTimeAcademic);

            redirectAttributes.addFlashAttribute("message", "Successfully added a new academic leave");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
        }

        return "redirect:/academic-kiosk/dashboard/";
    }

    @PostMapping("/academic-kiosk/addTimeCard")
    public String addTimeCard(@AuthenticationPrincipal PartTimeAcademic currentAcademic,RedirectAttributes redirectAttributes, @RequestParam("hoursCount") Integer hoursCount, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        if (currentAcademic == null) {
            redirectAttributes.addFlashAttribute("error", "Academic doesn't exist");
            return "redirect:/university-payroll/dashboard";
        }

        int currentMonthCount = 0;
        int totalWorkedHours = 0;
        List<TimeCard> timeCards = timeCardRepository.findByPartTimeAcademicId(currentAcademic.getId());
        for (TimeCard timeCard : timeCards) {
            if (PaymentsService.isCurrentMonth(timeCard.getDate())) {
                totalWorkedHours += timeCard.getHoursCount();
                currentMonthCount++;
            }
        }

        if (currentMonthCount == 4) {
            redirectAttributes.addFlashAttribute("error", "Monthly Time Cards limit has been reached (4 Per Month)");
            return "redirect:/academic-kiosk/dashboard/";
        }

        if (totalWorkedHours + hoursCount > currentAcademic.getContractHours()) {
            hoursCount = currentAcademic.getContractHours() - totalWorkedHours;
            if (hoursCount == 0) {
                redirectAttributes.addFlashAttribute("error", "Max worked hours was reached - Time Card was not added");
                return "redirect:/academic-kiosk/dashboard/";
            }
            redirectAttributes.addFlashAttribute("error", "Time Card hours > Contract Hours - Your hours will be capped at " + currentAcademic.getContractHours());
        }

        try {
            TimeCard timeCard = new TimeCard();
            timeCard.setHoursCount(hoursCount);
            timeCard.setDate(date);
            timeCard.setAcademic(currentAcademic);

            System.out.println(timeCard);
            timeCardRepository.save(timeCard);

            redirectAttributes.addFlashAttribute("message", "Successfully added a new time card");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "ERROR: " + e.getMessage());
        }

        return "redirect:/academic-kiosk/dashboard/";
    }

    @ExceptionHandler(Exception.class)
    private RedirectView handleMyException(Exception ex, HttpServletRequest request) {
        RedirectView redirectView = new RedirectView("/academic-kiosk/dashboard");
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
