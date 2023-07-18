package spring.cloud.commentsevice.exception;

public class CommentException extends RuntimeException {
    public CommentException(String message) {
        super(message);
    }
}
