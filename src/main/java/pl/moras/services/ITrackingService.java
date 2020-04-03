package pl.moras.services;

import pl.moras.model.LocationDto;
import pl.moras.model.User;

import java.security.Principal;
import java.util.List;

public interface ITrackingService {

    List<User> getOnlineFriends(String name);

    void updateLocation(Principal principal, LocationDto locationDto);

    void changeTrackingState(Principal principal, boolean enabled);
}
