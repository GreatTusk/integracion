package com.f776.vientosdelsur.api.employee.attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IEmployeeAttendanceService {
    Optional<EmployeeAttendance> getEmployeeAttendanceToday(Long id, LocalDate date);
    Optional<EmployeeAttendance> getEmployeeAttendanceToday(Long id);
    List<EmployeeAttendance> getAllEmployeeAttendance();
}
