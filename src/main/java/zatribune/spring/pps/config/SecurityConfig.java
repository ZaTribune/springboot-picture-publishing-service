package zatribune.spring.pps.config;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import zatribune.spring.pps.data.entities.Role;
import zatribune.spring.pps.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    @Bean
    public DaoAuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    // Create source of Authentication manager
    //then it will call the two methods to create the user service & the encoder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }


    //Expose the AuthenticationManager
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.warn("configure(HttpSecurity http)");
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint())//remove default login page when accessing forbidden URL's on the browser
                .and().anonymous()
                .and()
                .authorizeRequests().antMatchers("/pics/pending/**").hasRole(Role.ADMIN.name())
                .and()
                .formLogin()
                .failureHandler((request, response, exception) -> {
                    log.error("{}:{}", exception, "Login Failed!.");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    //exception.printStackTrace();
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