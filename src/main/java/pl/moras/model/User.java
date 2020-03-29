package pl.moras.model;


import com.fasterxml.jackson.annotation.*;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.id.IdStrategy;
import org.neo4j.ogm.id.InternalIdStrategy;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NodeEntity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @JsonIgnore
    private String password;

    private LocalDateTime lastOnline;

    private double latitude;
    private double longitude;

    private boolean trackEnabled; //user chce żeby go widziano i chce ogladac innych

    @Relationship(type = "FRIENDS")
    private Set<User> friends;

    @Relationship(type = "REQUEST", direction = Relationship.INCOMING)
    @JsonProperty("friend_request")
    private Set<User> friendRequests;

    public User() { }

    public User(User user) {
        this.name = user.name;
        this.password = user.password;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastOnline() {
        return lastOnline;
    }
    public void setLastOnline(LocalDateTime lastOnline) {
        this.lastOnline = lastOnline;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Set<User> getFriends() {
        return friends;
    }
    public void addFriend(User user){
        if (friends ==null){
            friends = new HashSet<>();
        }
        friends.add(user);
    }

    public boolean isTrackEnabled() {
        return trackEnabled;
    }
    public void setTrackEnabled(boolean trackEnabled) {
        this.trackEnabled = trackEnabled;
    }

    @JsonProperty("friend_request")
    public Set<User> getFriendRequest() {
        return friendRequests;
    }
    public void addFriendRequest(User friendRequest) {
        if (friendRequests==null){
            friendRequests = new HashSet<>();
        }
        friendRequests.add(friendRequest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return
                Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                Double.compare(user.latitude, latitude) == 0 &&
                Double.compare(user.longitude, longitude) == 0 &&
                trackEnabled == user.trackEnabled &&
                Objects.equals(lastOnline, user.lastOnline) &&
                Objects.equals(friends, user.friends) &&
                Objects.equals(friendRequests, user.friendRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, lastOnline, latitude, longitude, trackEnabled, friends, friendRequests);
    }
}
