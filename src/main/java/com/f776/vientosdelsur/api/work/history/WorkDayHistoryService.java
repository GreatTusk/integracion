package com.f776.vientosdelsur.api.work.history;

import com.f776.vientosdelsur.api.employee.EmployeeNotFoundException;
import com.f776.vientosdelsur.api.employee.EmployeeRepository;
import com.f776.vientosdelsur.api.response.NoContentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class WorkDayHistoryService implements IWorkDayHistoryService {

    private final WorkDayHistoryRepository workDayHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkDayHistoryMapper workDayHistoryMapper;

    @Override
    public WorkDayHistoryDTO getWorkDayByEmployeeOn(Long employeeId, LocalDate date) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee with id " + employeeId + " not found");
        }
        return workDayHistoryRepository.findByEmployee_IdAndDate(employeeId, date)
                .map(workDayHistoryMapper)
                .orElseThrow(() -> new NoContentException("Employee " + employeeId + " does not work on " + date));
    }

    @Override
    public List<WorkDayHistoryDTO> getAllEmployeesWorkDayOn(LocalDate date) {
        List<WorkDayHistoryDTO> workDayHistoryDTOS = workDayHistoryRepository.findAllByDate(date)
                .stream()
                .map(workDayHistoryMapper)
                .toList();

        if (workDayHistoryDTOS.isEmpty()) {
            throw new NoContentException("No employees work on " + date);
        }

        return workDayHistoryDTOS;
    }

    @Override
    public List<WorkDayHistoryDTO> getAllWorkDaysByEmployeeOnRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<WorkDayHistoryDTO> workDayHistoryDTOS = workDayHistoryRepository.findAllByEmployeeIdOnRange(employeeId, startDate, endDate)
                .stream()
                .map(workDayHistoryMapper)
                .toList();

        if (workDayHistoryDTOS.isEmpty()) {
            throw new NoContentException("Employee " + employeeId + " does not work between " + startDate.toString() + " and " + endDate.toString());
        }

        return workDayHistoryDTOS;
    }
}
