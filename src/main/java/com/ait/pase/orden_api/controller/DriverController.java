package com.ait.pase.orden_api.controller;

import com.ait.pase.orden_api.entity.Driver;
import com.ait.pase.orden_api.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("driver")
@RequiredArgsConstructor
@Tag(name = "driver API")
public class DriverController {
    private final DriverService service;
    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "crear un driver")
    public ResponseEntity<UUID> save(@Valid @RequestBody Driver driver) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save( driver ));
    }

    @GetMapping("/search/findByActiveIsTrue")
    @Operation(summary = "Buscar drivers que estan activos")
    public ResponseEntity<List<Driver>> findByActiveIsTrue() {
        return ResponseEntity.ok(service.findByActiveIsTrue());
    }
}
