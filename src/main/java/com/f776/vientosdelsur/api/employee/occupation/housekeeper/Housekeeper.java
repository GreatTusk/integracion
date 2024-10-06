package com.f776.vientosdelsur.api.employee.occupation.housekeeper;

import com.f776.vientosdelsur.api.employee.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Housekeeper {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @NotNull
    private Employee employee;

    @Enumerated(EnumType.STRING)
    private HousekeeperOccupation housekeeperOccupation;
}
