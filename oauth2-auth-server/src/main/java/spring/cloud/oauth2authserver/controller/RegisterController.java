package spring.cloud.oauth2authserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.cloud.oauth2authserver.entity.Response;
import spring.cloud.oauth2authserver.entity.SecurityUser;
import spring.cloud.oauth2authserver.exception.ExceptionResponse;
import spring.cloud.oauth2authserver.service.SecurityUserService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
//@RequestMapping("/auth")
public class RegisterController {
    private final SecurityUserService service;

    @Autowired
    public RegisterController(SecurityUserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody SecurityUser user) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("User", service.save(user)))
                        .message("User created")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handle(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }
}