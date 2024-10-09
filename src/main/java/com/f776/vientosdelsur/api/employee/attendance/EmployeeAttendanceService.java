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

    private final EmployeeAttendanceMapper employeeAttendanceMapper;
    private final EmployeeRepository employeeRepository;
    private final EmployeeAttendanceRepository employeeAttendanceRepository;

    @Override
    public Optional<EmployeeAttendanceDTO> getEmployeeAttendanceOn(Long employeeId, LocalDate date) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee with id " + employeeId + " not found");
        }

        Optional<EmployeeAttendance> employeeAttendance = employeeAttendanceRepository.getEmployeeAttendanceByEmployee_IdAndDate(employeeId, date);
        return employeeAttendance.map(employeeAttendanceMapper);
    }

    @Override
    public List<EmployeeAttendanceDTO> getEmployeeAttendanceRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee with id " + employeeId + " not found");
        }
        return employeeAttendanceRepository
                .findByEmployee_IdAndDateGreaterThanEqualAndDateLessThanEqual(employeeId, startDate, endDate)
                .stream()
                .map(employeeAttendanceMapper)
                .toList();
    }

    @Override
    public List<EmployeeAttendanceDTO> getEmployeesAttendanceRange(LocalDate startDate, LocalDate endDate) {
        return employeeAttendanceRepository
                .findAllByDateGreaterThanEqualAndDateLessThanEqual(startDate, endDate)
                .stream()
                .map(employeeAttendanceMapper)
                .toList();
    }
}
