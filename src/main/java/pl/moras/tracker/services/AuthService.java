package pl.moras.tracker.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.moras.tracker.exceptions.UsernameAlreadyExists;
import pl.moras.tracker.model.User;
import pl.moras.tracker.model.UserDto;
import pl.moras.tracker.repo.MongoDao;
import reactor.core.publisher.Mono;


@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    private final MongoDao mongoDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<User> addUser(UserDto userDto) {
        return Mono.just(userDto)
                .filterWhen(dto -> mongoDao.notExists(dto.getUsername()))
                .switchIfEmpty(Mono.error(new UsernameAlreadyExists(userDto.getUsername() + " already exists")))
                .map(this::toUser)
                .flatMap(mongoDao::save);
    }

    @Override
    public Mono<User> getUser(String name) {
        return mongoDao.findByName(name)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(name + " not found")))
                .map(this::updateLastOnline)
                .flatMap(mongoDao::save);
    }

    private User updateLastOnline(User user){
        user.updateLastOnlineDate();
        return user;
    }

    private User toUser(UserDto userDto){
        String hashPassword = passwordEncoder.encode(userDto.getPassword());
        User user = new User();
        user.setName(userDto.getUsername());
        user.setPassword(hashPassword);
        user.updateLastOnlineDate();
        return user;
    }

}
