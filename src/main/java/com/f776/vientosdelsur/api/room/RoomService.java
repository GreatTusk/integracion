package com.f776.vientosdelsur.api.room;

import com.f776.vientosdelsur.api.room.booking.RoomBooking;
import com.f776.vientosdelsur.api.room.booking.RoomBookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomBookingRepository roomBookingRepository;

    public List<RoomBooking> getCurrentBookings(Room room) {
        LocalDate today = LocalDate.now();
        return roomBookingRepository
                .findByRoomAndStartDateLessThanEqualAndEndDateGreaterThanEqual(room, today, today);
    }

}
