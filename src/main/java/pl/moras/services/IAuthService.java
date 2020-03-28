package pl.moras.services;


import org.springframework.http.ResponseEntity;
import pl.moras.model.User;

import java.security.Principal;

public interface IAuthService {
    User addUser(String username, String password);

    User getUser(String name);
}
