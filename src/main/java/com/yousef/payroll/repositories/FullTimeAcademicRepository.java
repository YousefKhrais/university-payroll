package com.yousef.payroll.repositories;

import com.yousef.payroll.model.users.FullTimeAcademic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FullTimeAcademicRepository extends CrudRepository<FullTimeAcademic, Long> {

    FullTimeAcademic findByAcademicId(long AcademicId);

    List<FullTimeAcademic> findAll();
}
