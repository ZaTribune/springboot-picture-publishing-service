package zatribune.spring.pps.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import zatribune.spring.pps.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        super();
        this.userService = userService;
    }

    // Create source of Authentication manager
    //then it will call the two methods to create the user service & the encoder

    /**
     * Password-Based Key Derivation Function 2
     * PBKDF2 prevents password cracking tools from making the best use of graphics processing units (GPUs),
     * which reduces guess rates from hundreds of thousands of guesses per second, to less than a few tens of
     * thousands of guesses per second.
     * PBKDF2 applies a pseudorandom function, such as hash-based message authentication code (HMAC),
     * to the input password or passphrase along with a salt value and repeats the process many times
     * to produce a derived key, which can then be used as a cryptographic key in subsequent operations.
     *
     * How does bcrypt algorithm work?
     * The problems present in traditional UNIX password hashes led naturally to a new password scheme
     * which we call bcrypt, referring to the Blowfish encryption algorithm.
     * Bcrypt uses a 128-bit salt and encrypts a 192-bit magic value.
     * It takes advantage of the expensive key setup in eksblowfish.
     *
     **/

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //the manager builder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.warn("configure(AuthenticationManagerBuilder auth)");
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.warn("configure(HttpSecurity http)");
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint())//remove default login page when accessing forbidden URL's on the browser
                .and().anonymous()
                .and()
                .authorizeRequests().antMatchers("/pics/pending/**").hasRole("ADMIN")
                .and()
                .formLogin()
                .failureHandler((request, response, exception) -> {
                    log.error("{}:{}", exception, "Login Failed!.");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    exception.printStackTrace();
                    response.getWriter().write("Bad Credentials!");
                })
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                    response.setHeader("location", "/login/OK");

                }).and().csrf().disable()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    log.info("{}:{}", authentication, "Logout Successful.");
                    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                    response.setHeader("location", "/exit");
                });
        http.headers().frameOptions().disable();//to allow using h2-console

    }

    static class AuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
        public AuthenticationEntryPoint() {
            super("");
        }

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            //todo: some user-friendly page
            response.sendError(401, "Unauthorized");
        }
    }

}