package pl.moras.tracker;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.moras.tracker.exceptions.UsernameAlreadyExists;
import pl.moras.fakes.PasswordEncoderTestImpl;
import pl.moras.fakes.UserRepoTestImpl;
import pl.moras.tracker.model.User;
import pl.moras.tracker.model.UserDto;
import pl.moras.tracker.repo.UserRepo;
import pl.moras.tracker.services.AuthService;
import pl.moras.tracker.services.IAuthService;

import static org.junit.jupiter.api.Assertions.*;


class AuthServiceTests {
    private UserRepo userRepo = new UserRepoTestImpl();
    private PasswordEncoder passwordEncoder = new PasswordEncoderTestImpl();
    private IAuthService authService = new AuthService(userRepo, passwordEncoder);

    @Test
    void should_login_user(){
        User user = authService.getUser("user").block();
        assertEquals("user", user.getName());
        assertNotNull(user.getLastOnline());
    }

    @Test
    void should_fail_login_user(){
        assertThrows(UsernameNotFoundException.class, ()->authService.getUser("non-existing-user"));
    }

    @Test
    void should_register_user(){
        User user = authService.addUser(new UserDto()).block();
        assertEquals("newUser", user.getName());
        assertEquals("hashed", user.getPassword());
        assertNotNull(user.getLastOnline());
    }

    @Test
    void should_fail_register_user(){
        assertThrows(UsernameAlreadyExists.class, ()->authService.addUser(new UserDto()));
    }
}
