package com.f776.vientosdelsur.api.work.history.housekeeper;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HousekeeperWorkDayRepository extends JpaRepository<HousekeeperWorkDay, Long> {

    Optional<HousekeeperWorkDay> findByWorkDayHistory_Employee_IdAndWorkDayHistory_Date(Long employeeId,
                                                                                        @NotNull LocalDate date);

    @Query("""
            SELECT hwd
            from HousekeeperWorkDay hwd
            where hwd.workDayHistory.employee.occupation = 'MUCAMA' AND
            hwd.workDayHistory.date = :date
            """)
    List<HousekeeperWorkDay> findAllHousekeepersWorkingOn(@Param("date") @NotNull LocalDate date);

    @Query("""
            SELECT hwd
            FROM HousekeeperWorkDay hwd
            WHERE hwd.workDayHistory.employee.id = :employeeId
            AND hwd.workDayHistory.date BETWEEN :startDate AND :endDate
            """)
    List<HousekeeperWorkDay> findAllByEmployeeIdOnRange(@Param("employeeId") Long employeeId,
                                                        @NotNull @Param("startDate") LocalDate startDate,
                                                        @NotNull @Param("endDate") LocalDate endDate);
}
