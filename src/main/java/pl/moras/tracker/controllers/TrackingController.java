package pl.moras.tracker.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.moras.tracker.model.LocationDto;
import pl.moras.tracker.model.User;
import pl.moras.tracker.services.IAuthService;
import pl.moras.tracker.services.ITrackingService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/tracking")
@AllArgsConstructor
public class TrackingController {

    private final ITrackingService trackingService;
    private final IAuthService authService;

    @PutMapping("/enable")
    public User enableTracking(Principal principal) {
        User user = authService.getUser(principal.getName());
        return trackingService.enableTracking(user);
    }

    @PutMapping("/disable")
    public User disableTracking(Principal principal) {
        User user = authService.getUser(principal.getName());
        return trackingService.disableTracking(user);
    }

    @PutMapping("/update")
    public User updateLocation(Principal principal, @Valid @RequestBody LocationDto locationDto) {
        User user = authService.getUser(principal.getName());
        return trackingService.updateLocation(user, locationDto);
    }

    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public SseEmitter getOnlineUsers(Principal principal) {
        SseEmitter sseEmitter = new SseEmitter();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                while (true) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(trackingService.getOnlineFriends(principal.getName()));
                    sseEmitter.send(event);
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
        });
        return sseEmitter;
    }
}
