package pl.moras.tracker.services;

import pl.moras.tracker.model.LocationDto;
import pl.moras.tracker.model.User;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITrackingService {

    Mono<List<User>> getOnlineFriends(String username);

    Mono<User> updateLocation(User user, LocationDto locationDto);

    Mono<User> enableTracking(User user);

    Mono<User> disableTracking(User user);
}
