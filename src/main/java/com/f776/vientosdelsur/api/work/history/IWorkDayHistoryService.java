package com.f776.vientosdelsur.api.work.history;


import java.time.LocalDate;
import java.util.List;

public interface IWorkDayHistoryService {
    WorkDayHistoryDTO getWorkDayByEmployeeOn(Long employeeId, LocalDate date);
    List<WorkDayHistoryDTO> getAllEmployeesWorkDayOn(LocalDate date);
    List<WorkDayHistoryDTO> getAllWorkDaysByEmployeeOnRange(Long employeeId, LocalDate startDate, LocalDate endDate);
}
