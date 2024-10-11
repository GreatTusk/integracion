package com.f776.vientosdelsur.api.work.history;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkDayHistoryRepository extends JpaRepository<WorkDayHistory, Long> {
    Optional<WorkDayHistory> findByEmployee_IdAndDate(Long employee_id, @NotNull LocalDate date);

    List<WorkDayHistory> findAllByDate(@NotNull LocalDate date);

    @Query("""
            SELECT WorkDayHistory
            FROM WorkDayHistory wdh
            WHERE wdh.employee.id = :employeeId AND
            wdh.date between :startDate and :endDate
            """)
    List<WorkDayHistory> findAllByEmployeeIdOnRange(@Param("employeeId") Long employeeId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
}
