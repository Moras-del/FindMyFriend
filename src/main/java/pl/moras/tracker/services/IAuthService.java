package pl.moras.tracker.services;


import pl.moras.tracker.model.User;
import pl.moras.tracker.model.UserDto;

public interface IAuthService {

    User addUser(UserDto userDto);

    User getUser(String name);
}
