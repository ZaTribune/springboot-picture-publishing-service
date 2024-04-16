package zatribune.spring.pps;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

public class PasswordGenerationTest {

    @Test
    void test(){
        String currentId = "pbkdf2.2018";
        String secret = "secret";

        // List of all encoders we support. Old ones still need to be here for rolling updates
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put(currentId, new Pbkdf2PasswordEncoder(secret, 12, 181, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256));

        DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(currentId, encoders);

        System.out.println(encoder.encode("admin123"));
        System.out.println(encoder.encode("user123"));
    }
}
