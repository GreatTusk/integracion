package com.f776.vientosdelsur.api.work.history.housekeeper;

import com.f776.vientosdelsur.api.employee.Employee;
import com.f776.vientosdelsur.api.employee.EmployeeNotFoundException;
import com.f776.vientosdelsur.api.employee.EmployeeRepository;
import com.f776.vientosdelsur.api.employee.occupation.Occupation;
import com.f776.vientosdelsur.api.employee.occupation.WrongOccupationException;
import com.f776.vientosdelsur.api.response.NoContentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class HousekeeperWorkDayService implements IHousekeeperWorkDayService {

    private final HousekeeperWorkDayRepository housekeeperWorkDayRepository;
    private final EmployeeRepository employeeRepository;
    private final HousekeeperWorkDayMapper housekeeperWorkDayMapper;

    @Override
    public HousekeeperWorkDayDTO getHousekeeperWorkDayOn(Long employeeId, LocalDate date)
            throws EmployeeNotFoundException, WrongOccupationException, NoContentException {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + employeeId + " not found"));

        if (employee.getOccupation() != Occupation.MUCAMA) {
            throw new WrongOccupationException("Employee with id " + employeeId + " is not a housekeeper");
        }

        return housekeeperWorkDayRepository.findByWorkDayHistory_Employee_IdAndWorkDayHistory_Date(employeeId, date)
                .map(housekeeperWorkDayMapper)
                .orElseThrow(() -> new NoContentException("Housekeeper " + employeeId + " does not work on " + date.toString()));
    }

    @Override
    public List<HousekeeperWorkDayDTO> getHousekeepersWorkDayOn(LocalDate date) throws NoContentException {

        List<Long> housekeeperIds = employeeRepository
                .findAll()
                .stream()
                .filter(employee -> employee.getOccupation() == Occupation.MUCAMA)
                .map(Employee::getId)
                .toList();

        if (housekeeperIds.isEmpty()) {
            throw new NoContentException("No work is planned for " + date.toString());
        }

        return housekeeperWorkDayRepository.findAllByHousekeeperIds(housekeeperIds)
                .stream()
                .map(housekeeperWorkDayMapper)
                .toList();
    }

    @Override
    public List<HousekeeperWorkDayDTO> getHousekeeperWorkDaysOnRange(Long employeeId, LocalDate startDate, LocalDate endDate)
            throws EmployeeNotFoundException, WrongOccupationException, NoContentException {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + employeeId + " not found"));

        if (employee.getOccupation() != Occupation.MUCAMA) {
            throw new WrongOccupationException("Employee with id " + employeeId + " is not a housekeeper");
        }

        List<HousekeeperWorkDayDTO> housekeeperWorkDays = housekeeperWorkDayRepository.findAllByEmployeeIdOnRange(employeeId, startDate, endDate)
                .stream()
                .map(housekeeperWorkDayMapper)
                .toList();

        if (housekeeperWorkDays.isEmpty()) {
            throw new NoContentException("Housekeeper " + employeeId + " does not work between " + startDate.toString() + " and " + endDate.toString());
        }

        return housekeeperWorkDays;
    }
}
