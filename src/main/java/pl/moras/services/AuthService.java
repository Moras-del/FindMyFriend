package pl.moras.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.moras.exceptions.UsernameAlreadyExists;
import pl.moras.model.User;
import pl.moras.repo.UserRepo;

import java.time.LocalDateTime;


@Service
public class AuthService implements IAuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User addUser(String username, String password) {
        if (userRepo.existsByName(username))
           throw new UsernameAlreadyExists(username);
        User user = new User();
        user.setName(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepo.save(user);
    }

    @Override
    public User getUser(String name) {
        User user = userRepo.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika "+name));
        user.setLastOnline(LocalDateTime.now());
        return userRepo.save(user);
    }

}
