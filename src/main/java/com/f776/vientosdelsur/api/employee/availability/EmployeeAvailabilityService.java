package com.f776.vientosdelsur.api.employee.availability;

import com.f776.vientosdelsur.api.employee.EmployeeNotFoundException;
import com.f776.vientosdelsur.api.employee.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeAvailabilityService implements IEmployeeAvailabilityService {

    private final EmployeeAvailabilityRepository employeeAvailabilityRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public AvailabilityStatus getAvailabilityOn(Long employeeId, LocalDate date) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee with id " + employeeId + " not found");
        }

        List<EmployeeAvailability> availability = employeeAvailabilityRepository
                .findByEmployee_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(employeeId, date, date);

        if (availability.isEmpty()) {
            return AvailabilityStatus.AVAILABLE;
        }

        return availability.get(0).getAvailabilityStatus();
    }

    @Override
    public AvailabilityStatus getAvailabilityToday(Long employeeId) {
        LocalDate today = LocalDate.now();
        return getAvailabilityOn(employeeId, today);
    }

    @Override
    public List<EmployeeAvailability> getAllAvailabilityRange(LocalDate startDate, LocalDate endDate) {
        return employeeAvailabilityRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(startDate, endDate);
    }

    @Override
    public List<EmployeeAvailability> getAvailabilityRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee with id " + employeeId + " not found");
        }

        return employeeAvailabilityRepository
                .findByEmployee_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(employeeId, startDate, endDate);

    }
}
