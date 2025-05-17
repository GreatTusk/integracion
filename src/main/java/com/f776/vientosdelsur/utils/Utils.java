package com.f776.vientosdelsur.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {
    public static String initCap(String message) {
        if (message == null || message.isBlank()) {
            return message;
        }

        return Arrays.stream(message.trim().split(" "))
                .map(word -> {
                    if (word.length() > 1) {
                        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                    }
                    return word.toUpperCase();
                })
                .collect(Collectors.joining(" "));

    }
}
