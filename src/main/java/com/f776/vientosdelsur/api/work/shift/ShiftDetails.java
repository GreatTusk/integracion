package com.f776.vientosdelsur.api.work.shift;

import com.f776.vientosdelsur.api.work.history.WorkDayHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Shift shift;

    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

    @OneToMany(mappedBy = "shiftDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkDayHistory> workDayHistory;
}
