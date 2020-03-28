package pl.moras.controllers;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.moras.model.LocationDto;
import pl.moras.model.User;
import pl.moras.services.ITrackingService;

import java.security.Principal;
import java.util.List;

@Controller
public class SocketController {

    private final ITrackingService trackingService;

    public SocketController(ITrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @SendToUser("/queue/reply")
    @MessageMapping("/track")
    public List<User> test(Principal principal, @Payload LocationDto locationDto){
        trackingService.updateLocation(principal, locationDto);
        return trackingService.getOnlineFriends(principal.getName());
    }
}
