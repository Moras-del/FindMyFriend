package pl.moras.tracker.controllers;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.moras.tracker.model.LocationDto;
import pl.moras.tracker.model.User;
import pl.moras.tracker.services.IAuthService;
import pl.moras.tracker.services.ITrackingService;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
public class SocketController {

    private final ITrackingService trackingService;
    private final IAuthService authService;

    @SendToUser("/queue/reply")
    @MessageMapping("/track")
    public Mono<List<User>> test(Principal principal, @Payload LocationDto locationDto){
        User user = authService.getUser(principal.getName()).block();
        trackingService.updateLocation(user, locationDto);
        return trackingService.getOnlineFriends(principal.getName());
    }
}
