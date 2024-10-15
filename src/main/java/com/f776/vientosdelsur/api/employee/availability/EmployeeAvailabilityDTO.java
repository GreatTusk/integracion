package com.f776.vientosdelsur.api.employee.availability;

import lombok.Builder;

import java.net.URI;
import java.time.LocalDate;

@Builder
public record EmployeeAvailabilityDTO(
        Long id,
        URI employeeURI,
        LocalDate date,
        AvailabilityStatus availabilityStatus
) {
}
