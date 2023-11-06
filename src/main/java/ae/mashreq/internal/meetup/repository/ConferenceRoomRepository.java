package ae.mashreq.internal.meetup.repository;

import ae.mashreq.internal.meetup.model.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Long> {

    List<ConferenceRoom> findConferenceRoomsByCapacityGreaterThanEqualOrderByCapacity(int noOfPeople);
}
