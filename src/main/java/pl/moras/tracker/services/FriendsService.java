package pl.moras.tracker.services;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.moras.tracker.model.User;
import pl.moras.tracker.repo.UserRepo;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@AllArgsConstructor
public class FriendsService implements IFriendsService {

    private final UserRepo userRepo;

    @Override
    public Mono<ResponseEntity> sendFriendRequest(String sender, String receiver) {
        return toUsers(sender, receiver)
                .map(users->persistRequest(users.getT1(), users.getT2()));
    }

    @Override
    public Mono<User> acceptRequest(String receiver, String sender) {
        return toUsers(sender, receiver)
                .flatMap(users->addFriend(users.getT1(), users.getT2()));
    }

    @Override
    public Mono<User> cancelRequest(String receiver, String sender) {
        return toUsers(sender, receiver)
                .flatMap(users->{
                    users.getT2().deleteFriendRequest(users.getT1());
                    return userRepo.save(users.getT2());
                });
    }

    @Override
    public Mono<User> deleteFriend(String username, String friendName) {
        return toUsers(username, friendName)
                .flatMap(users->{
                    User user = users.getT1();
                    User friend = users.getT2();
                    user.deleteFriend(friend);
                    friend.deleteFriend(user);
                    return userRepo.save(user);
                });
    }


    private Mono<Tuple2<User, User>> toUsers(String sender, String receiver) {
        Mono<User> userMono = getUser(sender);
        Mono<User> requestedUserMono = getUser(receiver);
        return userMono.zipWith(requestedUserMono);
    }

    private Mono<User> getUser(String name){
         return userRepo.findByName(name);
                //.orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika "+name));
    }

    private ResponseEntity persistRequest(User sender, User receiver){
        boolean isAlreadyFriend = receiver.getFriends().contains(sender);
        if (!isAlreadyFriend){
            receiver.addFriendRequest(sender);
            userRepo.save(receiver);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("juz jest twoim znajomym");
    }

    private Mono<User> addFriend(User sender, User receiver) {
        receiver.deleteFriendRequest(sender);
        receiver.addFriend(sender);
        sender.addFriend(receiver);
        return userRepo.save(receiver);
    }

}
