package com.f776.vientosdelsur.api.employee.availability;

import java.time.LocalDate;
import java.util.List;

public interface IEmployeeAvailabilityService {
    EmployeeAvailabilityDTO getAvailabilityOn(Long employeeId, LocalDate date);
    List<List<EmployeeAvailabilityDTO>> getAvailabilityRange(LocalDate startDate, LocalDate endDate);
    List<EmployeeAvailabilityDTO> getAvailabilityRangeFor(Long employeeId, LocalDate startDate, LocalDate endDate);

}
