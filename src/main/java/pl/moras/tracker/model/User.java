package pl.moras.tracker.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
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

    private boolean trackEnabled;

    //@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
    @JsonUnwrapped
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Relations relations = new Relations();

    public User() { }

    public User(User user) {
        this.name = user.name;
        this.password = user.password;
    }

    public void addFriend(User user){
        relations.getFriends().add(user);
    }

    public void addFriendRequest(User friendRequest) {
        relations.getFriendRequests().add(friendRequest);
    }

    public void removeFriendRequest(User requestedUser) {
        relations.getFriendRequests().remove(requestedUser);
    }

    public void removeFriend(User friend) {
        relations.getFriends().remove(friend);
    }

    public void updateLastOnlineDate(){
        lastOnline = LocalDateTime.now();
    }

    public boolean hasFriendRequest(User user) {
        return relations.getFriendRequests().contains(user);
    }

    public boolean hasFriend(User user) {
        return relations.getFriends().contains(user);
    }

    public boolean hasAnyFriends() {
        return !relations.getFriends().isEmpty();
    }

    public boolean hasAnyFriendRequest() {
        return !relations.getFriendRequests().isEmpty();
    }
}
