package com.yousef.payroll.repositories;

import com.yousef.payroll.model.Academic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicRepository extends CrudRepository<Academic, Long> {

    Academic findByEmail(String email);
}
