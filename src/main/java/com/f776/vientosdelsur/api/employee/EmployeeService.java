package com.f776.vientosdelsur.api.employee;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository
        .findAll()
        .stream()
        .map(employeeMapper)
        .toList();
    }

    @Override
    public EmployeeDTO getEmployeeById(Long employeeId) {
        return employeeMapper.apply(employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + employeeId + " not found")));
    }
}
