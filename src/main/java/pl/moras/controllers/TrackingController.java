package pl.moras.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.moras.model.LocationDto;
import pl.moras.services.ITrackingService;

import java.security.Principal;


//wlacz/wylacz sledzenie, aktualizuj lokalizacje
@RestController
@RequestMapping("/tracking")
public class TrackingController {

    private final ITrackingService trackingService;

    public TrackingController(ITrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @PutMapping("/switch")
    public ResponseEntity switchTrackingMode(Principal principal, @RequestParam boolean enabled){
        trackingService.changeTracking(principal, enabled);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/update")
    public ResponseEntity updateLocation(Principal principal, @RequestBody LocationDto locationDto){
        trackingService.updateLocation(principal, locationDto);
        return ResponseEntity.ok().build();
    }
}
