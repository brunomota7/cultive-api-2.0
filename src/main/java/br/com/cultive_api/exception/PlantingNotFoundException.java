package br.com.cultive_api.exception;

public class PlantingNotFoundException extends RuntimeException {
    public PlantingNotFoundException(String message) {
        super(message);
    }
}
