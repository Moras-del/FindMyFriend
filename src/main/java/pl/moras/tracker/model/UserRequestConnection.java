package pl.moras.tracker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
public class UserRequestConnection {
    private User main;
    private User other;
}
