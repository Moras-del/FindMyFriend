package pl.moras.tracker.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Document
@Data
@EqualsAndHashCode(exclude = "friends")
public class User {

    @Id
    private String id;

    private String name;

    @JsonIgnore
    private String password;

    @Setter(AccessLevel.NONE)
    private LocalDateTime lastOnline;

    @JsonUnwrapped
    private Location location;

    private boolean trackEnabled; //user wants to being seen and he wants to see others

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JsonIgnoreProperties(value = "friends")
    private Set<User> friends = new HashSet<>();

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

    public void removeFriendRequest(User requestedUser) {
        friendRequests.remove(requestedUser);
    }

    public void removeFriend(User friend) {
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
