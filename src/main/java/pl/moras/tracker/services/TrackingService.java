package pl.moras.tracker.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.moras.tracker.model.Location;
import pl.moras.tracker.model.LocationDto;
import pl.moras.tracker.model.User;
import pl.moras.tracker.repo.UserRepository;

import java.util.List;


@Service
@AllArgsConstructor
public class TrackingService implements ITrackingService {

    private final UserRepository userRepository;

    @Override
    public List<User> getOnlineFriends(String name) {
        return userRepository.findAll();
    }

    @Override
    public User updateLocation(User user, LocationDto locationDto) {
        user.setLocation(Location.of(locationDto));
        return userRepository.save(user);
    }

    @Override
    public User enableTracking(User user) {
        user.setTrackingEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public User disableTracking(User user) {
        user.setTrackingEnabled(false);
        return userRepository.save(user);
    }


}
