package pl.moras.controllers;

import org.springframework.web.bind.annotation.*;
import pl.moras.model.User;
import pl.moras.model.UserDto;
import pl.moras.services.IAuthService;

import java.security.Principal;




//logowanie, rejestracja
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public User register(@RequestBody UserDto userDto){
        return authService.addUser(userDto.getUsername(), userDto.getPassword());
    }

    @GetMapping
    public User getUser(Principal principal){
        return authService.getUser(principal.getName());
    }
}
