package com.yousef.payroll.repositories;

import com.yousef.payroll.model.TimeCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeCardRepository extends CrudRepository<TimeCard, Long> {

    @Query("SELECT tc FROM TimeCard tc WHERE tc.academic.id = ?1")
    List<TimeCard> findByPartTimeAcademicId(long partTimeAcademicId);
}
