package pl.moras.tracker.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.moras.tracker.model.User;
import pl.moras.tracker.services.IFriendsService;
import reactor.core.publisher.Mono;

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
    public Mono<ResponseEntity> sendRequest(Principal principal, @RequestParam String friendName){
        return friendsService.sendFriendRequest(principal.getName(), friendName);
    }

    @PostMapping("/accept")
    public Mono<User> acceptRequest(Principal principal, @RequestParam String friendName){
       return friendsService.acceptRequest(principal.getName(), friendName);
    }

    @PostMapping("/cancel")
    public Mono<User> cancelRequest(Principal principal, @RequestParam String friendName){
        return friendsService.cancelRequest(principal.getName(), friendName);
    }

    @PostMapping("/delete")
    public Mono<User> deleteFriend(Principal principal, @RequestParam String friendName){
        return friendsService.deleteFriend(principal.getName(), friendName);
    }
}
