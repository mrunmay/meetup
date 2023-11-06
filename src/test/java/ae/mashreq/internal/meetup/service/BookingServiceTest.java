package ae.mashreq.internal.meetup.service;

import ae.mashreq.internal.meetup.dto.BookingRequestDto;
import ae.mashreq.internal.meetup.model.Booking;
import ae.mashreq.internal.meetup.model.ConferenceRoom;
import ae.mashreq.internal.meetup.repository.BookingRepository;
import ae.mashreq.internal.meetup.repository.ConferenceRoomRepository;
import ae.mashreq.internal.meetup.repository.MaintenanceTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class BookingServiceTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private MaintenanceTimeRepository maintenanceTimeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBookForGivenTimeAndPerson_SuccessfulBooking() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStartTime(LocalTime.of(10, 0));
        bookingRequestDto.setEndTime(LocalTime.of(10, 15));
        bookingRequestDto.setNumberOfPeople(5);

        List<ConferenceRoom> conferenceRooms = Arrays.asList(
                new ConferenceRoom(1L, "Room1", 5),
                new ConferenceRoom(2L, "Room2", 10)
        );

        when(conferenceRoomRepository.findConferenceRoomsByCapacityGreaterThanEqualOrderByCapacity(5))
                .thenReturn(conferenceRooms);

        when(bookingRepository.findBookingsByTimeRangeAndRoom(Mockito.any(), Mockito.any(), Mockito.anyLong()))
                .thenReturn(Collections.emptyList());

        when(bookingRepository.save(Mockito.any(Booking.class))).thenAnswer(invocation -> {
            Booking savedBooking = invocation.getArgument(0);
            savedBooking.setId(1L);
            return savedBooking;
        });

        Booking booking = bookingService.bookForGivenTimeAndPerson(bookingRequestDto);

        assertNotNull(booking);
        assertEquals(1L, booking.getId());
        assertEquals(5, booking.getNumberOfPeople());
        assertEquals(bookingRequestDto.getStartTime(), booking.getStartTime().toLocalTime());
        assertEquals(bookingRequestDto.getEndTime(), booking.getEndTime().toLocalTime());
    }

    @Test
    public void testFindAvailableRoomsByTimeRange_Successful() {
        LocalTime startTime = LocalTime.of(14, 0);
        LocalTime endTime = LocalTime.of(14, 15);

        List<Booking> bookingsByTimeRange = Collections.emptyList();
        when(bookingRepository.findBookingsByTimeRange(Mockito.any(), Mockito.any()))
                .thenReturn(bookingsByTimeRange);

        List<ConferenceRoom> allRooms = Arrays.asList(
                new ConferenceRoom(1L, "Room1", 5),
                new ConferenceRoom(2L, "Room2", 10)
        );
        when(conferenceRoomRepository.findAll())
                .thenReturn(allRooms);

        List<ConferenceRoom> availableRooms = bookingService.findAvailableRoomsByTimeRange(startTime, endTime);

        assertEquals(allRooms, availableRooms);
    }

    @Test
    public void testBookForGivenTimeAndPerson_NoAvailableRooms() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStartTime(LocalTime.of(10, 0));
        bookingRequestDto.setEndTime(LocalTime.of(10, 15));
        bookingRequestDto.setNumberOfPeople(5);

        when(conferenceRoomRepository.findConferenceRoomsByCapacityGreaterThanEqualOrderByCapacity(5))
                .thenReturn(Collections.emptyList());

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.bookForGivenTimeAndPerson(bookingRequestDto),
                "No available rooms for the specified time and number of people.");
    }

    @Test
    public void testFindAvailableRoomsByTimeRange_NoRoomsAvailable() {
        LocalTime startTime = LocalTime.of(14, 0);
        LocalTime endTime = LocalTime.of(14, 15);

        List<Booking> bookingsByTimeRange = Collections.emptyList();
        when(bookingRepository.findBookingsByTimeRange(Mockito.any(), Mockito.any()))
                .thenReturn(bookingsByTimeRange);

        List<ConferenceRoom> allRooms = Collections.emptyList();
        when(conferenceRoomRepository.findAll())
                .thenReturn(allRooms);

        List<ConferenceRoom> availableRooms = bookingService.findAvailableRoomsByTimeRange(startTime, endTime);
        assertTrue(availableRooms.isEmpty());
    }
}
