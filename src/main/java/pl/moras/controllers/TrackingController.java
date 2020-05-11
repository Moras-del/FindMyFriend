package pl.moras.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.moras.model.LocationDto;
import pl.moras.model.User;
import pl.moras.services.IAuthService;
import pl.moras.services.ITrackingService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/tracking")
@AllArgsConstructor
public class TrackingController {

    private final ITrackingService trackingService;
    private final IAuthService authService;

    @PutMapping("/enable")
    public ResponseEntity enableTracking(Principal principal){
        User user = authService.getUser(principal.getName());
        trackingService.enableTracking(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/disable")
    public ResponseEntity disableTracking(Principal principal){
        User user = authService.getUser(principal.getName());
        trackingService.disableTracking(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity updateLocation(Principal principal, @Valid @RequestBody LocationDto locationDto){
        User user = authService.getUser(principal.getName());
        trackingService.updateLocation(user, locationDto);
        return ResponseEntity.ok().build();
    }
}
