package ae.mashreq.internal.meetup.repository;

import ae.mashreq.internal.meetup.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "WHERE ((:startTime > b.startTime AND :startTime < b.endTime) " +
            "OR (:endTime > b.startTime AND :endTime < b.endTime) " +
            "OR (:startTime <= b.startTime AND :endTime >= b.endTime)) " +
            "AND b.conferenceRoom.id = :roomId")
    List<Booking> findBookingsByTimeRangeAndRoom(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime,
                                                 @Param("roomId") Long roomId);

    @Query("SELECT b FROM Booking b " +
            "WHERE ((:startTime > b.startTime AND :startTime < b.endTime) " +
            "OR (:endTime > b.startTime AND :endTime < b.endTime) " +
            "OR (:startTime <= b.startTime AND :endTime >= b.endTime)) ")
    List<Booking> findBookingsByTimeRange(@Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
}
