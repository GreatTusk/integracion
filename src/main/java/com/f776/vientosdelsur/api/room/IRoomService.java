package com.f776.vientosdelsur.api.room;

import java.util.List;

public interface IRoomService {
    List<Room> getAllRoomsInfo();
    Room getRoomInfo(Integer roomId);
}
