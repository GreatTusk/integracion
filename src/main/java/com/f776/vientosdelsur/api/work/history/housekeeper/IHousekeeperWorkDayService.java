package com.f776.vientosdelsur.api.work.history.housekeeper;

import com.f776.vientosdelsur.api.employee.EmployeeNotFoundException;
import com.f776.vientosdelsur.api.employee.occupation.WrongOccupationException;
import com.f776.vientosdelsur.api.response.NoContentException;

import java.time.LocalDate;
import java.util.List;

public interface IHousekeeperWorkDayService {
    HousekeeperWorkDayDTO getHousekeeperWorkDayOn(Long employeeId, LocalDate date)
            throws EmployeeNotFoundException, WrongOccupationException, NoContentException;

    List<HousekeeperWorkDayDTO> getHousekeepersWorkDayOn(LocalDate date) throws NoContentException;
    List<HousekeeperWorkDayDTO> getHousekeeperWorkDaysOnRange(Long employeeId, LocalDate startDate, LocalDate endDate)
            throws EmployeeNotFoundException, WrongOccupationException, NoContentException;
}
