package ae.mashreq.internal.meetup.controller;

import ae.mashreq.internal.meetup.dto.BookingRequestDto;
import ae.mashreq.internal.meetup.dto.BookingResponseDto;
import ae.mashreq.internal.meetup.model.Booking;
import ae.mashreq.internal.meetup.model.ConferenceRoom;
import ae.mashreq.internal.meetup.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Validated
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<Object> bookForGivenTimeAndPerson(@Valid @RequestBody BookingRequestDto bookingRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(buildBookingResponse(bookingService.bookForGivenTimeAndPerson(bookingRequestDto)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<Object> findAvailableRooms(
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime) {
        try {
            List<ConferenceRoom> availableRooms = bookingService.findAvailableRoomsByTimeRange(startTime, endTime);
            if (!availableRooms.isEmpty()) {
                return ResponseEntity.ok(availableRooms);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No available rooms for the specified time.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    private BookingResponseDto buildBookingResponse(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setNumberOfPeople(booking.getNumberOfPeople());
        bookingResponseDto.setStartTime(LocalTime.from(booking.getStartTime()));
        bookingResponseDto.setEndTime(LocalTime.from(booking.getEndTime()));
        bookingResponseDto.setConferenceRoom(booking.getConferenceRoom().getName());
        bookingResponseDto.setMessage("Room booked successfully");
        return bookingResponseDto;
    }
}
