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

/**
 * <h3>How does <u>BCrypt</u> relates to <u>pbkdf2</u> (Password-Based Key Derivation Function 2)?</h3>
 * <ul>
 * <li>PBKDF2 prevents password cracking tools from making the best use of graphics processing units (GPUs),
 * which reduces guess rates from hundreds of thousands of guesses per second, to less than a few tens of
 * thousands of guesses per second.</li>
 * <li>PBKDF2 applies a pseudorandom function, such as hash-based message authentication code (HMAC),
 * to the input password or passphrase along with a salt value and repeats the process many times
 * to produce a derived key, which can then be used as a cryptographic key in subsequent operations.</li>
 * </ul>
 * </p>
 * </br></br>
 * <b>How does bcrypt algorithm work?</b>
 * <ul>
 * <li>The problems present in traditional UNIX password hashes led naturally to a new password scheme
 * which we call bcrypt, referring to the Blowfish encryption algorithm.</li>
 * <li>Bcrypt uses a 128-bit salt and encrypts a 192-bit magic value.</li>
 * <li>It takes advantage of the expensive key setup in eksblowfish algorithm.</li>
 * </ul>
 **/

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
        encoders.put(currentId, new Pbkdf2PasswordEncoder(secret, 12, 181, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256));
        return new DelegatingPasswordEncoder(currentId, encoders);
    }
}
