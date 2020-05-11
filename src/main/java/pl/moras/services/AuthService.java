package pl.moras.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.moras.exceptions.UsernameAlreadyExists;
import pl.moras.model.User;
import pl.moras.repo.UserRepo;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User addUser(String username, String password) {
        if (userRepo.existsByName(username))
           throw new UsernameAlreadyExists(username);
        User user = new User();
        user.updateLastOnlineDate();
        user.setName(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepo.save(user);
    }

    @Override
    public User getUser(String name) {
        User user = userRepo.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika "+name));
        user.updateLastOnlineDate();
        return userRepo.save(user);
    }

}
