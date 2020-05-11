package pl.moras.services;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.moras.model.User;
import pl.moras.repo.UserRepo;

import java.security.Principal;
import java.util.Set;

@Service
@AllArgsConstructor
public class FriendsService implements IFriendsService {

    private final UserRepo userRepo;

    @Override
    public ResponseEntity sendFriendRequest(String requestSender, String requestReceiver) {
        if (requestSender.equals(requestReceiver))
            return ResponseEntity.badRequest().body("Nie możesz zaprosić samego siebie");
        User user = getUser(requestSender);
        User requestedUser = getUser(requestReceiver);
        boolean isAlreadyFriend = user.getFriends().contains(requestedUser);
        if(!isAlreadyFriend) {
            requestedUser.addFriendRequest(user);
            userRepo.save(requestedUser);
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.badRequest().body("Użytkownik już jest Twoim znajomym");
    }

    @Override
    public User acceptRequest(String requestReceiver, String requestSender) {
        User user = getUser(requestReceiver);
        User friend = getUser(requestSender);
        user.deleteFriendRequest(friend);
        user.addFriend(friend);
        friend.addFriend(user);
        return userRepo.save(friend);
    }

    @Override
    public User cancelRequest(String requestReceiver, String sender) {
        User user = getUser(sender);
        User requestedUser = getUser(requestReceiver);
        user.deleteFriendRequest(requestedUser);
        return userRepo.save(user);
    }

    @Override
    public User deleteFriend(String username, String friendName) {
        User user = getUser(username);
        User friend = getUser(friendName);
        user.deleteFriend(friend);
        friend.deleteFriend(user);
        return userRepo.save(user);
    }


    private User getUser(String name){
         return userRepo.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika "+name));
    }
}
