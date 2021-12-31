package com.yousef.payroll.service;

import com.yousef.payroll.model.Academic;
import com.yousef.payroll.model.Payment;
import com.yousef.payroll.repositories.AcademicRepository;
import com.yousef.payroll.repositories.LeavesRepository;
import com.yousef.payroll.repositories.PaymentRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PaymentsService {

    private static final Logger LOGGER = Logger.getLogger(PaymentsService.class.getName());

    private final PaymentRepository paymentRepository;
    private final AcademicRepository academicRepository;
    private final LeavesRepository leavesRepository;

    public PaymentsService(PaymentRepository paymentRepository, AcademicRepository academicRepository, LeavesRepository leavesRepository) {
        this.paymentRepository = paymentRepository;
        this.academicRepository = academicRepository;
        this.leavesRepository = leavesRepository;
    }

    //@Async
    //@Scheduled(fixedDelay = 5000)
    //@Scheduled(cron = "@monthly")
    public void payAcademicsSalaries() throws InterruptedException {
        List<Academic> academics = academicRepository.findAll();
        for (Academic currentAcademic : academics) {
            Payment payment = new Payment();
            payment.setAcademic(currentAcademic);
            payment.setSalary(currentAcademic.getFlatSalary());
            payment.setTax(currentAcademic.getFlatSalary() * 0.15);
            payment.setDate(new Date());

            paymentRepository.save(payment);
            LOGGER.info("Added a new payment for: " + currentAcademic.getEmail());
        }
    }
}
