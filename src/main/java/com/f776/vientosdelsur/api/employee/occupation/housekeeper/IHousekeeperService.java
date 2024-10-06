package com.f776.vientosdelsur.api.employee.occupation.housekeeper;

import java.util.List;
import java.util.Optional;

public interface IHousekeeperService {
    List<Housekeeper> getAllHousekeepers();
    Optional<Housekeeper> getHousekeeperById(Long housekeeperId);
}
