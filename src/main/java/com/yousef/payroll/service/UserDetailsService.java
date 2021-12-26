package com.yousef.payroll.service;

import com.yousef.payroll.model.Academic;
import com.yousef.payroll.model.PersonnelEmployee;
import com.yousef.payroll.repositories.AcademicRepository;
import com.yousef.payroll.repositories.PersonnelEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private AcademicRepository academicRepository;

    @Autowired
    private PersonnelEmployeeRepository personnelEmployeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: " + username);
        Academic academic = academicRepository.findByEmail(username);

        if (academic == null) {
            throw new UsernameNotFoundException("User not found");
        }

        System.out.println("Academic: " + academic);

        System.out.println("username: " + username);
        PersonnelEmployee personnelEmployee = personnelEmployeeRepository.findByEmail(username);

        if (personnelEmployee == null) {
            throw new UsernameNotFoundException("User not found");
        }

        System.out.println("Personnel Employee: " + personnelEmployee);


        return academic;
    }
}