package zatribune.spring.pps.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import zatribune.spring.pps.data.entities.Role;
import zatribune.spring.pps.services.UserService;


import java.io.IOException;


@Slf4j
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.warn("filterChain(HttpSecurity http)");
        return http
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(config ->
                        config.authenticationEntryPoint(new AuthenticationEntryPoint()))  //remove default login page when accessing forbidden URL's on the browser
                .anonymous((anonymous) ->
                        anonymous
                                .authorities("ROLE_ANON")
                )
                .authorizeHttpRequests(config ->
                        config.requestMatchers("/", "/index", "/h2-console/**", "/webjars/**", "/images/favicon.ico"
                                        , "modal/{type}","/pics","/pics","/categories","/about"
                                        , "/login/**", "/exit").permitAll()
                                .requestMatchers(HttpMethod.GET, "/pic/image/{id}", "/images/**").permitAll()
                                .requestMatchers("/pics/add","/profile").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers("/pics/pending/**").hasAuthority(Role.ADMIN.name())
                                .anyRequest().authenticated())
                .formLogin(config -> config.failureHandler((request, response, exception) -> {
                    log.error("{}:{}", exception, "Login Failed!.");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    //exception.printStackTrace();
                    response.getWriter().write("Bad Credentials!");
                }).successHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                    response.setHeader("location", "/login/OK");

                }))
                .csrf(AbstractHttpConfigurer::disable)
                .logout(config -> config.logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            log.info("{}:{}", authentication, "Logout Successful.");
                            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                            response.setHeader("location", "/exit");
                        }))
                .headers(config -> config.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .build();//to allow using h2-console

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