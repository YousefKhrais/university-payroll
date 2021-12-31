package com.yousef.payroll.repositories;

import com.yousef.payroll.model.AcademicLeave;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeavesRepository extends CrudRepository<AcademicLeave, Long> {
}
