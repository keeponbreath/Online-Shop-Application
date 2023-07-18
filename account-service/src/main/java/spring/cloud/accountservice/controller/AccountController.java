package spring.cloud.accountservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.cloud.accountservice.dto.AccountRequest;
import spring.cloud.accountservice.entity.Account;
import spring.cloud.accountservice.entity.Response;
import spring.cloud.accountservice.service.AccountsService;
import spring.cloud.accountservice.util.ValidationUtil;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountsService service;

    @Autowired
    public AccountController(AccountsService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<Response> showAccounts() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("accounts", service.showAccounts()))
                        .message("Accounts loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> showAccount(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("account", service.showAccount(id)))
                        .message("Account loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/{id}/edit")
    public ResponseEntity<Response> editAccount(@PathVariable("id") Long id,
                                                @RequestBody @Valid AccountRequest accountRequest,
                                                BindingResult bindingResult,
                                                Authentication auth) throws IllegalAccessException {
        ValidationUtil.checkResult(bindingResult);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("edited", service.editAccount(id, accountRequest, auth)))
                        .message("Account edited")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/{id}/freeze")
    public ResponseEntity<Response> freezeAccount(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("frozen", service.freezeAccount(id)))
                        .message("Account frozen")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Response> deleteAccount(@PathVariable("id") Long id, Authentication auth) throws IllegalAccessException {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("deleted", service.deleteAccount(id, auth)))
                        .message("Account deleted")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/name")
    public String showFullName(@PathVariable("id") Long id) {
        Account account = service.showAccount(id);
        return account.getFirstName() + " " + account.getLastName();
    }

    @GetMapping("/check")
    public ResponseEntity<Long> check(Principal principal) {
        if(service.checkInfo(principal.getName())) {
            return new ResponseEntity<>(service.getAccountId(principal.getName()),
                    HttpStatus.valueOf(200));
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(201));
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<Double> checkBalance(Principal principal) {
        if(service.checkInfo(principal.getName())) {
            return new ResponseEntity<>(service.checkBalance(principal.getName()),
                    HttpStatus.valueOf(200));
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(201));
        }
    }
}