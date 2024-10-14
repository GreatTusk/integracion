package com.f776.vientosdelsur.api.employee;

import com.f776.vientosdelsur.api.employee.attendance.EmployeeAttendance;
import com.f776.vientosdelsur.api.employee.availability.EmployeeAvailability;
import com.f776.vientosdelsur.api.employee.occupation.Occupation;
import com.f776.vientosdelsur.api.employee.occupation.housekeeper.Housekeeper;
import com.f776.vientosdelsur.api.user.User;
import com.f776.vientosdelsur.api.work.history.WorkDayHistory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    // Child
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @OneToOne(mappedBy = "employee")
    private Housekeeper housekeeper;

    @OneToMany(mappedBy = "employee")
    private List<EmployeeAvailability> employeeAvailability;

    @OneToMany(mappedBy = "employee")
    private List<EmployeeAttendance> employeeAttendance;

    @OneToMany(mappedBy = "employee")
    private List<WorkDayHistory> workDayHistory;
}