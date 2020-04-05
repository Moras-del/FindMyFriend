package pl.moras.services;

import org.springframework.http.ResponseEntity;
import pl.moras.model.User;

import java.security.Principal;

public interface IFriendsService {

    ResponseEntity sendFriendRequest(Principal principal, String friendName);

    User acceptRequest(Principal principal, String friendName);

    User cancelRequest(Principal principal, String friendName);

    User deleteFriend(Principal principal, String friendName);
}
