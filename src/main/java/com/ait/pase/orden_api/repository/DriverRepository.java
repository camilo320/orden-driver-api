package com.ait.pase.orden_api.repository;

import com.ait.pase.orden_api.entity.Driver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface DriverRepository extends PagingAndSortingRepository<Driver, UUID>, CrudRepository<Driver, UUID> {
    List<Driver> findByActiveIsTrue();
}
