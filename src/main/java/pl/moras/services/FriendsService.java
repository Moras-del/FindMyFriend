package pl.moras.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.moras.model.User;
import pl.moras.repo.UserRepo;

import java.security.Principal;
import java.util.Set;

@Service
public class FriendsService implements IFriendsService {

    private final UserRepo userRepo;

    public FriendsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public ResponseEntity sendFriendRequest(Principal principal, String friendName) {
        User user = getUser(principal.getName());
        User friend = getUser(friendName);
        if((!principal.getName().equals(friendName)) && (user.getFriends()==null||!user.getFriends().contains(friend))) {
            friend.addFriendRequest(user);
            userRepo.save(friend);
            return ResponseEntity.ok().build();
        } else if (principal.getName().equals(friendName))
            return ResponseEntity.badRequest().body("Nie możesz zaprosić samego siebie");
          else
            return ResponseEntity.badRequest().body("Użytkownik już jest Twoim znajomym");
    }

    @Override
    public User acceptRequest(Principal principal, String friendName) {
        User user = getUser(principal.getName());
        User friend = getUser(friendName);
        user.getFriendRequest().remove(friend);
        user.addFriend(friend);
        friend.addFriend(user);
        return userRepo.save(user);
    }

    @Override
    public User cancelRequest(Principal principal, String friendName) {
        User user = getUser(principal.getName());
        User friend = getUser(friendName);
        user.getFriendRequest().remove(friend);
        return userRepo.save(user);
    }

    @Override
    public User deleteFriend(Principal principal, String friendName) {
        User user = getUser(principal.getName());
        User friend = getUser(friendName);

        user.getFriends()
            .remove(friend);
        return userRepo.save(user);
    }


    private User getUser(String name){
         return userRepo.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika "+name));
    }
}
