package com.f776.vientosdelsur.api.employee.attendance;

import com.f776.vientosdelsur.api.employee.EmployeeNotFoundException;
import com.f776.vientosdelsur.api.employee.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeAttendanceService implements IEmployeeAttendanceService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeAttendanceRepository employeeAttendanceRepository;

    @Override
    public Optional<EmployeeAttendance> getEmployeeAttendanceToday(Long id, LocalDate date) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }
        return employeeAttendanceRepository.getEmployeeAttendanceByEmployee_IdAndDate(id, date);
    }

    @Override
    public Optional<EmployeeAttendance> getEmployeeAttendanceToday(Long id) {
        return getEmployeeAttendanceToday(id, LocalDate.now());
    }

    @Override
    public List<EmployeeAttendance> getAllEmployeeAttendance() {
        return employeeAttendanceRepository.findAll();
    }
}
