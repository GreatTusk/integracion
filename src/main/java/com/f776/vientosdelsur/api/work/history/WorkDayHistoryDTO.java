package com.f776.vientosdelsur.api.work.history;

import com.f776.vientosdelsur.api.work.shift.ShiftDetails;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record WorkDayHistoryDTO(
        Long id,
        LocalDate date,
        String employeeURI,
        ShiftDetails shiftDetails
) {
}
