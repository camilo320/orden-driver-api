package com.ait.pase.orden_api.controller;

import com.ait.pase.orden_api.model.OrdenResponse;
import com.ait.pase.orden_api.entity.Status;
import com.ait.pase.orden_api.model.OrdenRequest;
import com.ait.pase.orden_api.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("orden")
@RequiredArgsConstructor
@Tag(name = "Orden API")
public class OrdenController {
    private final OrdenService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "crear una orden")
    public ResponseEntity<UUID> save(@Valid @RequestBody OrdenRequest orden) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save( orden ));
    }

    @PatchMapping("/changeStatus/{id}")
    public ResponseEntity<UUID> changeStatus( @PathVariable("id") UUID id, @RequestParam("status") Status status ) {
        return ResponseEntity.ok(service.changeStatus(id,status));
    }
    /*
    @GetMapping
    @Operation(summary = "Obtiene todas las ordenes")
    public ResponseEntity<List<OrdenResponse>> findAll() {
        return ResponseEntity.ok(service.getAll());
    }
    */
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene orden por ID")
    public ResponseEntity<OrdenResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/search/findByStatus")
    @Operation(summary = "Buscar ordenes por status")
    public ResponseEntity<List<OrdenResponse>> findBySize(@RequestParam("status") Status status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }

    @GetMapping("/search/findByOrigin")
    @Operation(summary = "Buscar ordenes por origin")
    public ResponseEntity<List<OrdenResponse>> findByOrigin(@RequestParam("origin") String origin) {
        return ResponseEntity.ok(service.findByOrigin(origin));
    }

    @GetMapping("/search/findByDestination")
    @Operation(summary = "Buscar ordenes por destination")
    public ResponseEntity<List<OrdenResponse>> findByDestination(@RequestParam("destination") String destination) {
        return ResponseEntity.ok(service.findByDestination(destination));
    }

    @GetMapping("/search/findByCreatedAtBetween")
    @Operation(summary = "Buscar ordenes entre fechas")
    public ResponseEntity<List<OrdenResponse>> findByCreatedAtBetween(
            @RequestParam("to")  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime to,
            @RequestParam("from")  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  LocalDateTime from) {
        return ResponseEntity.ok(service.findByCreatedAtBetween(to, from));
    }
    /*
    @PutMapping("/{id}")
    @Operation(summary = "Modificar una orden")
    public ResponseEntity<UUID> editar(@Valid @RequestBody OrdenResponse dto, @PathVariable UUID id) {
        Orden orden = Orden.fromCreate( service.getById(id) );
        orden.setOrigin(dto.origin());
        orden.setDestination(dto.destination());
        return ResponseEntity.status(CREATED).body(service.save( orden ));
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Borrar todas las ordenes")
    public void deleteAll() {
        service.deleteAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Borrar una orden por medio de su ID ")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }
    */
    @PostMapping(value = "/asignacion/{orden-id}/{driver-id}", consumes = "multipart/form-data")
    @Operation(summary = "asignacion de una orden a un driver con documentos adjuntos")
    public ResponseEntity<?> asignacion(
            @PathVariable("orden-id") UUID idOrden,
            @PathVariable("driver-id") UUID idDriver,
            @Parameter()
            @RequestPart("pdf") MultipartFile pdf,
            @Parameter()
            @RequestPart("imagen") MultipartFile imagen
    ) {
        service.asignacion(idOrden,idDriver,pdf,imagen);
        return ResponseEntity.accepted().build();
    }
}
