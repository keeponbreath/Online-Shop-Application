package spring.cloud.purchaseservice.exception;

public class PurchaseException extends RuntimeException {
    public PurchaseException(String message) {
        super(message);
    }
}
