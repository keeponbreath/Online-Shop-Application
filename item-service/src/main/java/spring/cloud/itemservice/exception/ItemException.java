package spring.cloud.itemservice.exception;

public class ItemException extends RuntimeException {
    public ItemException(String message) {
        super(message);
    }
}