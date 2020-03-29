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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        when(userRepo.findByName(currentUser().getName())).thenReturn(Optional.of(currentUser()));
        when(userRepo.findByName(randomUser().getName())).thenReturn(Optional.of(randomUser()));
        when(userRepo.findByName(friend().getName())).thenReturn(Optional.of(friend()));
        when(principal.getName()).thenReturn("user");
        friendsService = new FriendsService(userRepo);
    }

    @Test
    void should_send_request(){
        ResponseEntity responseEntity = friendsService.sendFriendRequest(principal, randomUser().getName());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void should_send_request_to_self(){
        ResponseEntity responseEntity = friendsService.sendFriendRequest(principal, principal.getName());
        assertEquals("Nie możesz zaprosić samego siebie", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void should_send_request_to_friend(){
        ResponseEntity responseEntity = friendsService.sendFriendRequest(principal, friend().getName());
        assertEquals("Użytkownik już jest Twoim znajomym", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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
        user.addFriend(currentUser());
        return user;
    }
}
