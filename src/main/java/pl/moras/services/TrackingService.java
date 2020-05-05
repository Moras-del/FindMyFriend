package pl.moras.services;

import org.springframework.stereotype.Service;
import pl.moras.model.Location;
import pl.moras.model.LocationDto;
import pl.moras.model.User;
import pl.moras.repo.UserRepo;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrackingService implements ITrackingService {

    private final UserRepo userRepo;
    private final IAuthService authService;
    public TrackingService(UserRepo userRepo, IAuthService authService) {
        this.userRepo = userRepo;
        this.authService = authService;
    }

    @Override
    public List<User> getOnlineFriends(String name) {
        return userRepo.findOnlineFriends(name);
    }

    @Override
    public void updateLocation(Principal principal, LocationDto locationDto) {
        User user = authService.getUser(principal.getName());
        user.setLocation(Location.of(locationDto));
        userRepo.save(user);
    }

    @Override
    public void changeTrackingState(Principal principal, boolean enable) {
        User user = authService.getUser(principal.getName());
        user.setTrackEnabled(enable);
        user.setLastOnline(LocalDateTime.now());
        userRepo.save(user);
    }

}
