package com.yousef.payroll.repositories;

import com.yousef.payroll.model.AcademicLeave;
import com.yousef.payroll.model.TimeCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeavesRepository extends CrudRepository<AcademicLeave, Long> {

    @Query("SELECT leave FROM AcademicLeave leave WHERE leave.academic.id = ?1")
    List<AcademicLeave> findByFullTimeAcademicId(long fullTimeAcademicId);
}
