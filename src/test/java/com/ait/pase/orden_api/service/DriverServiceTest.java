package com.ait.pase.orden_api.service;

import com.ait.pase.orden_api.entity.Driver;
import com.ait.pase.orden_api.model.DriverRequest;
import com.ait.pase.orden_api.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository repository;

    @InjectMocks
    private DriverService service;

    @Test
    void save_DeberiaGuardarDriver_CuandoTodoEsValido() {
        UUID driverId = UUID.randomUUID();
        DriverRequest request = new DriverRequest("Juan Perez", "LIC-123");
        when(repository.save(org.mockito.ArgumentMatchers.any(Driver.class)))
                .thenReturn(Driver.builder().id(driverId).build());

        UUID result = service.save(request);

        ArgumentCaptor<Driver> driverCaptor = ArgumentCaptor.forClass(Driver.class);
        verify(repository).save(driverCaptor.capture());
        assertEquals(driverId, result);
        assertEquals(request.name(), driverCaptor.getValue().getName());
        assertEquals(request.licenseNumber(), driverCaptor.getValue().getLicenseNumber());
    }

    @Test
    void findByActiveIsTrue_DeberiaRegresarLista_CuandoHayDriversActive() {
        List<Driver> drivers = List.of( Driver.builder().name("Juan Perez").active(true).build() );
        when(repository.findByActiveIsTrue()).thenReturn(drivers);

        List<Driver> result = service.findByActiveIsTrue();

        assertEquals(drivers, result);
        verify(repository).findByActiveIsTrue();
    }
}