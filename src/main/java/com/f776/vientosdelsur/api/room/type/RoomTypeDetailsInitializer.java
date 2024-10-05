package com.f776.vientosdelsur.api.room.type;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Transactional
@RequiredArgsConstructor
@Order(1)
public class RoomTypeDetailsInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final RoomTypeDetailsRepository roomTypeDetailsRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        init();
    }

    private void init() {
        if (roomTypeDetailsRepository.count() == 0) {
            Arrays.stream(RoomType.values()).forEach(roomType -> {
                RoomTypeDetails roomTypeDetails =
                        RoomTypeDetails
                                .builder()
                                .roomType(roomType)
                                .workUnits(getWorkUnits(roomType))
                                .exitWorkUnits(getExitWorkUnits(roomType))
                                .build();
                roomTypeDetailsRepository.save(roomTypeDetails);
            });
        }
    }

    private Integer getWorkUnits(RoomType roomType) {
        return switch (roomType) {
            case SINGLE -> 30;
            case DOUBLE -> 35;
            case TRIPLE -> 45;
            case QUADRUPLE -> 50;
        };
    }

    private Integer getExitWorkUnits(RoomType roomType) {
        return switch (roomType) {
            case SINGLE -> 30;
            case DOUBLE -> 45;
            case TRIPLE -> 55;
            case QUADRUPLE -> 60;
        };
    }
}
