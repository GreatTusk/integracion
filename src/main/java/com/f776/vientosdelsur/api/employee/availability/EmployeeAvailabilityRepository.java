package com.f776.vientosdelsur.api.employee.availability;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeAvailabilityRepository extends JpaRepository<EmployeeAvailability, Long> {
    List<EmployeeAvailability> findByEmployee_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long employee_id, @NotNull LocalDate startDate, @NotNull LocalDate endDate);

    List<EmployeeAvailability> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(@NotNull LocalDate startDate, @NotNull LocalDate endDate);
}
