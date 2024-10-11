package com.f776.vientosdelsur.api.work.history;


import com.f776.vientosdelsur.api.employee.EmployeeNotFoundException;
import com.f776.vientosdelsur.api.response.NoContentException;

import java.time.LocalDate;
import java.util.List;

public interface IWorkDayHistoryService {
    List<WorkDayHistoryDTO> getAllEmployeeWorkDays() throws NoContentException;

    WorkDayHistoryDTO getWorkDayByEmployeeOn(Long employeeId, LocalDate date)
            throws EmployeeNotFoundException, NoContentException;

    List<WorkDayHistoryDTO> getAllEmployeesWorkDayOn(LocalDate date) throws NoContentException;

    List<WorkDayHistoryDTO> getAllWorkDaysByEmployeeOnRange(Long employeeId, LocalDate startDate, LocalDate endDate)
            throws EmployeeNotFoundException, NoContentException;
}
