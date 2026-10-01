package com.ait.pase.orden_api.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import com.ait.pase.orden_api.entity.Driver;
import com.ait.pase.orden_api.entity.Orden;
import com.ait.pase.orden_api.entity.Status;
import com.ait.pase.orden_api.exception.OperationNotPermittedException;
import com.ait.pase.orden_api.exception.ResourceNotFoundException;
import com.ait.pase.orden_api.repository.DriverRepository;
import com.ait.pase.orden_api.repository.OrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class AsignacionServiceTest {

    @Mock
    private OrdenRepository repository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private OrdenService service; 
    private UUID ordenId;
    private UUID driverId;
    private MultipartFile mockPdf;
    private MultipartFile mockImagen;

    @BeforeEach
    void setUp() {
        ordenId = UUID.randomUUID();
        driverId = UUID.randomUUID();
        mockPdf = new MockMultipartFile("pdf", "documento.pdf", "application/pdf", "contenido_pdf".getBytes());
        mockImagen = new MockMultipartFile("imagen", "foto.png", "image/png", "contenido_imagen".getBytes());
    }

    @Test
    void asignacion_DeberiaGuardarOrden_CuandoTodoEsValido() {
        // Arrange
        Orden ordenMock = Orden.builder().status(Status.CREATED).build();
        Driver driverMock = Driver.builder().active(true).build();

        when(repository.findById(ordenId)).thenReturn(Optional.of(ordenMock));
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driverMock));
        when(fileStorageService.save(mockPdf, ordenId.toString())).thenReturn("url_pdf");
        when(fileStorageService.save(mockImagen, ordenId.toString())).thenReturn("url_imagen");

        // Act
        service.asignacion(ordenId, driverId, mockPdf, mockImagen);

        // Assert
        assertEquals("url_pdf", ordenMock.getAsignacionArchivo());
        assertEquals("url_imagen", ordenMock.getAsignacionImagen());
        assertNotNull(ordenMock.getDriver());
        assertEquals(driverId, ordenMock.getDriver().getId());

        verify(repository, times(1)).save(ordenMock);
    }

    @Test
    void asignacion_DeberiaLanzarResourceNotFoundException_CuandoOrdenNoExiste() {
        // Arrange
        when(repository.findById(ordenId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.asignacion(ordenId, driverId, mockPdf, mockImagen);
        });

        assertTrue(exception.getMessage().contains("Orden no encontrada"));
        verify(repository, never()).save(any());
    }

    @Test
    void asignacion_DeberiaLanzarOperationNotPermittedException_CuandoStatusNoEsCreated() {
        // Arrange
        Orden ordenMock = Orden.builder().status(Status.DELIVERED).build();

        when(repository.findById(ordenId)).thenReturn(Optional.of(ordenMock));

        // Act & Assert
        OperationNotPermittedException exception = assertThrows(OperationNotPermittedException.class, () -> {
            service.asignacion(ordenId, driverId, mockPdf, mockImagen);
        });

        assertTrue(exception.getMessage().contains("solo esta permitido cuando la orden tiene status CREATED"));
        verify(driverRepository, never()).findById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void asignacion_DeberiaLanzarResourceNotFoundException_CuandoDriverNoExiste() {
        // Arrange
        Orden ordenMock = Orden.builder().status(Status.CREATED).build();

        when(repository.findById(ordenId)).thenReturn(Optional.of(ordenMock));
        when(driverRepository.findById(driverId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.asignacion(ordenId, driverId, mockPdf, mockImagen);
        });

        assertTrue(exception.getMessage().contains("Driver no encontrada"));
        verify(fileStorageService, never()).save(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    void asignacion_DeberiaLanzarOperationNotPermittedException_CuandoDriverEstaInactivo() {
        // Arrange
        Orden ordenMock = Orden.builder().status(Status.CREATED).build();
        Driver driverMock = Driver.builder().active(false).build(); // Inactivo

        when(repository.findById(ordenId)).thenReturn(Optional.of(ordenMock));
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driverMock));

        // Act & Assert
        OperationNotPermittedException exception = assertThrows(OperationNotPermittedException.class, () -> {
            service.asignacion(ordenId, driverId, mockPdf, mockImagen);
        });

        assertTrue(exception.getMessage().contains("solo esta permitido cuando el driver esta activo"));
        verify(fileStorageService, never()).save(any(), any());
        verify(repository, never()).save(any());
    }
}
