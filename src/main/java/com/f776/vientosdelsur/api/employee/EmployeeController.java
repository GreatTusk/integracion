package com.f776.vientosdelsur.api.employee;

import com.f776.vientosdelsur.api.employee.attendance.EmployeeAttendance;
import com.f776.vientosdelsur.api.employee.attendance.IEmployeeAttendanceService;
import com.f776.vientosdelsur.api.employee.availability.IEmployeeAvailabilityService;
import com.f776.vientosdelsur.api.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final IEmployeeAvailabilityService employeeAvailabilityService;
    private final IEmployeeAttendanceService employeeAttendanceService;
    private final IEmployeeService employeeService;

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

    @GetMapping("/{employeeId}/availability/today")
    public ResponseEntity<ApiResponse> getEmployeeAvailabilityToday(@PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(new ApiResponse("Found!", employeeAvailabilityService.getAvailabilityToday(employeeId)));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/{employeeId}/attendance/today")
    public ResponseEntity<ApiResponse> getEmployeeAttendanceToday(@PathVariable Long employeeId) {
        try {
            Optional<EmployeeAttendance> employeeAttendanceToday = employeeAttendanceService.getEmployeeAttendanceToday(employeeId);
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


}
