package com.f776.vientosdelsur.api.employee;

import java.util.List;

public interface IEmployeeService {
    List<Employee> getAllEmployees();
    EmployeeDTO getEmployeeById(Long employeeId);
}
