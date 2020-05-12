package pl.moras.tracker.services;

import org.springframework.http.ResponseEntity;
import pl.moras.tracker.model.User;
import reactor.core.publisher.Mono;

public interface IFriendsService {

    Mono<ResponseEntity> sendFriendRequest(String sender, String receiver);

    Mono<User> acceptRequest(String receiver, String sender);

    Mono<User> cancelRequest(String receiver, String sender);

    Mono<User> deleteFriend(String username, String friendName);
}
