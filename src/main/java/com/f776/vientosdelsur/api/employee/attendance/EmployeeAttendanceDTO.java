package com.f776.vientosdelsur.api.employee.attendance;

import lombok.Builder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record EmployeeAttendanceDTO(
        Long id,
        URI employeeURI,
        LocalTime clockInTime,
        LocalTime clockOutTime,
        LocalDate date,
        ClockInStatus clockInStatus,
        ClockOutStatus clockOutStatus
) {
}
