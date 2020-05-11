package pl.moras.tracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.moras.fakes.UserRepoTestImpl;
import pl.moras.model.Location;
import pl.moras.model.LocationDto;
import pl.moras.model.User;
import pl.moras.repo.UserRepo;
import pl.moras.services.ITrackingService;
import pl.moras.services.TrackingService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TrackingServiceTests {

    private UserRepo userRepo = new UserRepoTestImpl();
    private ITrackingService trackingService = new TrackingService(userRepo);

    @Test
    void should_get_online_friends(){
        List<User> onlineFriends = trackingService.getOnlineFriends("user");
        assertFalse(onlineFriends.isEmpty());
        assertTrue(onlineFriends.get(0).isTrackEnabled());
    }

    @Test
    void should_change_tracking_state_to_online(){
        User user = getUser();
        trackingService.enableTracking(user);
        assertTrue(user.isTrackEnabled());
    }

    @Test
    void should_change_tracking_state_to_offline(){
        User user = getUser();
        trackingService.disableTracking(user);
        assertFalse(user.isTrackEnabled());
    }

    @Test
    void should_update_location(){
        User user = getUser();
        LocationDto locationDto = new LocationDto();
        locationDto.setLatitude(50);
        locationDto.setLongitude(100);

        trackingService.updateLocation(user, locationDto);
        assertEquals(user.getLocation(), Location.of(50, 100));
    }

    private User getUser(){
        User user = new User();
        user.setName("user");
        user.setPassword("haslo");
        return user;
    }
}
