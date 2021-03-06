package pl.moras.tracker.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.moras.tracker.model.User;
import pl.moras.tracker.model.UserDto;
import pl.moras.tracker.services.IAuthService;

import java.security.Principal;

//logowanie, rejestracja
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping
    public User register(@RequestBody UserDto userDto) {
        return authService.addUser(userDto);
    }

    @GetMapping
    public User getUser(Principal principal) {
        return authService.getUser(principal.getName());
    }
}
