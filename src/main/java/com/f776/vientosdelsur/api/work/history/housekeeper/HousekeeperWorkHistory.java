package com.f776.vientosdelsur.api.work.history.housekeeper;

import com.f776.vientosdelsur.api.room.Room;
import com.f776.vientosdelsur.api.work.history.WorkDayHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HousekeeperWorkHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_day_history_id")
    private WorkDayHistory workDayHistory;

    @ManyToMany
    @JoinTable(
            name = "housekeeper_work_history_rooms",
            joinColumns = @JoinColumn(name = "housekeeper_work_history_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<Room> rooms;

// TODO: Uncomment or delete when implementing controllers
//    public static class HousekeeperWorkHistoryBuilder {
//        public HousekeeperWorkHistoryBuilder workDayHistory(WorkDayHistory workDayHistory) {
//            this.workDayHistory = checkEmployeeOccupation(workDayHistory);
//            return this;
//        }
//    }
//
//    private static WorkDayHistory checkEmployeeOccupation(WorkDayHistory workDayHistory) {
//        final Employee employee = workDayHistory.getEmployee();
//        if (employee.getOccupation() != Occupation.MUCAMA) {
//            throw new IllegalArgumentException("Employee " + employee.getFirstName() + " is not a housekeeper");
//        }
//        return workDayHistory;
//    }
//
//    public void setWorkDayHistory(WorkDayHistory workDayHistory) {
//        this.workDayHistory = checkEmployeeOccupation(workDayHistory);
//    }
}