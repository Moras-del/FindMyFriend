package pl.moras.services;

import org.springframework.http.ResponseEntity;
import pl.moras.model.User;

import java.security.Principal;

public interface IFriendsService {

    ResponseEntity setFriendRequest(Principal principal, String friendName);

    User acceptRequest(Principal principal, String friendName);

    ResponseEntity cancelRequest(Principal principal, String friendName);

    ResponseEntity deleteFriend(Principal principal, String friendName);
}
