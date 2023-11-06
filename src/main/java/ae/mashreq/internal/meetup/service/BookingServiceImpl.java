package ae.mashreq.internal.meetup.service;

import ae.mashreq.internal.meetup.dto.BookingRequestDto;
import ae.mashreq.internal.meetup.model.Booking;
import ae.mashreq.internal.meetup.model.ConferenceRoom;
import ae.mashreq.internal.meetup.model.MaintenanceTime;
import ae.mashreq.internal.meetup.repository.BookingRepository;
import ae.mashreq.internal.meetup.repository.ConferenceRoomRepository;
import ae.mashreq.internal.meetup.repository.MaintenanceTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MaintenanceTimeRepository maintenanceTimeRepository;

    @Override
    public Booking bookForGivenTimeAndPerson(BookingRequestDto bookingRequestDto) {
        if (!isValidBookingInterval(bookingRequestDto.getStartTime(), bookingRequestDto.getEndTime())) {
            throw new IllegalArgumentException("Booking time intervals must be in 15-minute increments.");
        }
        if (isBookingDuringMaintenance(bookingRequestDto.getStartTime(), bookingRequestDto.getEndTime())) {
            throw new IllegalArgumentException("Booking overlaps with maintenance time.");
        }

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), bookingRequestDto.getStartTime());
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), bookingRequestDto.getEndTime());

        List<ConferenceRoom> conferenceRooms = conferenceRoomRepository.findConferenceRoomsByCapacityGreaterThanEqualOrderByCapacity(bookingRequestDto.getNumberOfPeople());
        Optional<Booking> firstAvailableBooking = conferenceRooms.stream()
                .filter(conferenceRoom -> {
                    List<Booking> bookingsByConferenceRoomId = bookingRepository.findBookingsByTimeRangeAndRoom(startTime, endTime, conferenceRoom.getId());
                    return bookingsByConferenceRoomId == null || bookingsByConferenceRoomId.isEmpty();
                })
                .map(conferenceRoom -> {
                    Booking booking = new Booking();
                    booking.setNumberOfPeople(bookingRequestDto.getNumberOfPeople());
                    booking.setStartTime(startTime);
                    booking.setEndTime(endTime);
                    booking.setConferenceRoom(conferenceRoom);
                    return bookingRepository.save(booking);
                })
                .findFirst();
        if (firstAvailableBooking.isPresent()) {
            return firstAvailableBooking.get();
        } else {
            throw new IllegalArgumentException("No available rooms for the specified time and number of people.");
        }
    }

    @Override
    public List<ConferenceRoom> findAvailableRoomsByTimeRange(LocalTime startTime, LocalTime endTime) {
        if (!isValidBookingInterval(startTime, endTime)) {
            throw new IllegalArgumentException("Booking time intervals must be in 15-minute increments.");
        }
        if (isBookingDuringMaintenance(startTime, endTime)) {
            throw new IllegalArgumentException("Rooms not available during maintenance time.");
        }
        List<Booking> bookingsByTimeRange = bookingRepository.findBookingsByTimeRange(LocalDateTime.of(LocalDate.now(), startTime), LocalDateTime.of(LocalDate.now(), endTime));
        Set<ConferenceRoom> bookedRooms = bookingsByTimeRange.stream().map(Booking::getConferenceRoom).collect(Collectors.toSet());
        List<ConferenceRoom> availableRooms = conferenceRoomRepository.findAll();
        availableRooms.removeAll(bookedRooms);
        return availableRooms;
    }

    private boolean isValidBookingInterval(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        long minutesBetween = ChronoUnit.MINUTES.between(startTime, endTime);
        return minutesBetween % 15 == 0;
    }

    public boolean isBookingDuringMaintenance(LocalTime startTime, LocalTime endTime) {
        List<MaintenanceTime> maintenanceTimes = maintenanceTimeRepository.findMaintenanceTimesByRoomAndTimeRange(startTime, endTime);
        return !maintenanceTimes.isEmpty();
    }
}
