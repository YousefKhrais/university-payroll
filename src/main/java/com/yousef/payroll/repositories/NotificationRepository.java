package com.yousef.payroll.repositories;

import com.yousef.payroll.model.AcademicLeave;
import com.yousef.payroll.model.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {
}
