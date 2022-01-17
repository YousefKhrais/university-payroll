package com.yousef.payroll.repositories;

import com.yousef.payroll.model.users.PartTimeAcademic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartTimeAcademicRepository extends CrudRepository<PartTimeAcademic, Long> {

    PartTimeAcademic findByAcademicId(long AcademicId);

    List<PartTimeAcademic> findAll();
}
