package com.yousef.payroll.repositories;

import com.yousef.payroll.model.users.Academic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicRepository extends CrudRepository<Academic, Long> {

    Academic findByEmail(String email);

    List<Academic> findAll();
}
