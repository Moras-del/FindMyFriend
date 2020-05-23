package pl.moras.tracker.services;

import org.springframework.http.ResponseEntity;
import pl.moras.tracker.model.User;

public interface IFriendsService {

    ResponseEntity sendFriendRequest(String main, String other);

    User acceptRequest(String main, String other);

    User cancelRequest(String main, String other);

    User deleteFriend(String main, String other);
}
