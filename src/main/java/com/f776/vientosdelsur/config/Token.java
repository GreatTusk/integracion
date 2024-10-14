package com.f776.vientosdelsur.config;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Token {
    private String access;
    private String refresh;
}
