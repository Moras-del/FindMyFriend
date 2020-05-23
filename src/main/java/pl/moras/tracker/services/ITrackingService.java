package pl.moras.tracker.services;

import pl.moras.tracker.model.LocationDto;
import pl.moras.tracker.model.User;

import java.util.List;

public interface ITrackingService {

    List<User> getOnlineFriends(String username);

    User updateLocation(User user, LocationDto locationDto);

    User enableTracking(User user);

    User disableTracking(User user);
}
