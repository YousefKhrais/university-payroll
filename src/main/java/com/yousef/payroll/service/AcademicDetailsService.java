package com.yousef.payroll.service;

import com.yousef.payroll.model.types.AcademicType;
import com.yousef.payroll.model.users.Academic;
import com.yousef.payroll.model.users.FullTimeAcademic;
import com.yousef.payroll.model.users.PartTimeAcademic;
import com.yousef.payroll.repositories.AcademicRepository;
import com.yousef.payroll.repositories.FullTimeAcademicRepository;
import com.yousef.payroll.repositories.PartTimeAcademicRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AcademicDetailsService implements UserDetailsService {

    private final AcademicRepository academicRepository;
    private final FullTimeAcademicRepository fullTimeAcademicRepository;
    private final PartTimeAcademicRepository partTimeAcademicRepository;

    public AcademicDetailsService(AcademicRepository academicRepository, FullTimeAcademicRepository fullTimeAcademicRepository, PartTimeAcademicRepository partTimeAcademicRepository) {
        this.academicRepository = academicRepository;
        this.fullTimeAcademicRepository = fullTimeAcademicRepository;
        this.partTimeAcademicRepository = partTimeAcademicRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: " + username);
        Academic academic = academicRepository.findByEmail(username);

        if (academic == null) {
            throw new UsernameNotFoundException("User not found");
        }

        System.out.println("Academic: " + academic);

        if (academic.getType() == AcademicType.FULL_TIME_ACADEMIC) {
            FullTimeAcademic fullTimeAcademic = fullTimeAcademicRepository.findByAcademicId(academic.getId());
            return fullTimeAcademic;
        } else if (academic.getType() == AcademicType.PART_TIME_ACADEMIC) {
            PartTimeAcademic partTimeAcademic = partTimeAcademicRepository.findByAcademicId(academic.getId());
            return partTimeAcademic;
        } else {
            throw new RuntimeException("User type is not valid");
        }
    }
}