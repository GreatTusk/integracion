package com.f776.vientosdelsur.api.employee.availability;

import com.f776.vientosdelsur.api.employee.Employee;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmployeeAvailabilityMapper implements Function<EmployeeAvailability, EmployeeAvailabilityDTO> {


    @Override
    public EmployeeAvailabilityDTO apply(EmployeeAvailability employeeAvailability) {

        Employee employee = employeeAvailability.getEmployee();
        String employeeURI = "/api/v1/employees/" + employee.getId();

        return EmployeeAvailabilityDTO
                .builder()
                .id(employeeAvailability.getId())
                .availabilityStatus(employeeAvailability.getAvailabilityStatus())
                .employeeURI(employeeURI)
                .date(employeeAvailability.getDate())
                .build();
    }
}
