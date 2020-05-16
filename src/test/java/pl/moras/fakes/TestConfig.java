package pl.moras.fakes;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import pl.moras.tracker.repo.MongoDao;
import pl.moras.tracker.services.*;

@TestConfiguration
public class TestConfig {


    @Bean
    MongoDao userRepo() {
        return new MongoDaoTestImpl();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new PasswordEncoderTestImpl();
    }

    @Bean
    IAuthService authService() {
        return new AuthService(userRepo(), passwordEncoder());
    }

    @Bean
    IFriendsService friendsService() {
        return new FriendsService(userRepo());
    }

    @Bean
    ITrackingService trackingService() {
        return new TrackingService(userRepo());
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/auth").permitAll()
                .anyExchange().authenticated()
                .and()
                .csrf().disable()
                .build();
    }

    @Bean
    MapReactiveUserDetailsService userDetailsService() {
        UserDetails userDetails = org.springframework.security.core.userdetails
                .User
                .builder()
                .authorities("USER")
                .username("user")
                .password("password")
                .build();
        return new MapReactiveUserDetailsService(userDetails);
    }

}
