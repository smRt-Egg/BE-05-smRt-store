package com.programmers.smrtstore.core.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
@Getter
public class JwtProperties {
    private final String header;
    private final String issuer;
    private final Key key;
    private final int expirySecond;

    public JwtProperties(@Value("jwt.header") String header,
                         @Value("jwt.issuer") String issuer,
                         @Value("jwt.client-secret") String clientSecret,
                         @Value("jwt.expiry-second") int expirySecond) {
        this.header = header;
        this.issuer = issuer;
        this.key = encodeKey(clientSecret);
        this.expirySecond = expirySecond;
    }

    private static Key encodeKey(String clientSecret) {
        return new SecretKeySpec(Base64.getDecoder().decode(clientSecret), "HmacSHA256");
    }
}
