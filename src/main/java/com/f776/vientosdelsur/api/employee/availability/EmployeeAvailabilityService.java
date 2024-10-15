package com.f776.vientosdelsur.api.employee.availability;

import com.f776.vientosdelsur.api.employee.Employee;
import com.f776.vientosdelsur.api.employee.EmployeeNotFoundException;
import com.f776.vientosdelsur.api.employee.EmployeeRepository;
import com.f776.vientosdelsur.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeAvailabilityService implements IEmployeeAvailabilityService {

    private final EmployeeAvailabilityRepository employeeAvailabilityRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeAvailabilityMapper employeeAvailabilityMapper;

    @Override
    public EmployeeAvailabilityDTO getAvailabilityOn(Long employeeId, LocalDate date) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee with id " + employeeId + " not found");
        }

        return employeeAvailabilityRepository.findByEmployee_IdAndDate(employeeId, date)
                .map(employeeAvailabilityMapper)
                .orElse(EmployeeAvailabilityDTO
                        .builder()
                        .id(employeeId)
                        .employeeURI(Constants.buildEmployeeURI.apply(employeeId))
                        .availabilityStatus(AvailabilityStatus.AVAILABLE)
                        .date(date)
                        .build());
    }

    @Override
    public List<List<EmployeeAvailabilityDTO>> getAvailabilityRange(LocalDate startDate, LocalDate endDate) {
        return employeeRepository
                .findAll()
                .stream()
                .mapToLong(Employee::getId)
                .mapToObj(id -> getAvailabilityRangeFor(id, startDate, endDate))
                .toList();
    }

    @Override
    public List<EmployeeAvailabilityDTO> getAvailabilityRangeFor(Long employeeId, LocalDate startDate, LocalDate endDate) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee with id " + employeeId + " not found");
        }

        final long rangeLength = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        List<EmployeeAvailabilityDTO> employeeAvailability = new ArrayList<>(employeeAvailabilityRepository
                .findByEmployee_IdAndDateBetween(employeeId, startDate, endDate)
                .stream()
                .map(employeeAvailabilityMapper)
                .toList());

        // There is an entry for each day -- early return
        if (rangeLength == employeeAvailability.size()) {
            return employeeAvailability;
        }

        final List<LocalDate> existingDates = employeeAvailability
                .stream()
                .map(EmployeeAvailabilityDTO::date)
                .toList();

        final URI employeeURI = Constants.buildEmployeeURI.apply(employeeId);
        // Could possibly optimize in the future -- not needed now
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (!existingDates.contains(currentDate)) {
                employeeAvailability.add(EmployeeAvailabilityDTO
                        .builder()
                        .id(employeeId)
                        .employeeURI(employeeURI)
                        .availabilityStatus(AvailabilityStatus.AVAILABLE)
                        .date(currentDate)
                        .build());
            }
            currentDate = currentDate.plusDays(1);
        }

        return employeeAvailability;
    }

}
