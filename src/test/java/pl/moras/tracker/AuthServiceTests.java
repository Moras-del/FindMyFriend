package pl.moras.tracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.moras.exceptions.UsernameAlreadyExists;
import pl.moras.fakes.PasswordEncoderTestImpl;
import pl.moras.fakes.UserRepoTestImpl;
import pl.moras.model.User;
import pl.moras.model.UserDto;
import pl.moras.repo.UserRepo;
import pl.moras.services.AuthService;
import pl.moras.services.IAuthService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class AuthServiceTests {
    private UserRepo userRepo = new UserRepoTestImpl();
    private PasswordEncoder passwordEncoder = new PasswordEncoderTestImpl();
    private IAuthService authService = new AuthService(userRepo, passwordEncoder);

    @Test
    void should_login_user(){
        User user = authService.getUser("user");
        assertEquals("user", user.getName());
        assertNotNull(user.getLastOnline());
    }

    @Test
    void should_fail_login_user(){
        assertThrows(UsernameNotFoundException.class, ()->authService.getUser("non-existing-user"));
    }

    @Test
    void should_register_user(){
        User user = authService.addUser("newUser", "haslo");
        assertEquals("newUser", user.getName());
        assertEquals("hashed", user.getPassword());
        assertNotNull(user.getLastOnline());
    }

    @Test
    void should_fail_register_user(){
        assertThrows(UsernameAlreadyExists.class, ()->authService.addUser("user", "haslo"));
    }
}
