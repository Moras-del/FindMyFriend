package pl.moras.tracker.services;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.moras.tracker.model.User;
import pl.moras.tracker.model.UserRequestConnection;
import pl.moras.tracker.repo.UserRepository;


@Service
@AllArgsConstructor
public class FriendsService implements IFriendsService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity sendFriendRequest(String main, String other) {
        UserRequestConnection users = toUsers(main, other);
        if (usersAreDifferent(users) && requestNotSentYet(users)) {
            saveRequest(users);
            return ResponseEntity.ok().build();
        } else {
            String errorMessage = getErrorMessage(users);
            return ResponseEntity.badRequest().body(errorMessage);
        }

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

    private void saveRequest(UserRequestConnection users) {
        User main = users.getMain();
        User other = users.getOther();
        other.addFriendRequest(main);
        userRepository.save(other);
    }

    private String getErrorMessage(UserRequestConnection users) {
        if (!usersAreDifferent(users))
            return "You can't add yourself";
        else
            return "You have already sent a request to this user";
    }

    @Override
    public User acceptRequest(String main, String other) {
        UserRequestConnection users = toUsers(main, other);
        addFriend(users);
        return userRepository.save(users.getMain());
    }

    private void addFriend(UserRequestConnection users) {
        User main = users.getMain();
        User other = users.getOther();
        main.removeFriendRequest(other);
        main.addFriend(other);
        other.addFriend(main);
    }

    @Override
    public User cancelRequest(String main, String other) {
        UserRequestConnection users = toUsers(main, other);
        users.getMain().removeFriendRequest(users.getOther());
        return userRepository.save(users.getMain());
    }

    @Override
    public User deleteFriend(String main, String other) {
        UserRequestConnection users = toUsers(main, other);
        detachFriends(users);
        return userRepository.save(users.getMain());
    }


    private void detachFriends(UserRequestConnection users) {
        User main = users.getMain();
        User other = users.getOther();
        main.removeFriend(other);
        other.removeFriend(main);
    }


    private UserRequestConnection toUsers(String main, String other) {
        User mainUser = getUser(main);
        User otherUser = getUser(other);
        return UserRequestConnection
                        .builder()
                .withMain(mainUser)
                .withOther(otherUser)
                .build();
    }

    private User getUser(String name) {
        return userRepository.findByName(name).orElseThrow(() -> new UsernameNotFoundException(name + " does not exists"));
    }

}
