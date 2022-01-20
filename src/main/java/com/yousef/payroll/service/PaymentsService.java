package com.yousef.payroll.service;

import com.yousef.payroll.model.Payment;
import com.yousef.payroll.model.TimeCard;
import com.yousef.payroll.model.users.FullTimeAcademic;
import com.yousef.payroll.model.users.PartTimeAcademic;
import com.yousef.payroll.repositories.*;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PaymentsService {

    private final AcademicRepository academicRepository;
    private final FullTimeAcademicRepository fullTimeAcademicRepository;
    private final PartTimeAcademicRepository partTimeAcademicRepository;
    private final PaymentRepository paymentRepository;
    private final TimeCardRepository timeCardRepository;

    public PaymentsService(PaymentRepository paymentRepository, AcademicRepository academicRepository, TimeCardRepository timeCardRepository, FullTimeAcademicRepository fullTimeAcademicRepository, PartTimeAcademicRepository partTimeAcademicRepository) {
        this.paymentRepository = paymentRepository;
        this.academicRepository = academicRepository;
        this.timeCardRepository = timeCardRepository;
        this.fullTimeAcademicRepository = fullTimeAcademicRepository;
        this.partTimeAcademicRepository = partTimeAcademicRepository;
    }

    //    @Scheduled(fixedDelay = 100000)
    @Async
    @Scheduled(cron = "@monthly")
    public void payFullTimeAcademicsSalaries() throws InterruptedException {
        List<FullTimeAcademic> fullTimeAcademics = fullTimeAcademicRepository.findAll();

        for (FullTimeAcademic fullTimeAcademic : fullTimeAcademics) {
            Payment payment = new Payment();
            payment.setAcademic(fullTimeAcademic.getAcademic());
            payment.setSalary(fullTimeAcademic.getAcademic().getFlatSalary());
            payment.setTax(fullTimeAcademic.getAcademic().getFlatSalary() * 0.15);
            payment.setDate(new Date());

            paymentRepository.save(payment);
        }
    }

    //    @Scheduled(fixedDelay = 100000)
    @Async
    @Scheduled(cron = "@monthly")
    public void payPartTimeAcademicsSalaries() throws InterruptedException {
        List<PartTimeAcademic> partTimeAcademics = partTimeAcademicRepository.findAll();

        for (PartTimeAcademic partTimeAcademic : partTimeAcademics) {
            int totalWorkedHours = 0;

            List<TimeCard> timeCards = timeCardRepository.findByPartTimeAcademicId(partTimeAcademic.getId());
            for (TimeCard timeCard : timeCards) {
                if (isCurrentMonth(timeCard.getDate())) {
                    totalWorkedHours += timeCard.getHoursCount();
                }
            }

            if (totalWorkedHours > 40) {
                totalWorkedHours = 40;
            }

            Payment payment = new Payment();
            payment.setAcademic(partTimeAcademic.getAcademic());
            payment.setTax(0);
            payment.setPaymentMethodType(partTimeAcademic.getAcademic().getPaymentMethodType());
            payment.setSalary(partTimeAcademic.getAcademic().getFlatSalary() * totalWorkedHours);
            payment.setDate(new Date());

            paymentRepository.save(payment);
        }
    }

    private boolean isCurrentMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        DateTime now = DateTime.now();
        return (dateTime.getYear() == now.getYear()) && (dateTime.getMonthOfYear() == now.getMonthOfYear());
    }
}