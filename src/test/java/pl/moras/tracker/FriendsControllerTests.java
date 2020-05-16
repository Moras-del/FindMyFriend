package pl.moras.tracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.moras.fakes.TestConfig;
import pl.moras.tracker.controllers.FriendsController;
import pl.moras.tracker.model.User;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(FriendsController.class)
@ContextConfiguration(classes = TestConfig.class)
class FriendsControllerTests {

    @Autowired
    private WebTestClient client;

    @Test
    @WithMockUser("principal")
    void should_send_request() {
        client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/friends/request")
                        .queryParam("friendName", "other")
                        .build())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser("principal")
    void should_send_fail_request() {
        client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/friends/request")
                        .queryParam("friendName", "principal")
                        .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(response -> assertEquals("You can't add yourself", response));
    }

    @Test
    @WithMockUser(username = "user")
    void should_accept_friend_request() {
        client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/friends/accept")
                        .queryParam("friendName", "other")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> assertEquals("user", user.getName()))
                .value(user -> assertTrue(user.hasAnyFriends()));
    }

    @Test
    @WithMockUser(username = "user")
    void should_cancel_friend_request() {
        client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/friends/cancel")
                        .queryParam("friendName", "other")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> assertEquals("user", user.getName()))
                .value(user -> assertFalse(user.hasAnyFriendRequest()));
    }

    @Test
    @WithMockUser(username = "user")
    void should_delete_friend() {
        client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/friends/delete")
                        .queryParam("friendName", "other")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> assertEquals("user", user.getName()))
                .value(user -> assertFalse(user.hasAnyFriends()));
    }


}
