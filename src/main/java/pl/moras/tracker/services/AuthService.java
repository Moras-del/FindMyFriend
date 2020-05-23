package pl.moras.tracker.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.moras.tracker.exceptions.UsernameAlreadyExists;
import pl.moras.tracker.model.User;
import pl.moras.tracker.model.UserDto;
import pl.moras.tracker.repo.UserRepository;


@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User addUser(UserDto userDto) {
        if (userRepository.existsByName(userDto.getUsername()))
            throw new UsernameAlreadyExists(userDto.getUsername() + " already exists");
        User user = toUser(userDto);
        user.updateLastOnlineDate();
        return userRepository.save(user);
    }


    @Override
    public User getUser(String name) {
        User user = userRepository.findByName(name).orElseThrow(() -> new UsernameNotFoundException(name + " does not exists"));
        user.updateLastOnlineDate();
        return userRepository.save(user);
    }


    private User toUser(UserDto userDto){
        String hashPassword = passwordEncoder.encode(userDto.getPassword());
        User user = new User();
        user.setName(userDto.getUsername());
        user.setPassword(hashPassword);
        return user;
    }

}
