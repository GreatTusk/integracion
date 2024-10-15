package com.f776.vientosdelsur.api.employee.availability;

import com.f776.vientosdelsur.api.employee.Employee;
import com.f776.vientosdelsur.utils.Constants;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.function.Function;

@Service
public class EmployeeAvailabilityMapper implements Function<EmployeeAvailability, EmployeeAvailabilityDTO> {


    @Override
    public EmployeeAvailabilityDTO apply(EmployeeAvailability employeeAvailability) {

        Employee employee = employeeAvailability.getEmployee();
        URI employeeURI = Constants.buildEmployeeURI.apply(employee.getId());

        return EmployeeAvailabilityDTO
                .builder()
                .id(employeeAvailability.getId())
                .availabilityStatus(employeeAvailability.getAvailabilityStatus())
                .employeeURI(employeeURI)
                .date(employeeAvailability.getDate())
                .build();
    }
}
