package pl.moras.tracker.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.moras.tracker.model.Location;
import pl.moras.tracker.model.LocationDto;
import pl.moras.tracker.model.User;
import pl.moras.tracker.repo.UserRepo;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class TrackingService implements ITrackingService {

    private final UserRepo userRepo;

    @Override
    public Mono<List<User>> getOnlineFriends(String name) {
        return userRepo.findOnlineFriends(name);
    }

    @Override
    public Mono<User> updateLocation(User user, LocationDto locationDto) {
        return Mono.just(user)
                .map(user1 -> {
                    user1.setLocation(Location.of(locationDto));
                    return user1;
                })
                .flatMap(userRepo::save);
    }

    @Override
    public Mono<User> enableTracking(User user) {
        return Mono.just(user)
                .map(user1 -> {
                    user1.setTrackEnabled(true);
                    return user1;
                })
                .flatMap(userRepo::save);
    }

    @Override
    public Mono<User> disableTracking(User user) {
        return Mono.just(user)
                .map(user1 -> {
                    user1.setTrackEnabled(false);
                    return user1;
                })
                .flatMap(userRepo::save);
    }

}
