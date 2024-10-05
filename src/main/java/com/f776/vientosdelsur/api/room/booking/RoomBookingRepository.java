package com.f776.vientosdelsur.api.room.booking;

import com.f776.vientosdelsur.api.room.Room;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
    List<RoomBooking> findByRoomAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Room room, @NotNull LocalDate startDate, LocalDate endDate);
}
