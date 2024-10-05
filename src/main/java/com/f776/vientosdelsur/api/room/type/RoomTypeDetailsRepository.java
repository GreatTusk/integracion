package com.f776.vientosdelsur.api.room.type;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeDetailsRepository extends JpaRepository<RoomTypeDetails, Integer> {
    RoomTypeDetails findByRoomType(@NotNull RoomType roomType);
}
