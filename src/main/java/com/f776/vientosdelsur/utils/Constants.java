package com.f776.vientosdelsur.utils;

import java.net.URI;
import java.util.function.Function;

public class Constants {

    private static final String EMPLOYEE_URI_PREFIX = "/api/v1/employees/";
    public static final Function<Long, URI> buildEmployeeURI = (id) -> URI.create(EMPLOYEE_URI_PREFIX + id);
}
