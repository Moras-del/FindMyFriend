package pl.moras.controllers;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.moras.model.LocationDto;
import pl.moras.model.User;
import pl.moras.services.IAuthService;
import pl.moras.services.ITrackingService;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
public class SocketController {

    private final ITrackingService trackingService;
    private final IAuthService authService;

    @SendToUser("/queue/reply")
    @MessageMapping("/track")
    public List<User> test(Principal principal, @Payload LocationDto locationDto){
        User user = authService.getUser(principal.getName());
        trackingService.updateLocation(user, locationDto);
        return trackingService.getOnlineFriends(principal.getName());
    }
}
