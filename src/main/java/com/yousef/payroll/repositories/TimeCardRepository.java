package com.yousef.payroll.repositories;

import com.yousef.payroll.model.AcademicLeave;
import com.yousef.payroll.model.TimeCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeCardRepository extends CrudRepository<TimeCard, Long> {
}
