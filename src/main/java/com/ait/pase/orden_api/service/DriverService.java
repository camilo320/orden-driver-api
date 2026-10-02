package com.ait.pase.orden_api.service;

import com.ait.pase.orden_api.entity.Driver;
import com.ait.pase.orden_api.model.DriverRequest;
import com.ait.pase.orden_api.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {
    private final DriverRepository repository;

    public UUID save(DriverRequest driverRequest){
        log.info("Guardando Driver {} {}",
                kv("nombre", driverRequest.name()),
                kv("licenseNumber",driverRequest.licenseNumber()));
        Driver driver = Driver.builder()
                .name(driverRequest.name())
                .licenseNumber(driverRequest.licenseNumber())
                .active(true)
                .build();
        return repository.save(driver).getId();
    }
    public List<Driver> findByActiveIsTrue(){
        log.info("Buscando drivers activos");
        return repository.findByActiveIsTrue();
    }
}
