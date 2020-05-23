package pl.moras.tracker.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.moras.tracker.model.User;
import pl.moras.tracker.services.IFriendsService;

import java.security.Principal;


//dodaj, usun, zapros znajomych
@RestController
@RequestMapping("/friends")
public class FriendsController {

    private final IFriendsService friendsService;

    public FriendsController(IFriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @PostMapping("/request/send")
    public ResponseEntity sendRequest(Principal principal, @RequestParam String friendName) {
        return friendsService.sendFriendRequest(principal.getName(), friendName);
    }

    @PostMapping("/accept")
    public User acceptRequest(Principal principal, @RequestParam String friendName) {
       return friendsService.acceptRequest(principal.getName(), friendName);
    }

    @PostMapping("/request/cancel")
    public User cancelRequest(Principal principal, @RequestParam String friendName) {
        return friendsService.cancelRequest(principal.getName(), friendName);
    }

    @PostMapping("/delete")
    public User deleteFriend(Principal principal, @RequestParam String friendName) {
        return friendsService.deleteFriend(principal.getName(), friendName);
    }
}
