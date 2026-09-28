package com.ait.pase.orden_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = PRIVATE)
public class Driver {
    @Id
    @GeneratedValue
    UUID id;
    @NotNull(message = "No puede ser nulo")
    @NotEmpty(message = "No puede estar vacio")
    String name;
    @NotNull(message = "No puede ser nulo")
    @NotEmpty(message = "No puede estar vacio")
    String licenseNumber;

    Boolean active = true;
}
