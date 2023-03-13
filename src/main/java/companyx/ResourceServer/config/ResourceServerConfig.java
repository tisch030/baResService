package companyx.ResourceServer.config;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Default security config, that configures a spring security filter chain which
 * requires all requests to contain an access token and a valid authentication.
 */
@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(@NonNull final HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests().anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}
