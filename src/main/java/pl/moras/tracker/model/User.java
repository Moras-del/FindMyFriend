package pl.moras.tracker.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NodeEntity("person")
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JsonIgnore
    private String password;

    @Setter(AccessLevel.NONE)
    private LocalDateTime lastOnline;

    private Location location;

    private boolean trackingEnabled;

//    @Setter(AccessLevel.NONE)
//    @Getter(AccessLevel.NONE)
//    private Relations relations = new Relations();

    @JsonIgnoreProperties(value = {"friendRequests", "friends"})
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Relationship(type = "FRIENDS")
    private Set<User> friends = new HashSet<>();

    @JsonIgnoreProperties(value = {"friends", "friendRequests"})
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Relationship(type = "REQUEST", direction = Relationship.INCOMING)
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

    public void updateLastOnlineDate(){
        lastOnline = LocalDateTime.now();
    }

    public boolean hasFriendRequest(User user) {
        return friendRequests.contains(user);
    }

    public boolean hasFriend(User user) {
        return friends.contains(user);
    }

    public boolean hasAnyFriends() {
        return !friends.isEmpty();
    }

    public boolean hasAnyFriendRequest() {
        return !friendRequests.isEmpty();
    }
}
