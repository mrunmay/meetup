package ae.mashreq.internal.meetup.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
public class BookingResponseDto implements Serializable {

    private int numberOfPeople;
    private LocalTime startTime;
    private LocalTime endTime;
    private String conferenceRoom;
    private String message;

}
