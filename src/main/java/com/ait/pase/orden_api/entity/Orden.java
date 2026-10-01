package com.ait.pase.orden_api.entity;

import com.ait.pase.orden_api.model.OrdenRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public static Orden from(OrdenRequest ordenRequest) {
        return Orden.builder().origin(ordenRequest.origin()).destination(ordenRequest.destination()).build();
    }
}
