package com.f776.vientosdelsur.api.employee.attendance;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance, Long> {
    Optional<EmployeeAttendance> getEmployeeAttendanceByEmployee_IdAndDate(Long id, LocalDate date);

    List<EmployeeAttendance> findAllByDateGreaterThanEqualAndDateLessThanEqual(@NotNull LocalDate startDate,
                                                                                  @NotNull LocalDate endDate);

    List<EmployeeAttendance> findByEmployee_IdAndDateGreaterThanEqualAndDateLessThanEqual(Long employeeId, @NotNull LocalDate startDate, @NotNull LocalDate endDate);
}
