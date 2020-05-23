package pl.moras.tracker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "with")
public class UserRequestConnection {
    private User main;
    private User other;

    public List<User> toList() {
        return Arrays.asList(main, other);
    }
}
