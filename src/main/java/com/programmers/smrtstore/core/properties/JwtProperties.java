package com.programmers.smrtstore.core.properties;

import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtProperties {
    @Setter
    private String header;
    @Setter
    private String issuer;
    private String clientSecret;
    @Setter
    private int accessTokenExpiryHour;
    @Setter
    private int refreshTokenExpiryHour;

    public void setClientSecret(String clientSecret) {
        this.clientSecret = new SecretKeySpec(Base64.getDecoder().decode(clientSecret), "HmacSHA256").toString();
    }
}
