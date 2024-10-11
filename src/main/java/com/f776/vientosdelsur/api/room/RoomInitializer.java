package com.f776.vientosdelsur.api.room;

import com.f776.vientosdelsur.api.room.type.RoomType;
import com.f776.vientosdelsur.api.room.type.RoomTypeDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
@Order(2)
public class RoomInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final RoomRepository roomRepository;
    private final RoomTypeDetailsRepository roomTypeDetailsRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        init();
    }

    private void init() {
        final int TOTAL_ROOMS = 28;
        if (roomRepository.count() == 28) {
            return;
        }
        List<Room> rooms = new ArrayList<>(TOTAL_ROOMS);

        rooms.add(Room
                .builder()
                .roomNumber("101")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.TRIPLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("102")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.DOUBLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("105")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.DOUBLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("104")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.SINGLE))
                .build());

        for (int i = 206; i < 217; i++) {
            rooms.add(Room
                    .builder()
                    .roomNumber(String.valueOf(i))
                    .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.DOUBLE))
                    .build());
        }

        rooms.add(Room
                .builder()
                .roomNumber("317")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.TRIPLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("318")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.QUADRUPLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("319")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.DOUBLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("320")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.DOUBLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("321")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.TRIPLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("322")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.QUADRUPLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("323")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.DOUBLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("424")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.QUADRUPLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("425")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.QUADRUPLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("426")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.DOUBLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("427")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.DOUBLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("428")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.DOUBLE))
                .build());

        rooms.add(Room
                .builder()
                .roomNumber("429")
                .roomTypeDetails(roomTypeDetailsRepository.findByRoomType(RoomType.DOUBLE))
                .build());

        roomRepository.saveAll(rooms);

    }
}
