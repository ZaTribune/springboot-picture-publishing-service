package zatribune.spring.pps.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import zatribune.spring.pps.data.repositories.RoleRepository;
import zatribune.spring.pps.data.repositories.UserRepository;
import zatribune.spring.pps.services.UserDetailsServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Autowired
    public SecurityConfig(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    // Create source of Authentication manager
    //then it will call the two methods to create the user service & the encoder

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.warn("passwordEncoder()");
        // This is the ID we use for encoding.
        String currentId = "pbkdf2.2018";
        String secret="secret";
        // List of all encoders we support. Old ones still need to be here for rolling updates
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put(currentId, new Pbkdf2PasswordEncoder(secret, 12, 181));
        return new DelegatingPasswordEncoder(currentId, encoders);
    }

    // Specify the manager
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        log.warn("authenticationManagerBean()");
        //will call the builder if not initialized
        //so we'll specify a builder of our own
        return authenticationManager();
    }

    //the manager builder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.warn("configure(AuthenticationManagerBuilder auth)");
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //because it depends on injected beans
        log.warn("userDetailsService()");
        return new UserDetailsServiceImpl(userRepository, roleRepository, passwordEncoder());
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
        public AuthenticationEntryPoint()  {
            super("");
        }

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            //todo: some user-friendly page
            response.sendError(401, "Unauthorized");
        }
    }

}