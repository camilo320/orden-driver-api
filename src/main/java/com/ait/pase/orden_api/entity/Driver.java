package com.ait.pase.orden_api.entity;

import com.ait.pase.orden_api.model.DriverResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Value
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = PRIVATE)
public class Driver {
    @Id
    @GeneratedValue
    UUID id;
    String name;
    String licenseNumber;
    Boolean active = true;

    @OneToMany(mappedBy = "driver")
    private List<Orden> ordenes;
    public DriverResponse toResponse() {
        return DriverResponse.builder()
                .id(this.id)
                .name(this.name)
                .licenseNumber(this.licenseNumber)
                .active(this.active)
                .build();
    }
}
