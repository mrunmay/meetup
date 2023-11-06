package ae.mashreq.internal.meetup.repository;

import ae.mashreq.internal.meetup.model.MaintenanceTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface MaintenanceTimeRepository extends JpaRepository<MaintenanceTime, Long> {

    @Query("SELECT m FROM MaintenanceTime m " +
            "WHERE :startTime < m.endTime " +
            "AND :endTime > m.startTime")
    List<MaintenanceTime> findMaintenanceTimesByRoomAndTimeRange(@Param("startTime") LocalTime startTime,
                                                                 @Param("endTime") LocalTime endTime);
}
