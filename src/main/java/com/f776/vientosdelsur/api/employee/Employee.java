package com.f776.vientosdelsur.api.employee;

import com.f776.vientosdelsur.api.employee.occupation.Occupation;
import com.f776.vientosdelsur.api.user.User;
import com.f776.vientosdelsur.api.work.history.WorkDayHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Size(min = 9, max = 9)
    private String phoneNumber;
    private DayOfWeek dayOff;
    @NotNull
    private LocalDate entryDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Occupation occupation;

    @OneToOne(mappedBy = "employee")
    private User user;

    @OneToMany(mappedBy = "employee")
    private List<WorkDayHistory> workDayHistory;
}
