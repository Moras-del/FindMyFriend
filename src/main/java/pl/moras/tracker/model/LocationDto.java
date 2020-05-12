package pl.moras.tracker.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Getter
@Setter
public class LocationDto {

    @Max(value = 90, message = "invalid latitude value")
    @Min(value = -90, message = "invalid latitude value")
    double latitude;

    @Max(value = 180, message = "invalid longitude value")
    @Min(value = -180, message = "invalid longitude value")
    double longitude;
}
