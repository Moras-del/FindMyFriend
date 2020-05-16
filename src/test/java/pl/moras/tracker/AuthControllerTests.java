package pl.moras.tracker;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.moras.fakes.TestConfig;
import pl.moras.tracker.controllers.AuthController;
import pl.moras.tracker.model.User;
import pl.moras.tracker.model.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(AuthController.class)
@ContextConfiguration(classes = TestConfig.class)
class AuthControllerTests {

    @Autowired
    private WebTestClient client;

    @Test
    void should_register() {
        UserDto userDto = new UserDto();
        userDto.setUsername("principal");
        userDto.setPassword("password");
        client.post()
                .uri("/auth")
                .bodyValue(userDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> assertEquals("principal", user.getName()))
                .value(user -> assertNull(user.getPassword()))
                .value(user -> assertNotNull(user.getLastOnline()));
    }

    @Test
    void should_throw_username_already_exists() {
        UserDto userDto = new UserDto();
        userDto.setUsername("user");
        userDto.setPassword("password");
        client.post()
                .uri("/auth")
                .bodyValue(userDto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .value(response -> assertEquals("user already exists", response));
    }

    @Test
    @WithMockUser(username = "user")
    void should_authenticate() {
        client.get()
                .uri("/auth")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> assertEquals("user", user.getName()))
                .value(user -> assertNull(user.getPassword()))
                .value(user -> assertNotNull(user.getLastOnline()));
    }

    @Test
    void should_get_unauthorized() {
        client.get()
                .uri("/auth")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @WithMockUser(username = "non-existing-user")
    void should_throw_username_not_found() {
        client.get()
                .uri("/auth")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(response -> assertEquals("non-existing-user not found", response));

    }
}
