package ru.lipnin.itmohomework.security.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import ru.lipnin.itmohomework.security.model.CustomUserDetails;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtSecurityService {

    private final WebApplicationContext webApplicationContext;

    @Value("${security.jwtSecret}")
    private String jwtSecret;
    @Value("${security.jwtRefreshSecret}")
    private String jwtRefreshSecret;
    @Value("${security.jwtSecretExpiration}")
    private long jwtSecretExpiration;


    public JwtSecurityService(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    private JWEHeader getHeader() {
        return new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM);
    }

    private Payload getPayload(String subject, String email) {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(subject) // идентификатор пользователя subject
                .claim("email", email)
                .issueTime(new Date()) // время выдачи токена
                .expirationTime(new Date(System.currentTimeMillis() + jwtSecretExpiration))
                .build();

        return new Payload(claims.toJSONObject());
    }

    private void encrypt(JWEObject jweObject) throws JOSEException {
        jweObject.encrypt((JWEEncrypter) webApplicationContext.getBean("JWEEncrypter"));
    }

    public String generateToken(CustomUserDetails user) throws JOSEException {
        JWEObject jweObject = new JWEObject(
                getHeader(),
                getPayload(
                        user.getUsername(),
                        user.getEmail()
                ));

        encrypt(jweObject);
        return jweObject.serialize();
    }

    @Bean("JWEEncrypter")
    public JWEEncrypter encrypter() throws KeyLengthException {
        return new DirectEncrypter(jwtSecret.getBytes());
    }

    @Bean("JWTProcessor")
    public ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor() {
        ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWKSource<SimpleSecurityContext> jweKeySource = new ImmutableSecret<>(jwtSecret.getBytes());
        JWEKeySelector<SimpleSecurityContext> jweKeySelector =
                new JWEDecryptionKeySelector<>(JWEAlgorithm.DIR, EncryptionMethod.A128GCM, jweKeySource);
        jwtProcessor.setJWEKeySelector(jweKeySelector);
        return jwtProcessor;
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    private JWTClaimsSet extractClaims(String token) throws BadJOSEException, ParseException, JOSEException {
        ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor =
                (ConfigurableJWTProcessor<SimpleSecurityContext>) webApplicationContext.getBean("JWTProcessor");
        return jwtProcessor.process(token, null);
    }

    public String getSubject(String token) throws BadJOSEException, ParseException, JOSEException {
        JWTClaimsSet claims = extractClaims(token);
        return claims.getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws BadJOSEException, ParseException, JOSEException {
        String userName = getSubject(token);
        Date expiration = extractClaims(token).getExpirationTime();
        return userName.equals(userDetails.getUsername()) && expiration.after(new Date());
    }

    public String getStringClaimsByKey(String token, String claimsKey) throws BadJOSEException, ParseException, JOSEException {
        JWTClaimsSet claims = extractClaims(token);
        return claims.getStringClaim(claimsKey);
    }

}