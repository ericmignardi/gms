package com.ericmignardi.gms;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JwtSecretGeneratorTest {

    @Test
    void generateSecret() {
        SecretKey secret = Jwts.SIG.HS512.key().build();
        String encodedSecret = DatatypeConverter.printHexBinary(secret.getEncoded());
        System.out.println(encodedSecret);
    }
}
