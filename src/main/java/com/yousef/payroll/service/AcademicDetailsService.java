package com.yousef.payroll.service;

import com.yousef.payroll.model.Academic;
import com.yousef.payroll.repositories.AcademicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AcademicDetailsService implements UserDetailsService {

    @Autowired
    private AcademicRepository academicRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: " + username);
        Academic academic = academicRepository.findByEmail(username);

        if (academic == null) {
            throw new UsernameNotFoundException("User not found");
        }

        System.out.println("Academic: " + academic);

        return academic;
    }
}