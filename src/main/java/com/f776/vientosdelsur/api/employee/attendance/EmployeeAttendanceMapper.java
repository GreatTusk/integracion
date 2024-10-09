package com.f776.vientosdelsur.api.employee.attendance;

import com.f776.vientosdelsur.api.employee.Employee;
import com.f776.vientosdelsur.api.work.history.WorkDayHistory;
import com.f776.vientosdelsur.api.work.history.WorkDayHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class EmployeeAttendanceMapper implements Function<EmployeeAttendance, EmployeeAttendanceDTO> {

    private final WorkDayHistoryRepository workDayHistoryRepository;

    @Override
    public EmployeeAttendanceDTO apply(EmployeeAttendance employeeAttendance) {
        // TODO: Extract to constants
        Employee employee = employeeAttendance.getEmployee();
        // Maybe not needed
        String employeeURI = "/api/v1/employees/" + employee.getId();

        Optional<WorkDayHistory> workDay = workDayHistoryRepository
                .findByEmployeeAndDate(employee, employeeAttendance.getDate());

        return workDay
                .map(workDayHistory -> EmployeeAttendanceDTO
                        .builder()
                        .id(employeeAttendance.getId())
                        .date(employeeAttendance.getDate())
                        .clockInTime(employeeAttendance.getClockInTime())
                        .clockOutTime(employeeAttendance.getClockOutTime())
                        .clockInStatus(getClockInStatus(workDayHistory.getShiftDetails().getStartTime(), employeeAttendance.getClockInTime()))
                        .clockOutStatus(getClockOutStatus(workDayHistory.getShiftDetails().getEndTime(), employeeAttendance.getClockOutTime()))
                        .employeeURI(employeeURI)
                        .build())
                .orElse(null);
    }

    private ClockInStatus getClockInStatus(LocalTime startTime, LocalTime clockInTime) {
        if (clockInTime == null) {
            return ClockInStatus.ABSENT;
        } else if (clockInTime.isAfter(startTime)) {
            return ClockInStatus.LATE;
        } else {
            return ClockInStatus.ON_TIME;
        }
    }

    private ClockOutStatus getClockOutStatus(LocalTime endTime, LocalTime clockOutTime) {
        if (clockOutTime == null) {
            return ClockOutStatus.NOT_REGISTERED;
        }

        final int MINUTES_THRESHOLD = 10;
        final LocalTime MIN = clockOutTime.minusMinutes(MINUTES_THRESHOLD);
        final LocalTime MAX = clockOutTime.plusMinutes(MINUTES_THRESHOLD);

        if (endTime.isBefore(MIN)) {
            return ClockOutStatus.EARLY;
        } else if (endTime.isAfter(MAX)) {
            return ClockOutStatus.LATE;
        } else {
            return ClockOutStatus.ON_TIME;
        }
    }
}
