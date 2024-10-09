package com.f776.vientosdelsur.api.work.history;

import com.f776.vientosdelsur.api.employee.Employee;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkDayHistoryRepository extends JpaRepository<WorkDayHistory, Long> {
    Optional<WorkDayHistory> findByEmployeeAndDate(@NotNull Employee employee, @NotNull LocalDate date);
}
