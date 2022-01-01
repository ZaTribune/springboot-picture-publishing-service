package zatribune.spring.pps.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class EncodersConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.warn("passwordEncoder()");
        // This is the ID we use for encoding.
        String currentId = "pbkdf2.2018";
        String secret = "secret";
        // List of all encoders we support. Old ones still need to be here for rolling updates
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        //didn't use salt
        encoders.put(currentId, new Pbkdf2PasswordEncoder(secret,12, 181));
        return new DelegatingPasswordEncoder(currentId, encoders);
    }
}
