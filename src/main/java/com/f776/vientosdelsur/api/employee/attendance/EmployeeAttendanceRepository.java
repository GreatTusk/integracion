package com.f776.vientosdelsur.api.employee.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance, Long> {
    Optional<EmployeeAttendance> getEmployeeAttendanceByEmployee_IdAndDate(Long id, LocalDate date);
}
