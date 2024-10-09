package com.f776.vientosdelsur.api.employee.attendance;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record EmployeeAttendanceDTO(
        Long id,
        String employeeURI,
        LocalTime clockInTime,
        LocalTime clockOutTime,
        LocalDate date,
        ClockInStatus clockInStatus,
        ClockOutStatus clockOutStatus
) {
}
