package spring.cloud.accountservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NonNull;

@Data
public class AccountRequest {
    private String firstName;
    private String lastName;
    @Pattern(regexp = "\\+?[7|8]\\(?\\d{3}\\)?\\d{3}-?-?\\d{2}-?\\d{2}")
    // matches:
    // +7XXXXXXXXXX
    // +7(XXX)XXXXXXX
    // +7(XXX)XXX-XX-XX
    // +7(XXX)XXX-XXXX
    // +7(XXX)XXXXX-XX
    // 7XXXXXXXXXX
    // 7(XXX)XXXXXXX
    // 7(XXX)XXX-XX-XX
    // 7(XXX)XXX-XXXX
    // 7(XXX)XXXXX-XX
    // 8XXXXXXXXXX
    // 8(XXX)XXXXXXX
    // 8(XXX)XXX-XX-XX
    // 8(XXX)XXX-XXXX
    // 8(XXX)XXXXX-XX
    private String phoneNumber;
    @Email
    private String email;
}
