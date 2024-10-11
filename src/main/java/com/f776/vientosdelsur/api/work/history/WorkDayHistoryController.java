package com.f776.vientosdelsur.api.work.history;

import com.f776.vientosdelsur.api.employee.EmployeeNotFoundException;
import com.f776.vientosdelsur.api.response.ApiResponse;
import com.f776.vientosdelsur.api.response.NoContentException;
import com.f776.vientosdelsur.api.work.history.housekeeper.IHousekeeperWorkDayService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/work-day-history")
@Slf4j
public class WorkDayHistoryController {

    private final IWorkDayHistoryService workDayHistoryService;
    private final IHousekeeperWorkDayService housekeeperWorkDayService;

    // Returns 200/404/204/500
    @GetMapping("/today")
    public ResponseEntity<ApiResponse> getEmployeesWorkingToday() {
        try {
            return ResponseEntity.ok(new ApiResponse("Found!", workDayHistoryService.getAllEmployeesWorkDayOn(LocalDate.now())));
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error:", e.getMessage()));
        } catch (NoContentException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // Returns 200/400/404/204/500
    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse> getEmployeeWorkDay(@PathVariable Long employeeId,
                                                          @RequestParam(required = false) LocalDate date,
                                                          @RequestParam(required = false) LocalDate startDate,
                                                          @RequestParam(required = false) LocalDate endDate) {
        try {
            if (date != null) {
                return ResponseEntity.ok(new ApiResponse("Found!", workDayHistoryService.getWorkDayByEmployeeOn(employeeId, date)));
            } else if (startDate != null && endDate != null) {
                return ResponseEntity.ok(new ApiResponse("Found!", workDayHistoryService.getAllWorkDaysByEmployeeOnRange(employeeId, startDate, endDate)));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error:", e.getMessage()));
        } catch (NoContentException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/housekeepers/today")
    public ResponseEntity<ApiResponse> getHousekeepersWorkingToday() {
        try {
            return ResponseEntity.ok(new ApiResponse("Found!", housekeeperWorkDayService.getHousekeepersWorkDayOn(LocalDate.now())));
        } catch (NoContentException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/housekeepers/{employeeId}")
    public ResponseEntity<ApiResponse> getHousekeeperWorkDay(@PathVariable Long employeeId,
                                                          @RequestParam(required = false) LocalDate date,
                                                          @RequestParam(required = false) LocalDate startDate,
                                                          @RequestParam(required = false) LocalDate endDate) {
        try {
            if (date != null) {
                return ResponseEntity.ok(new ApiResponse("Found!", housekeeperWorkDayService.getHousekeeperWorkDayOn(employeeId, date)));
            } else if (startDate != null && endDate != null) {
                return ResponseEntity.ok(new ApiResponse("Found!", housekeeperWorkDayService.getHousekeeperWorkDaysOnRange(employeeId, startDate, endDate)));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error:", e.getMessage()));
        } catch (NoContentException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
