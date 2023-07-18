package spring.cloud.oauth2authserver.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authorization {

    @Id
    @Column
    private String id;
    private String registeredClientId;
    private String principalName;
    private String authorizationGrantType;
    @Column(length = 1000)
    private String authorizedScopes;
    @Column(length = 4000, columnDefinition = "text")
    private String attributes;
    @Column(length = 500)
    private String state;
    @Column(length = 4000, columnDefinition = "text")
    private String authorizationCodeValue;
    private Instant authorizationCodeIssuedAt;
    private Instant authorizationCodeExpiresAt;
    private String authorizationCodeMetadata;
    @Column(length = 4000, columnDefinition = "text")
    private String accessTokenValue;
    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;
    @Column(length = 2000, columnDefinition = "text")
    private String accessTokenMetadata;
    private String accessTokenType;
    @Column(length = 1000)
    private String accessTokenScopes;
    @Column(length = 4000, columnDefinition = "text")
    private String refreshTokenValue;
    private Instant refreshTokenIssuedAt;
    private Instant refreshTokenExpiresAt;
    @Column(length = 2000, columnDefinition = "text")
    private String refreshTokenMetadata;
    @Column(length = 4000, columnDefinition = "text")
    private String oidcIdTokenValue;
    private Instant oidcIdTokenIssuedAt;
    private Instant oidcIdTokenExpiresAt;
    @Column(length = 2000, columnDefinition = "text")
    private String oidcIdTokenMetadata;
    @Column(length = 2000, columnDefinition = "text")
    private String oidcIdTokenClaims;
}