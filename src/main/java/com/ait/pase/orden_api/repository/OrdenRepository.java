package com.ait.pase.orden_api.repository;

import com.ait.pase.orden_api.entity.Orden;
import com.ait.pase.orden_api.entity.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrdenRepository extends PagingAndSortingRepository<Orden, UUID>, CrudRepository<Orden, UUID> {

    List<Orden> findByStatus(Status status);
    List<Orden> findByOrigin(String origin);
    List<Orden> findByDestination(String destination);
    List<Orden> findByCreatedAtBetween(LocalDateTime to, LocalDateTime from);
}
