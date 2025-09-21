package br.com.cultive_api.exception;

public class CannotDeleteAdminException extends RuntimeException {
    public CannotDeleteAdminException(String message) {
        super(message);
    }
}
