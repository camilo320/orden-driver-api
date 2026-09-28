package com.ait.pase.orden_api.entity;


import com.ait.pase.orden_api.model.OrdenDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Data
@NoArgsConstructor(force = true, access = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Orden {
    @Id
    @GeneratedValue
    private UUID id;

    private String origin;

    private String destination;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonIgnore
    private Driver driver;
    private String AsignacionArchivo;
    private String AsignacionImagen;

    public static Orden fromCreate(OrdenDTO dto) {
        Orden orden = new Orden();
        orden.setId(dto.id());
        orden.setOrigin(dto.origin());
        orden.setDestination(dto.destination());
        orden.setStatus(dto.status());
        return orden;
    }
}
