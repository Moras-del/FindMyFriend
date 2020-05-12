package pl.moras.tracker.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.moras.tracker.model.LocationDto;
import pl.moras.tracker.model.User;
import pl.moras.tracker.services.IAuthService;
import pl.moras.tracker.services.ITrackingService;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/tracking")
@AllArgsConstructor
public class TrackingController {

    private final ITrackingService trackingService;
    private final IAuthService authService;

    @PutMapping("/enable")
    public Mono<User> enableTracking(Principal principal){
        return Mono.just(principal.getName())
                .flatMap(authService::getUser)
                .flatMap(trackingService::enableTracking);
    }

    @PutMapping("/disable")
    public Mono<User> disableTracking(Principal principal){
        return Mono.just(principal.getName())
                .flatMap(authService::getUser)
                .flatMap(trackingService::disableTracking);
    }

    @PutMapping("/update")
    public Mono<User> updateLocation(Principal principal, @Valid @RequestBody LocationDto locationDto){
        return Mono.just(principal.getName())
                .flatMap(authService::getUser)
                .flatMap(user->trackingService.updateLocation(user, locationDto));
    }
}
