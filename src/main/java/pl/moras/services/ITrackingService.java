package pl.moras.services;

import pl.moras.model.LocationDto;
import pl.moras.model.User;

import java.security.Principal;
import java.util.List;

public interface ITrackingService {

    List<User> getOnlineFriends(String username);

    void updateLocation(User user, LocationDto locationDto);

    void enableTracking(User user);

    void disableTracking(User user);
}
