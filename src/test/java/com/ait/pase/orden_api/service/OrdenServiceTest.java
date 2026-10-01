package com.ait.pase.orden_api.service;

import com.ait.pase.orden_api.entity.Orden;
import com.ait.pase.orden_api.entity.Status;
import com.ait.pase.orden_api.exception.OperationNotPermittedException;
import com.ait.pase.orden_api.exception.ResourceNotFoundException;
import com.ait.pase.orden_api.model.OrdenRequest;
import com.ait.pase.orden_api.model.OrdenResponse;
import com.ait.pase.orden_api.repository.OrdenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdenServiceTest {

    @Mock
    private OrdenRepository repository;

    @Mock
    private OrdenMapper mapper;

    @InjectMocks
    private OrdenService service;

    @Test
    void save_DeberiaGuardarOrden_CuandoTodoEsValido(){
        UUID ordenId = UUID.randomUUID();
        OrdenRequest request = new OrdenRequest("CDMX","Guadalajara");

        when(repository.save(any(Orden.class))).thenReturn( Orden.builder().id(ordenId).build() );

        UUID result = service.save(request);

        ArgumentCaptor<Orden> ordenCaptor = ArgumentCaptor.forClass(Orden.class);
        verify(repository).save( ordenCaptor.capture());
        assertEquals(ordenId, result);
        assertEquals(request.origin(), ordenCaptor.getValue().getOrigin() );
        assertEquals(request.destination(), ordenCaptor.getValue().getDestination() );
    }
    @Test
    void getById_DeberiaRegresarOrden_CuandoExisteId(){
        UUID ordenId = UUID.randomUUID();
        Orden orden = Orden.builder().id(ordenId).build();
        OrdenResponse ordenResponse = OrdenResponse.builder().id(ordenId).build();

        when( repository.findById(ordenId) ).thenReturn( Optional.of(orden) );
        when(mapper.apply(orden)).thenReturn(ordenResponse);

        OrdenResponse result = service.getById(ordenId);

        assertEquals(ordenResponse, result);
        verify(repository).findById(ordenId);
    }

    @Test
    void getById_DeberiaLanzarResourceNotFoundException_CuandoOrdenNoExiste(){
        UUID ordenId = UUID.randomUUID();

        when(repository.findById(ordenId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.getById(ordenId));

        assertTrue(exception.getMessage().contains("Orden no encontrada"));

        verify(mapper, never()).apply( any() );
    }

    @Test
    void findByStatus_DeberiaRegresarLista_CuandoExistenOrdenesConStatusCreated(){
        Orden orden1 = Orden.builder().origin("Origen1").destination("Destino1").status(Status.CREATED).build();
        Orden orden2 = Orden.builder().origin("Origen2").destination("Destino2").status(Status.CREATED).build();

        List<Orden> ordenes = List.of(orden1,orden2);

        OrdenResponse ordenResponse1 = OrdenResponse.builder().origin("Origen1").destination("Destino1").status(Status.CREATED).build();
        OrdenResponse ordenResponse2 = OrdenResponse.builder().origin("Origen2").destination("Destino2").status(Status.CREATED).build();

        List<OrdenResponse> ordenesResponse = List.of(ordenResponse1,ordenResponse2);

        when(mapper.apply(orden1)).thenReturn(ordenResponse1);
        when(mapper.apply(orden2)).thenReturn(ordenResponse2);

        when(repository.findByStatus(Status.CREATED)).thenReturn(ordenes);

        List<OrdenResponse> result = service.findByStatus(Status.CREATED);

        assertEquals(ordenesResponse, result);

        verify(repository).findByStatus(Status.CREATED);
    }
    private Orden ordenConStatus(Status status, UUID id) {
        Orden order = mock(Orden.class);
        when(order.getStatus()).thenReturn(status);
        when(order.getId()).thenReturn(id);
        return order;
    }
    @Test
    void changeStatus_DeberiaCambiarStatus_CuandoEstaEnCreatedAInTransit() {
        UUID orderId = UUID.randomUUID();
        Orden order = ordenConStatus(Status.CREATED, orderId);
        when(repository.findById(orderId)).thenReturn(Optional.of(order));

        UUID result = service.changeStatus(orderId, Status.IN_TRANSIT);

        assertEquals(orderId, result);
        verify(order).setStatus(Status.IN_TRANSIT);
        verify(repository).save(order);
    }

    @Test
    void changeStatus_DeberiaCambiarStatus_CuandoEstaEnInTransitADelivered() {
        UUID orderId = UUID.randomUUID();
        Orden order = ordenConStatus(Status.IN_TRANSIT, orderId);
        when(repository.findById(orderId)).thenReturn(Optional.of(order));

        UUID result = service.changeStatus(orderId, Status.DELIVERED);

        assertEquals(orderId, result);
        verify(order).setStatus(Status.DELIVERED);
        verify(repository).save(order);
    }

    @Test
    void changeStatus_DeberiaLanzarOperationNotPermittedException_CuandoEstaEnCreatedADelivered() {
        UUID orderId = UUID.randomUUID();
        Orden order = mock(Orden.class);
        when(order.getStatus()).thenReturn(Status.CREATED);
        when(repository.findById(orderId)).thenReturn(Optional.of(order));

        OperationNotPermittedException exception = assertThrows(OperationNotPermittedException.class,
                () -> service.changeStatus(orderId, Status.DELIVERED));

        assertTrue(exception.getMessage().contains("Del status CREATED solo se puede cambiar a IN_TRANSIT o CANCELLED"));
        verify(repository, never()).save(order);
    }

    @Test
    void changeStatus_DeberiaLanzarOperationNotPermittedException_CuandoEstaEnDeliveredACancelled() {
        UUID orderId = UUID.randomUUID();
        Orden order = mock(Orden.class);
        when(order.getStatus()).thenReturn(Status.DELIVERED);
        when(repository.findById(orderId)).thenReturn(Optional.of(order));

        OperationNotPermittedException exception = assertThrows(OperationNotPermittedException.class,
                () -> service.changeStatus(orderId, Status.CANCELLED));
        assertTrue(exception.getMessage().contains("La orden ya esta entregada, ya no se puede cambiar su status"));
        verify(repository, never()).save(order);
    }

    @Test
    void changeStatus_DeberiaLanzarResourceNotFoundException_CuandoOrdenNoExiste() {
        UUID orderId = UUID.randomUUID();
        when(repository.findById(orderId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.changeStatus(orderId, Status.IN_TRANSIT));

        assertTrue(exception.getMessage().contains("Orden no encontrada"));
        verify(repository, never()).save(any(Orden.class));
    }
}