package com.f776.vientosdelsur.api.work.shift;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
@Order(3)
public class ShiftDetailsInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ShiftDetailsRepository shiftDetailsRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        init();
    }

    private void init() {
        List<ShiftDetails> shiftDetails = new ArrayList<>();

        shiftDetails.add(
                ShiftDetails
                        .builder()
                        .shift(Shift.TIEMPO_COMPLETO)
                        .startTime(LocalTime.of(9, 0, 0))
                        .endTime(LocalTime.of(17, 0, 0))
                        .build()
        );

        shiftDetails.add(
                ShiftDetails
                        .builder()
                        .shift(Shift.APOYO_COCINA)
                        .startTime(LocalTime.of(8, 0, 0))
                        .endTime(LocalTime.of(16, 0, 0))
                        .build()
        );

        shiftDetails.add(
                ShiftDetails
                        .builder()
                        .shift(Shift.ENCARGADA_COCINA)
                        .startTime(LocalTime.of(7, 30, 0))
                        .endTime(LocalTime.of(15, 30, 0))
                        .build()
        );

        shiftDetailsRepository.saveAll(shiftDetails);
    }
}
