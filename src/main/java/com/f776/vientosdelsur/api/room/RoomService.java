package com.f776.vientosdelsur.api.room;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;

    @Override
    public List<Room> getAllRoomsInfo() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomInfo(Integer roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow();
    }
}
