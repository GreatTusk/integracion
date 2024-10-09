package com.f776.vientosdelsur.api.employee.attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IEmployeeAttendanceService {
    Optional<EmployeeAttendanceDTO> getEmployeeAttendanceOn(Long employeeId, LocalDate date);
    List<EmployeeAttendanceDTO> getEmployeeAttendanceRange(Long employeeId, LocalDate startDate, LocalDate endDate);
    List<EmployeeAttendanceDTO> getEmployeesAttendanceRange(LocalDate startDate, LocalDate endDate);
}
