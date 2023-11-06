package ae.mashreq.internal.meetup.service;

import ae.mashreq.internal.meetup.dto.BookingRequestDto;
import ae.mashreq.internal.meetup.model.Booking;
import ae.mashreq.internal.meetup.model.ConferenceRoom;

import java.time.LocalTime;
import java.util.List;

public interface BookingService {

    Booking bookForGivenTimeAndPerson(BookingRequestDto bookingDto);

    List<ConferenceRoom> findAvailableRoomsByTimeRange(LocalTime startTime, LocalTime endTime);
}
