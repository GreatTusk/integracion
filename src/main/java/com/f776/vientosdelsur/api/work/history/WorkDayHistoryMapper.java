package com.f776.vientosdelsur.api.work.history;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class WorkDayHistoryMapper implements Function<WorkDayHistory, WorkDayHistoryDTO> {
    @Override
    public WorkDayHistoryDTO apply(WorkDayHistory workDayHistory) {
        String employeeURI = "/api/v1/employees/" + workDayHistory.getEmployee().getId();
        return WorkDayHistoryDTO
                .builder()
                .id(workDayHistory.getId())
                .date(workDayHistory.getDate())
                .employeeURI(employeeURI)
                .shiftDetails(workDayHistory.getShiftDetails())
                .build();
    }
}
