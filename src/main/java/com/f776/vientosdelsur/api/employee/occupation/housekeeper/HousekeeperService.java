package com.f776.vientosdelsur.api.employee.occupation.housekeeper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HousekeeperService implements IHousekeeperService {

    private final HousekeeperRepository housekeeperRepository;

    @Override
    public List<Housekeeper> getAllHousekeepers() {
        return housekeeperRepository.findAll();
    }

    @Override
    public Optional<Housekeeper> getHousekeeperById(Long housekeeperId) {
        return housekeeperRepository.findById(housekeeperId);
    }
}
