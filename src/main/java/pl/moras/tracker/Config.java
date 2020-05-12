package pl.moras.tracker;


import org.neo4j.springframework.data.repository.config.EnableReactiveNeo4jRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import pl.moras.tracker.model.User;
import pl.moras.tracker.repo.UserRepo;

import java.util.HashMap;
import java.util.Map;

//import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveNeo4jRepositories(basePackageClasses = UserRepo.class)
@EntityScan(basePackageClasses = User.class)
public class Config {

    @Autowired
    WebSocketHandler webSocketHandler;
    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/auth").permitAll()
                .anyExchange().permitAll()
                .and()
                .httpBasic().authenticationEntryPoint(new HttpBasicServerAuthenticationEntryPoint())
                .and()
                .csrf().disable()
                .build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(16);
    }

    @Bean
    HandlerMapping webSocketHandlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/tracker", webSocketHandler);
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }

    @Bean
    HandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

}
