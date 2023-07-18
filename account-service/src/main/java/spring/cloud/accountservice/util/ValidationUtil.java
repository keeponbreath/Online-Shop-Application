package spring.cloud.accountservice.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import spring.cloud.accountservice.exception.AccountException;

import java.util.List;

public class ValidationUtil {
    public static void checkResult(BindingResult result) {
        if(result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> fieldErrors = result.getFieldErrors();

            for(FieldError error : fieldErrors) {
                errorMessage.append(error.getField())
                            .append(" - ")
                            .append(error.getDefaultMessage())
                            .append(";");
            }

            throw new AccountException(errorMessage.toString());
        }
    }
}