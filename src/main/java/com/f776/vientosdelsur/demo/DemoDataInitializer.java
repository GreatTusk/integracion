package com.f776.vientosdelsur.demo;

import com.f776.vientosdelsur.api.employee.Employee;
import com.f776.vientosdelsur.api.employee.EmployeeRepository;
import com.f776.vientosdelsur.api.employee.attendance.EmployeeAttendance;
import com.f776.vientosdelsur.api.employee.attendance.EmployeeAttendanceRepository;
import com.f776.vientosdelsur.api.employee.availability.AvailabilityStatus;
import com.f776.vientosdelsur.api.employee.availability.EmployeeAvailability;
import com.f776.vientosdelsur.api.employee.availability.EmployeeAvailabilityRepository;
import com.f776.vientosdelsur.api.employee.occupation.Occupation;
import com.f776.vientosdelsur.api.employee.occupation.housekeeper.Housekeeper;
import com.f776.vientosdelsur.api.employee.occupation.housekeeper.HousekeeperOccupation;
import com.f776.vientosdelsur.api.employee.occupation.housekeeper.HousekeeperRepository;
import com.f776.vientosdelsur.api.guest.Guest;
import com.f776.vientosdelsur.api.guest.GuestRepository;
import com.f776.vientosdelsur.api.room.Room;
import com.f776.vientosdelsur.api.room.RoomRepository;
import com.f776.vientosdelsur.api.room.booking.RoomBooking;
import com.f776.vientosdelsur.api.room.booking.RoomBookingRepository;
import com.f776.vientosdelsur.api.user.Role;
import com.f776.vientosdelsur.api.user.User;
import com.f776.vientosdelsur.api.user.UserRepository;
import com.f776.vientosdelsur.api.work.history.WorkDayHistory;
import com.f776.vientosdelsur.api.work.history.WorkDayHistoryRepository;
import com.f776.vientosdelsur.api.work.history.housekeeper.HousekeeperWorkDay;
import com.f776.vientosdelsur.api.work.history.housekeeper.HousekeeperWorkDayRepository;
import com.f776.vientosdelsur.api.work.shift.ShiftDetails;
import com.f776.vientosdelsur.api.work.shift.ShiftDetailsRepository;
import com.f776.vientosdelsur.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Transactional
@Order(4)
public class DemoDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final HousekeeperRepository housekeeperRepository;
    private final UserRepository userRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final RoomBookingRepository roomBookingRepository;
    private final ShiftDetailsRepository shiftDetailsRepository;
    private final WorkDayHistoryRepository workDayHistoryRepository;
    private final HousekeeperWorkDayRepository housekeeperWorkDayRepository;
    private final EmployeeAvailabilityRepository employeeAvailabilityRepository;
    private final EmployeeAttendanceRepository employeeAttendanceRepository;

    /*
     * This class initializes tables that in production would contain dynamic data.
     * Other "...Initializer" classes take care of populating tables that will always contain the
     * same data.
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        addSampleUsersIfNotExists();
    }

    private void addSampleUsersIfNotExists() {

        Role[] roles = Role.values();
        Occupation[] occupations = Occupation.values();
        List<Room> rooms = roomRepository.findAll();
        List<ShiftDetails> shifts = shiftDetailsRepository.findAll();

        Random seed = new Random();
        for (Role role : roles) {
            for (int j = 0; j < 5; j++) {
                String defaultEmail = "user" + seed.nextInt(100, 999) + "@email.com";

                if (userRepository.existsByEmail(defaultEmail)) {
                    continue;
                }

                User user = User
                        .builder()
                        .email(defaultEmail)
                        .password(passwordEncoder.encode("Contrasena." + seed.nextInt(100, 999)))
                        .role(role)
                        .isEnabled(true)
                        .build();

                Occupation occupation = Utils.pickRandom(List.of(occupations));
                Employee employee = Employee
                        .builder()
                        .dayOff(DayOfWeek.of(seed.nextInt(1, 8)))
                        .phoneNumber(String.valueOf(seed.nextInt(100000000, 999999999)))
                        .firstName("john" + seed.nextInt(100, 999))
                        .lastName("doe" + seed.nextInt(100, 999))
                        .occupation(occupation)
                        .user(user)
                        .entryDate(LocalDate.now())
                        .build();
                employeeRepository.save(employee);

                LocalDate startDate = LocalDate.now().minusDays(seed.nextInt(30));
                LocalDate endDate = startDate.plusDays(seed.nextInt(1, 10));
                EmployeeAvailability employeeAvailability = EmployeeAvailability
                        .builder()
                        .employee(employee)
                        .availabilityStatus(Utils.pickRandom(List.of(AvailabilityStatus.values())))
                        .date(startDate)
                        .build();
                employeeAvailabilityRepository.save(employeeAvailability);

                EmployeeAttendance employeeAttendance = EmployeeAttendance
                        .builder()
                        .employee(employee)
                        .date(startDate)
                        .clockInTime(LocalTime.of(seed.nextInt(6, 11), seed.nextInt(0, 60)))
                        .clockOutTime(LocalTime.of(seed.nextInt(15, 18), seed.nextInt(0, 60)))
                        .build();
                employeeAttendanceRepository.save(employeeAttendance);

                if (employee.getOccupation() == Occupation.MUCAMA) {
                    Housekeeper housekeeper = Housekeeper
                            .builder()
                            .employee(employee)
                            .housekeeperOccupation(Utils.pickRandom(List.of(HousekeeperOccupation.values())))
                            .build();
                    housekeeperRepository.save(housekeeper);
                }


                userRepository.save(user);

                addSampleGuestsAndBookings(rooms, startDate, endDate);
                addSampleWorkDayHistoriesAndHousekeeperWorkHistories(startDate, employee, shifts, rooms);
            }
        }
    }

    private void addSampleGuestsAndBookings(List<Room> rooms, LocalDate startDate, LocalDate endDate) {
        Random seed = new Random();

        String email = "guest" + seed.nextInt(100, 999) + "@example.com";

        Guest guest = Guest
                .builder()
                .firstName("GuestFirstName" + seed.nextInt(100, 999))
                .lastName("GuestLastName" + seed.nextInt(100, 999))
                .email(email)
                .build();
        guestRepository.save(guest);

        RoomBooking booking = RoomBooking
                .builder()
                .guest(guest)
                .room(Utils.pickRandom(rooms))
                .startDate(startDate)
                .endDate(endDate)
                .build();
        roomBookingRepository.save(booking);

    }

    private void addSampleWorkDayHistoriesAndHousekeeperWorkHistories(LocalDate date, Employee employee, List<ShiftDetails> shifts, List<Room> rooms) {
        WorkDayHistory workDayHistory = WorkDayHistory
                .builder()
                .date(date)
                .employee(employee)
                .shiftDetails(Utils.pickRandom(shifts))
                .build();
        workDayHistoryRepository.save(workDayHistory);

        if (employee.getOccupation() == Occupation.MUCAMA) {
            HousekeeperWorkDay housekeeperWorkDay = HousekeeperWorkDay
                    .builder()
                    .workDayHistory(workDayHistory)
                    .rooms(Utils.pickRandomRange(rooms))
                    .build();
            housekeeperWorkDayRepository.save(housekeeperWorkDay);
        }

    }
}
