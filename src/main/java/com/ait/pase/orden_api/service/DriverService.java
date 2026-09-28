package com.ait.pase.orden_api.service;

import com.ait.pase.orden_api.entity.Driver;
import com.ait.pase.orden_api.model.DriverRequest;
import com.ait.pase.orden_api.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository repository;

    public UUID save(DriverRequest driverRequest){
        Driver driver = Driver.builder()
                .name(driverRequest.name())
                .licenseNumber(driverRequest.licenseNumber())
                .build();
        return repository.save(driver).getId();
    }
    public List<Driver> findByActiveIsTrue(){
        return repository.findByActiveIsTrue();
    }
}
