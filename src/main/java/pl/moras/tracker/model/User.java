package pl.moras.tracker.model;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

import org.neo4j.springframework.data.core.schema.*;

import java.time.LocalDateTime;
import java.util.*;

@Node
@Data
@EqualsAndHashCode(exclude = "friends")
public class User {

    @Id
    @GeneratedValue
    private Long id;


    private String name;

    @JsonIgnore
    private String password;

    @Setter(AccessLevel.NONE)
    private LocalDateTime lastOnline;

    @JsonUnwrapped
    private Location location;

    private boolean trackEnabled; //user wants to being seen and he wants to see others

    @Relationship(type = "FRIENDS")
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JsonIgnoreProperties(value = "friends")
    private Set<User> friends = new HashSet<>();

    @Relationship(type = "REQUEST", direction = Relationship.Direction.INCOMING)
    @JsonProperty("friend_requests")
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Set<User> friendRequests = new HashSet<>();

    public User() { }

    public User(User user) {
        this.name = user.name;
        this.password = user.password;
    }

    public void addFriend(User user){
        friends.add(user);
    }

    public void addFriendRequest(User friendRequest) {
        friendRequests.add(friendRequest);
    }

    public void deleteFriendRequest(User requestedUser){
        friendRequests.remove(requestedUser);
    }

    public void deleteFriend(User friend){
        friends.remove(friend);
    }

    public Set<User> getFriends() {
        return Collections.unmodifiableSet(friends);
    }

    public Set<User> getFriendRequests(){
        return Collections.unmodifiableSet(friendRequests);
    }

    public void updateLastOnlineDate(){
        lastOnline = LocalDateTime.now();
    }

}
