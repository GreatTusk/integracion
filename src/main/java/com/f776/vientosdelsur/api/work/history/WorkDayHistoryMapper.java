package com.f776.vientosdelsur.api.work.history;

import com.f776.vientosdelsur.utils.Constants;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.function.Function;

@Service
public class WorkDayHistoryMapper implements Function<WorkDayHistory, WorkDayHistoryDTO> {
    @Override
    public WorkDayHistoryDTO apply(WorkDayHistory workDayHistory) {
        URI employeeURI = Constants.buildEmployeeURI.apply(workDayHistory.getEmployee().getId());
        return WorkDayHistoryDTO
                .builder()
                .id(workDayHistory.getId())
                .date(workDayHistory.getDate())
                .employeeURI(employeeURI)
                .shiftDetails(workDayHistory.getShiftDetails())
                .build();
    }
}
