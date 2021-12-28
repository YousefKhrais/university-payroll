package com.yousef.payroll.bootstrap;

import com.yousef.payroll.repositories.AcademicRepository;
import com.yousef.payroll.repositories.PaymentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {

    private final AcademicRepository academicRepository;
    private final PaymentRepository paymentRepository;

    public BootStrapData(AcademicRepository academicRepository, PaymentRepository paymentRepository) {
        this.academicRepository = academicRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Started in Bootstrap");

//        Academic academic = new Academic();
//        academic.setEmail("yousefokhrais@gmail.com");
//        academic.setPassword("123");
//        academic.setFirstName("FirstName");
//        academic.setLastName("LastName");
//        academic.setType(UserType.FullTimeAcademics);
//        academic.setDepartment("IT");
//        academic.setPhoneNumber("0594111772");
//        academic.setAddress("gaza??");
//        academic.setPaymentDetails("MYEMail");
//        academic.setFlatSalary(1000);
//        academic.setLeaveBalance(10);

//        academicRepository.save(academic);

        System.out.println("Academic Count: " + academicRepository.count());

//        Payment ddd = new Payment(5000, new Date(new java.util.Date().getTime()));
//
//        ddd.setAcademic(academic);
//        academic.getPayments().add(ddd);
//
//        paymentRepository.save(ddd);
//        academicRepository.save(academic);
//
//        Payment dddd = new Payment(3400, new Date(new java.util.Date().getTime()));
//
//        dddd.setAcademic(academic);
//        academic.getPayments().add(dddd);
//
//        paymentRepository.save(dddd);
//        academicRepository.save(academic);
//
//        System.out.println("paymentRepository count: " + paymentRepository.count());
//        System.out.println("Publisher Number of Books: " + academic.getPayments().size());
//
//        for (Academic academic : academicRepository.findAll()) {
//            System.out.println("*******");
//            for (int i = 0; i < academic.getPayments().size(); i++) {
//                System.out.println(academic.getPayments().get(i).toString());
//            }
//            System.out.println("*******");
//        }
    }
}