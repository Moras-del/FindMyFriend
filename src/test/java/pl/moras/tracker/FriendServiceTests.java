package pl.moras.tracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import pl.moras.fakes.UserRepoTestImpl;
import pl.moras.model.User;
import pl.moras.repo.UserRepo;
import pl.moras.services.FriendsService;
import pl.moras.services.IFriendsService;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class FriendServiceTests {

    private UserRepo userRepo = new UserRepoTestImpl();

    private IFriendsService friendsService = new FriendsService(userRepo);

    @Test
    void should_send_request(){
        ResponseEntity responseEntity = friendsService.sendFriendRequest("sender", "receiver");

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void should_send_request_to_self(){
        ResponseEntity responseEntity = friendsService.sendFriendRequest("sender", "sender");

        assertEquals("Nie możesz zaprosić samego siebie", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void should_send_request_to_friend(){
        ResponseEntity responseEntity = friendsService.sendFriendRequest("sender", "friend");
        assertEquals("Użytkownik już jest Twoim znajomym", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void should_accept_request(){
        User friend = friendsService.acceptRequest("receiver", "sender");
        User user = (User) friend.getFriends().toArray()[0];
        assertEquals("sender", friend.getName());
        assertEquals("receiver", user.getName());
        assertTrue(user.getFriends().contains(friend));
    }

    @Test
    void should_cancel_request(){
        User user = friendsService.cancelRequest("receiver", "sender");
        assertTrue(user.getFriendRequests().isEmpty());
    }

    @Test
    void should_delete_friend(){
        User user = friendsService.deleteFriend("username", "friend");
        assertTrue(user.getFriends().isEmpty());
    }
}
