package pl.moras.tracker.services;


import pl.moras.tracker.model.User;
import pl.moras.tracker.model.UserDto;
import reactor.core.publisher.Mono;

public interface IAuthService {

    Mono<User> addUser(UserDto userDto);

    Mono<User> getUser(String name);
}
