package pl.moras.tracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class Relations {

    private Set<User> friends = new HashSet<>();

    private Set<User> friendRequests = new HashSet<>();

}
