package pl.moras.tracker.services;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.moras.tracker.model.User;
import pl.moras.tracker.model.UserRequestConnection;
import pl.moras.tracker.repo.MongoDao;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class FriendsService implements IFriendsService {

    private final MongoDao mongoDao;

    @Override
    public Mono<ResponseEntity> sendFriendRequest(String main, String other) {
        return toUsers(main, other)
                .filter(this::usersAreDifferent)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("You can't add yourself")))
                .filter(this::requestNotSentYet)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("You have already sent request to this user")))
                .flatMap(this::saveRequest)
                .map(users -> ResponseEntity.ok().build());
    }

    private boolean usersAreDifferent(UserRequestConnection users) {
        return !users.getMain().getName().equals(users.getOther().getName());
    }

    private boolean requestNotSentYet(UserRequestConnection userRequestConnection) {
        User other = userRequestConnection.getOther();
        User main = userRequestConnection.getMain();
        boolean notRequested = !other.hasFriendRequest(main);
        boolean notFriends = !other.hasFriend(main);
        return notRequested && notFriends;
    }

    private Mono<User> saveRequest(UserRequestConnection users) {
        User main = users.getMain();
        User other = users.getOther();
        other.addFriendRequest(main);
        return mongoDao.save(other);
    }

    @Override
    public Mono<User> acceptRequest(String main, String other) {
        return toUsers(main, other)
                .map(this::addFriend)
                .flatMap(users -> mongoDao.save(users.getMain()));
    }

    private UserRequestConnection addFriend(UserRequestConnection users) {
        User main = users.getMain();
        User other = users.getOther();
        main.removeFriendRequest(other);
        main.addFriend(other);
        main.addFriend(other);
        return users;
    }

    @Override
    public Mono<User> cancelRequest(String main, String other) {
        return toUsers(main, other)
                .map(users -> {
                    users.getMain().removeFriendRequest(users.getOther());
                    return users;
                })
                .flatMap(users -> mongoDao.save(users.getMain()));
    }

    @Override
    public Mono<User> deleteFriend(String main, String other) {
        return toUsers(main, other)
                .map(this::detachFriends)
                .flatMap(users -> mongoDao.save(users.getMain()));
    }


    private UserRequestConnection detachFriends(UserRequestConnection users) {
        User main = users.getMain();
        User other = users.getOther();
        main.removeFriend(other);
        other.removeFriend(main);
        return users;
    }


    private Mono<UserRequestConnection> toUsers(String main, String other) {
        Mono<User> mainUser = getUser(main);
        Mono<User> otherUser = getUser(other);
        return mainUser.zipWith(otherUser)
                .map(users -> UserRequestConnection
                        .builder()
                        .withMain(users.getT1())
                        .withOther(users.getT2())
                        .build());
    }

    private Mono<User> getUser(String name){
        return mongoDao.findByName(name)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(name + " not found")));
    }


}
