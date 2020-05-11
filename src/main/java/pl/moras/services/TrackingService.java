package pl.moras.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.moras.model.Location;
import pl.moras.model.LocationDto;
import pl.moras.model.User;
import pl.moras.repo.UserRepo;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TrackingService implements ITrackingService {

    private final UserRepo userRepo;

    @Override
    public List<User> getOnlineFriends(String name) {
        return userRepo.findOnlineFriends(name);
    }

    @Override
    public void updateLocation(User user, LocationDto locationDto) {
        user.setLocation(Location.of(locationDto));
        userRepo.save(user);
    }

    @Override
    public void enableTracking(User user) {
        user.setTrackEnabled(true);
        userRepo.save(user);
    }

    @Override
    public void disableTracking(User user) {
        user.setTrackEnabled(false);
        userRepo.save(user);
    }

}
