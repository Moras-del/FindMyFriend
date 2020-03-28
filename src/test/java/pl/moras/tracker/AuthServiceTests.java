package pl.moras.tracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.moras.exceptions.UsernameAlreadyExists;
import pl.moras.model.User;
import pl.moras.model.UserDto;
import pl.moras.repo.UserRepo;
import pl.moras.services.AuthService;
import pl.moras.services.IAuthService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class AuthServiceTests {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    private IAuthService authService;

    @BeforeEach
    void setup(){
        initMocks(this);
        when(userRepo.findByName(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByName(exampleUser().getName())).thenReturn(Optional.of(exampleUser()));
        when(userRepo.save(any(User.class))).thenAnswer(arg->arg.getArgument(0));
        when(userRepo.existsByName(anyString())).thenReturn(false);
        when(userRepo.existsByName(exampleUser().getName())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        authService = new AuthService(userRepo, passwordEncoder);
    }

    @Test
    void should_login_user(){
        User user = authService.getUser(exampleUser().getName());
        assertEquals(exampleUser().getName(), user.getName());
        assertNotNull(user.getLastOnline());
    }

    @Test
    void should_fail_login_user(){
        assertThrows(UsernameNotFoundException.class, ()->authService.getUser("non-existing user"));
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

    private User exampleUser(){
        User user = new User();
        user.setName("user");
        user.setPassword("haslo");
        return user;
    }


}
