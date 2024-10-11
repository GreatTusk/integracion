package com.f776.vientosdelsur.api.work.history.housekeeper;

import com.f776.vientosdelsur.api.work.history.WorkDayHistoryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class HousekeeperWorkDayMapper implements Function<HousekeeperWorkDay, HousekeeperWorkDayDTO> {

    private WorkDayHistoryMapper workDayHistoryMapper;
    @Override
    public HousekeeperWorkDayDTO apply(HousekeeperWorkDay housekeeperWorkDay) {
        return HousekeeperWorkDayDTO
                .builder()
                .id(housekeeperWorkDay.getId())
                .employeeWorkDayHistory(workDayHistoryMapper.apply(housekeeperWorkDay.getWorkDayHistory()))
                .assignedRooms(housekeeperWorkDay.getRooms())
                .build();
    }
}
