package com.yousef.payroll.service;

import com.yousef.payroll.model.users.PersonnelEmployee;
import com.yousef.payroll.repositories.PersonnelEmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonnelEmployeeDetailsService implements UserDetailsService {

    private final PersonnelEmployeeRepository personnelEmployeeRepository;

    public PersonnelEmployeeDetailsService(PersonnelEmployeeRepository personnelEmployeeRepository) {
        this.personnelEmployeeRepository = personnelEmployeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PersonnelEmployee personnelEmployee = personnelEmployeeRepository.findByEmail(username);

        if (personnelEmployee == null)
            throw new UsernameNotFoundException("User not found");

        return personnelEmployee;
    }
}