package com.f776.vientosdelsur.api.employee;

import com.f776.vientosdelsur.api.work.history.WorkDayHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
