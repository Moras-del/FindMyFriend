package pl.moras.tracker;

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

    @Mock
    private Principal principal;

    @Mock
    private UserRepo userRepo;

    private IFriendsService friendsService;

    @BeforeEach
    void setup(){
        initMocks(this);
        when(userRepo.save(any(User.class))).thenAnswer(arg->arg.getArgument(0));
        when(userRepo.findByName(randomUser().getName())).thenReturn(Optional.of(randomUser()));
        when(userRepo.findByName(friend().getName())).thenReturn(Optional.of(friend()));
        when(principal.getName()).thenReturn(currentUser().getName());
        friendsService = new FriendsService(userRepo);
    }

    @Test
    void should_send_request(){
        when(userRepo.findByName(principal.getName())).thenReturn(Optional.of(currentUser()));

        ResponseEntity responseEntity = friendsService.sendFriendRequest(principal, randomUser().getName());

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void should_send_request_to_self(){
        when(userRepo.findByName(principal.getName())).thenReturn(Optional.of(currentUser()));

        ResponseEntity responseEntity = friendsService.sendFriendRequest(principal, principal.getName());

        assertEquals("Nie możesz zaprosić samego siebie", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void should_send_request_to_friend(){
        User user = currentUser();
        User friend = friend();
        user.addFriend(friend);

        when(userRepo.findByName(principal.getName())).thenReturn(Optional.of(user));
        when(userRepo.findByName(friend.getName())).thenReturn(Optional.of(friend));

        ResponseEntity responseEntity = friendsService.sendFriendRequest(principal, friend.getName());

        assertEquals("Użytkownik już jest Twoim znajomym", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void should_accept_request(){
        User user = currentUser();
        User requestedUser = randomUser();
        user.addFriendRequest(requestedUser);

        when(userRepo.findByName(principal.getName())).thenReturn(Optional.of(user));
        when(userRepo.findByName(requestedUser.getName())).thenReturn(Optional.of(requestedUser));

        User result = friendsService.acceptRequest(principal, requestedUser.getName());

        assertTrue(result.getFriendRequest().isEmpty());
        assertTrue(result.getFriends().contains(requestedUser));
        assertTrue(requestedUser.getFriends().contains(result));

    }

    @Test
    void should_cancel_request(){
        User user = currentUser();
        User requestedUser = randomUser();
        user.addFriendRequest(requestedUser);

        when(userRepo.findByName(principal.getName())).thenReturn(Optional.of(user));
        when(userRepo.findByName(requestedUser.getName())).thenReturn(Optional.of(requestedUser));

        ResponseEntity responseEntity = friendsService.cancelRequest(principal, requestedUser.getName());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(user.getFriendRequest().isEmpty());
        assertNull(user.getFriends());
    }

    @Test
    void should_delete_friend(){
        User user = currentUser();
        User friend = friend();
        user.addFriend(friend);

        when(userRepo.findByName(principal.getName())).thenReturn(Optional.of(user));
        when(userRepo.findByName(friend.getName())).thenReturn(Optional.of(friend));

        ResponseEntity responseEntity = friendsService.deleteFriend(principal, friend.getName());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(user.getFriends().isEmpty());
    }

    private User currentUser(){
        User user = new User();
        user.setName("user");
        user.setPassword("haslo");
        return user;
    }

    private User randomUser(){
        User user = new User();
        user.setName("randomUser");
        user.setPassword("haslo");
        return user;
    }

    private User friend(){
        User user = new User();
        user.setName("friend");
        user.setPassword("haslo");
        return user;
    }
}
