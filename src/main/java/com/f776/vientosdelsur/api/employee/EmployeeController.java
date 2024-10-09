package com.f776.vientosdelsur.api.employee;

import com.f776.vientosdelsur.api.employee.attendance.EmployeeAttendanceDTO;
import com.f776.vientosdelsur.api.employee.attendance.IEmployeeAttendanceService;
import com.f776.vientosdelsur.api.employee.availability.IEmployeeAvailabilityService;
import com.f776.vientosdelsur.api.response.ApiResponse;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final IEmployeeService employeeService;
    private final IEmployeeAvailabilityService employeeAvailabilityService;
    private final IEmployeeAttendanceService employeeAttendanceService;

    /* Employee data */

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllEmployees() {
        try {
            return ResponseEntity.ok(new ApiResponse("Found!", employeeService.getAllEmployees()));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(new ApiResponse("Found!", employeeService.getEmployeeById(employeeId)));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }


    /* Attendance */

    @GetMapping("/attendance")
    public ResponseEntity<ApiResponse> getEmployeeAttendanceRange(@RequestParam(required = false) LocalDate startDate,
                                                                  @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) {
            startDate = YearMonth.now().atDay(1);
        }
        if (endDate == null) {
            endDate = YearMonth.now().atEndOfMonth();
        }

        try {
            return ResponseEntity.ok(new ApiResponse("Found!", employeeAttendanceService.getEmployeesAttendanceRange(startDate, endDate)));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/{employeeId}/attendance/today")
    public ResponseEntity<ApiResponse> getEmployeeAttendanceToday(@PathVariable Long employeeId) {
        try {
            Optional<EmployeeAttendanceDTO> employeeAttendanceToday = employeeAttendanceService.getEmployeeAttendanceOn(employeeId, LocalDate.now());

            if (employeeAttendanceToday.isPresent()) {
                return ResponseEntity.ok(new ApiResponse("Found!", employeeAttendanceToday));
            }

            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/{employeeId}/attendance")
    public ResponseEntity<ApiResponse> getEmployeeAttendanceOn(@PathVariable Long employeeId,
                                                               @RequestParam(required = false) LocalDate date,
                                                               @RequestParam(required = false) LocalDate startDate,
                                                               @RequestParam(required = false) LocalDate endDate) {
        try {
            if (date != null) {
                Optional<EmployeeAttendanceDTO> employeeAttendanceToday = employeeAttendanceService.getEmployeeAttendanceOn(employeeId, date);

                if (employeeAttendanceToday.isPresent()) {
                    return ResponseEntity.ok(new ApiResponse("Found!", employeeAttendanceToday));
                } else {
                    return ResponseEntity
                            .status(HttpStatus.NO_CONTENT)
                            .build();
                }
            } else if (startDate != null && endDate != null) {
                List<EmployeeAttendanceDTO> employeeAttendanceToday = employeeAttendanceService.getEmployeeAttendanceRange(employeeId, startDate, endDate);
                if (!employeeAttendanceToday.isEmpty()) {
                    return ResponseEntity.ok(new ApiResponse("Found!", employeeAttendanceToday));
                } else {
                    return ResponseEntity
                            .status(HttpStatus.NO_CONTENT)
                            .build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Invalid request parameters", null));
            }
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /* Availability */

    @GetMapping("/{employeeId}/availability/today")
    public ResponseEntity<ApiResponse> getEmployeeAvailabilityToday(@PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(new ApiResponse("Found!", employeeAvailabilityService.getAvailabilityOn(employeeId, LocalDate.now())));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/{employeeId}/availability")
    public ResponseEntity<ApiResponse> getEmployeeAvailabilityOn(@PathVariable Long employeeId,
                                                                 @RequestParam(required = false) LocalDate date,
                                                                 @RequestParam(required = false) LocalDate startDate,
                                                                 @RequestParam(required = false) LocalDate endDate) {
        try {
            if (date != null) {
                return ResponseEntity.ok(new ApiResponse("Found!", employeeAvailabilityService.getAvailabilityOn(employeeId, date)));
            } else if (startDate != null && endDate != null) {
                return ResponseEntity.ok(new ApiResponse("Found!", employeeAvailabilityService.getAvailabilityRangeFor(employeeId, startDate, endDate)));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Invalid request parameters", null));
            }
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }


    @GetMapping("/availability")
    public ResponseEntity<ApiResponse> getAllAvailability(@RequestParam LocalDate startDate,
                                                          @RequestParam LocalDate endDate) {
        try {
            return ResponseEntity.ok(new ApiResponse("Found!", employeeAvailabilityService.getAvailabilityRange(startDate, endDate)));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
