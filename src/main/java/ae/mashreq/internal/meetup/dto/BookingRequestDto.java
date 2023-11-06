package ae.mashreq.internal.meetup.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
public class BookingRequestDto implements Serializable {

    @Min(value = 2, message = "Number of people must be greater than 1")
    private int numberOfPeople;

    private LocalTime startTime;

    private LocalTime endTime;
}
