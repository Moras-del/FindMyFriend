package pl.moras.model;

import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class Location {
    private double latitude;
    private double longitude;

    public static Location of(LocationDto locationDto){
        return new Location(locationDto.getLatitude(), locationDto.getLongitude());
    }
}




