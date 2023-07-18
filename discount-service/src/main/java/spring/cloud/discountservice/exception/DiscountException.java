package spring.cloud.discountservice.exception;

public class DiscountException extends RuntimeException {
    public DiscountException(String message) {
        super(message);
    }
}
