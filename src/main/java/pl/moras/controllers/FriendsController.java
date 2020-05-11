package pl.moras.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.moras.model.User;
import pl.moras.services.IFriendsService;

import javax.validation.constraints.NotNull;
import java.security.Principal;


//dodaj, usun, zapros znajomych
@RestController
@RequestMapping("/friends")
public class FriendsController {

    private final IFriendsService friendsService;

    public FriendsController(IFriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @PostMapping("/request")
    public ResponseEntity sendRequest(Principal principal, @RequestParam String friendName){
        return friendsService.sendFriendRequest(principal.getName(), friendName);
    }

    @PostMapping("/accept")
    public User acceptRequest(Principal principal, @RequestParam String friendName){
       return friendsService.acceptRequest(principal.getName(), friendName);
    }

    @PostMapping("/cancel")
    public User cancelRequest(Principal principal, @RequestParam String friendName){
        return friendsService.cancelRequest(principal.getName(), friendName);
    }

    @PostMapping("/delete")
    public User deleteFriend(Principal principal, @RequestParam String friendName){
        return friendsService.deleteFriend(principal.getName(), friendName);
    }
}
