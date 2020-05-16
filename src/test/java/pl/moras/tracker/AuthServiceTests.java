package pl.moras.tracker;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.moras.fakes.MongoDaoTestImpl;
import pl.moras.fakes.PasswordEncoderTestImpl;
import pl.moras.tracker.exceptions.UsernameAlreadyExists;
import pl.moras.tracker.model.UserDto;
import pl.moras.tracker.repo.MongoDao;
import pl.moras.tracker.services.AuthService;
import pl.moras.tracker.services.IAuthService;
import reactor.test.StepVerifier;


class AuthServiceTests {
    private MongoDao mongoDao = new MongoDaoTestImpl();
    private PasswordEncoder passwordEncoder = new PasswordEncoderTestImpl();
    private IAuthService authService = new AuthService(mongoDao, passwordEncoder);

    @Test
    void should_login_user() throws InterruptedException {
        StepVerifier
                .create(authService.getUser("user"))
                .expectNextMatches(user -> user.getName().contains("user"))
                .verifyComplete();
    }

    @Test
    void should_fail_login_user(){
        StepVerifier
                .create(authService.getUser("non-existing-user"))
                .expectError(UsernameNotFoundException.class)
                .verify();
    }

    @Test
    void should_register_user(){
        UserDto userDto = new UserDto();
        userDto.setUsername("newUser");
        userDto.setPassword("password");
        StepVerifier
                .create(authService.addUser(userDto))
                .expectNextMatches(user -> user.getName().equals("newUser"))
                .verifyComplete();

//        assertEquals("newUser", user.getName());
//        assertEquals("hashed", user.getPassword());
//        assertNotNull(user.getLastOnline());
    }

    @Test
    void should_fail_register_user(){
        UserDto userDto = new UserDto();
        userDto.setUsername("user");
        userDto.setPassword("password");
        StepVerifier
                .create(authService.addUser(userDto))
                .expectError(UsernameAlreadyExists.class)
                .verify();
    }
}
