package ae.mashreq.internal.meetup.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ConferenceRoom conferenceRoom;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int numberOfPeople;
}
