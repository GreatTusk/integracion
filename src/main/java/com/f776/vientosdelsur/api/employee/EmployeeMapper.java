package com.f776.vientosdelsur.api.employee;

import com.f776.vientosdelsur.utils.Utils;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Function;

@Service
public class EmployeeMapper implements Function<Employee, EmployeeDTO> {
    @Override
    public EmployeeDTO apply(Employee employee) {
        DayOfWeek dayOff = employee.getDayOff();
        String day = dayOff != null ?
                dayOff.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")) : "no aplica";

        return EmployeeDTO
                .builder()
                .id(employee.getId())
                .firstName(Utils.capitalize(employee.getFirstName()))
                .lastName(Utils.capitalize(employee.getLastName()))
                .phoneNumber(employee.getPhoneNumber())
                .entryDate(employee.getEntryDate())
                .dayOff(Utils.capitalize(day))
                .occupation(Utils.initCap(employee.getOccupation().toString().replace('_', ' ')))
                .build();
    }
}
