package com.f776.vientosdelsur.api.employee.availability;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeAvailabilityRepository extends JpaRepository<EmployeeAvailability, Long> {
    List<EmployeeAvailability> findByEmployee_IdAndDateBetween(Long employeeId,
                                                               @NotNull LocalDate startDate,
                                                               @NotNull LocalDate endDate);

    Optional<EmployeeAvailability> findByEmployee_IdAndDate(Long employee_id,
                                                            @NotNull LocalDate date);
}
