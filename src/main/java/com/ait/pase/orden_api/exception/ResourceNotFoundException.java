package com.ait.pase.orden_api.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final String name;

    public ResourceNotFoundException(String name) {
        super("Recurso no encontrado: " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
