package com.f776.vientosdelsur.api.employee;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EmployeeDTO(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String dayOff,
        LocalDate entryDate,
        String occupation
) {
}