package pl.moras.tracker.services;

import org.springframework.http.ResponseEntity;
import pl.moras.tracker.model.User;
import reactor.core.publisher.Mono;

public interface IFriendsService {

    Mono<ResponseEntity> sendFriendRequest(String main, String other);

    Mono<User> acceptRequest(String main, String other);

    Mono<User> cancelRequest(String main, String other);

    Mono<User> deleteFriend(String main, String other);
}
