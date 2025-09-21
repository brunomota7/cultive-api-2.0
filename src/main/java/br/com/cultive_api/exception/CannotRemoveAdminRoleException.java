package br.com.cultive_api.exception;

public class CannotRemoveAdminRoleException extends RuntimeException {
    public CannotRemoveAdminRoleException(String message) {
        super(message);
    }
}
