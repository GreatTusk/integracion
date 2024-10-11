package com.f776.vientosdelsur.api.work.history.housekeeper;

import com.f776.vientosdelsur.api.room.Room;
import com.f776.vientosdelsur.api.work.history.WorkDayHistoryDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record HousekeeperWorkDayDTO(
        Long id,
        WorkDayHistoryDTO employeeWorkDayHistory,
        List<Room> assignedRooms
) {
}
