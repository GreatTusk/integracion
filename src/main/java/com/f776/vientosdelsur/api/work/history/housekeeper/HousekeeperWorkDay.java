package com.f776.vientosdelsur.api.work.history.housekeeper;

import com.f776.vientosdelsur.api.room.Room;
import com.f776.vientosdelsur.api.work.history.WorkDayHistory;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class HousekeeperWorkDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_day_history_id")
    private WorkDayHistory workDayHistory;

    @ManyToMany
    @JoinTable(
            name = "housekeeper_work_history_rooms",
            joinColumns = @JoinColumn(name = "housekeeper_work_history_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<Room> rooms;

}