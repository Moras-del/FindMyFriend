package pl.moras.services;

import org.springframework.http.ResponseEntity;
import pl.moras.model.User;

import java.security.Principal;

public interface IFriendsService {

    ResponseEntity sendFriendRequest(String requestSender, String requestReceiver);

    User acceptRequest(String requestReceiver, String requestSender);

    User cancelRequest(String requestReceiver, String requestSender);

    User deleteFriend(String username, String friendName);
}
