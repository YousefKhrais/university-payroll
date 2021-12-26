package com.yousef.payroll.repositories;

import com.yousef.payroll.model.PersonnelEmployee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelEmployeeRepository extends CrudRepository<PersonnelEmployee, Long> {

    PersonnelEmployee findByEmail(String email);
}
