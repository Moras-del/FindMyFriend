package pl.moras.tracker;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.moras.fakes.UserRepoTestImpl;
import pl.moras.tracker.model.User;
import pl.moras.tracker.repo.UserRepo;
import pl.moras.tracker.services.FriendsService;
import pl.moras.tracker.services.IFriendsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class FriendServiceTests {

    private UserRepo userRepo = new UserRepoTestImpl();

    private IFriendsService friendsService = new FriendsService(userRepo);

    @Test
    void should_send_request(){
        ResponseEntity responseEntity = friendsService.sendFriendRequest("sender", "receiver").block();

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void should_send_request_to_self(){
        ResponseEntity responseEntity = friendsService.sendFriendRequest("sender", "sender").block();

        assertEquals("Nie możesz zaprosić samego siebie", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void should_send_request_to_friend(){
        ResponseEntity responseEntity = friendsService.sendFriendRequest("sender", "friend").block();
        assertEquals("Użytkownik już jest Twoim znajomym", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void should_accept_request(){
        User friend = friendsService.acceptRequest("receiver", "sender").block();
        User user = (User) friend.getFriends().toArray()[0];
        assertEquals("sender", friend.getName());
        assertEquals("receiver", user.getName());
        assertTrue(user.getFriends().contains(friend));
    }

    @Test
    void should_cancel_request(){
        User user = friendsService.cancelRequest("receiver", "sender").block();
        assertTrue(user.getFriendRequests().isEmpty());
    }

    @Test
    void should_delete_friend(){
        User user = friendsService.deleteFriend("username", "friend").block();
        assertTrue(user.getFriends().isEmpty());
    }
}
