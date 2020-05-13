package pl.moras.tracker.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.moras.tracker.model.Location;
import pl.moras.tracker.model.LocationDto;
import pl.moras.tracker.model.User;
import pl.moras.tracker.repo.UserRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class TrackingService implements ITrackingService {

    private final UserRepo userRepo;

    @Override
    public Flux<User> getOnlineFriends(String name) {
        return userRepo.findAll();
    }

    @Override
    public Mono<User> updateLocation(User user, LocationDto locationDto) {
        return Mono.just(user)
                .map(person -> {
                    person.setLocation(Location.of(locationDto));
                    return person;
                })
                .flatMap(userRepo::save);
    }

    @Override
    public Mono<User> enableTracking(User user) {
        return Mono.just(user)
                .map(person -> {
                    person.setTrackEnabled(true);
                    return person;
                })
                .flatMap(userRepo::save);
    }

    @Override
    public Mono<User> disableTracking(User user) {
        return Mono.just(user)
                .map(person -> {
                    person.setTrackEnabled(false);
                    return person;
                })
                .flatMap(userRepo::save);
    }


}
