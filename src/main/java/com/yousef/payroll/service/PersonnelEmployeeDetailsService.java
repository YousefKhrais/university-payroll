package com.yousef.payroll.service;

import com.yousef.payroll.model.users.PersonnelEmployee;
import com.yousef.payroll.repositories.PersonnelEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonnelEmployeeDetailsService implements UserDetailsService {

    @Autowired
    private PersonnelEmployeeRepository personnelEmployeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: " + username);
        PersonnelEmployee personnelEmployee = personnelEmployeeRepository.findByEmail(username);

        if (personnelEmployee == null) {
            throw new UsernameNotFoundException("User not found");
        }

        System.out.println("Personnel Employee: " + personnelEmployee);

        System.out.println("Auth: " + personnelEmployee.getAuthorities().toString());
        return personnelEmployee;
    }
}