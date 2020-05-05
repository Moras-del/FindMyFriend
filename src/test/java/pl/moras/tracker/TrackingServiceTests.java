package pl.moras.tracker;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.moras.model.Location;
import pl.moras.model.LocationDto;
import pl.moras.model.User;
import pl.moras.repo.UserRepo;
import pl.moras.services.IAuthService;
import pl.moras.services.ITrackingService;
import pl.moras.services.TrackingService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TrackingServiceTests {

    private ITrackingService trackingService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private IAuthService authService;

    @Mock
    private Principal principal;

    @BeforeEach
    void setup(){
        initMocks(this);
        when(principal.getName()).thenReturn(currentUser().getName());
        when(authService.getUser(anyString())).thenAnswer(arg->{
            User user = userRepo.findByName(arg.getArgument(0)).get();
            user.setLastOnline(LocalDateTime.now());
            return user;
        });
        trackingService = new TrackingService(userRepo, authService);
    }

    @Test
    void should_get_online_friends(){
        User user = currentUser();
        User offlineFriend = friend();
        User onlineFriend = friend();

        onlineFriend.setTrackEnabled(true);

        user.addFriend(offlineFriend);
        user.addFriend(onlineFriend);

        when(userRepo.findOnlineFriends(user.getName()))
                .thenReturn(user.getFriends()
                                .stream()
                                .filter(User::isTrackEnabled)
                                .collect(Collectors.toList()));

        List<User> list = trackingService.getOnlineFriends(user.getName());
        assertTrue(list.contains(onlineFriend));
        assertEquals(1, list.size());
    }

    @Test
    void should_change_tracking_state_to_online(){
        User user = currentUser();
        when(userRepo.findByName(principal.getName())).thenReturn(Optional.of(user));
        trackingService.changeTrackingState(principal, true);
        assertTrue(user.isTrackEnabled());
        assertNotNull(user.getLastOnline());
    }

    @Test
    void should_update_location(){
        User user = currentUser();
        LocationDto locationDto = new LocationDto();
        locationDto.setLatitude(50);
        locationDto.setLongitude(100);

        when(userRepo.findByName(principal.getName())).thenReturn(Optional.of(user));
        trackingService.updateLocation(principal, locationDto);
        assertEquals(user.getLocation(), Location.of(50, 100));
        assertNotNull(user.getLastOnline());
    }



    private User currentUser(){
        User user = new User();
        user.setName("user");
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
