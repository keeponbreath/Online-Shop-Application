package spring.cloud.accountservice.exception;

public class AccountException extends RuntimeException {
    public AccountException(String message) {
        super(message);
    }
}
