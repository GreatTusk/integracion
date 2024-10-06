package com.f776.vientosdelsur.api.employee.availability;

import java.time.LocalDate;
import java.util.List;

public interface IEmployeeAvailabilityService {
    AvailabilityStatus getAvailabilityToday(Long employeeId);
    AvailabilityStatus getAvailabilityOn(Long employeeId, LocalDate date);
    List<EmployeeAvailability> getAllAvailabilityRange(LocalDate startDate, LocalDate endDate);
    List<EmployeeAvailability> getAvailabilityRange(Long employeeId, LocalDate startDate, LocalDate endDate);

}
