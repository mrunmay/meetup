package ae.mashreq.internal.meetup.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class MaintenanceTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime startTime;

    private LocalTime endTime;
}
