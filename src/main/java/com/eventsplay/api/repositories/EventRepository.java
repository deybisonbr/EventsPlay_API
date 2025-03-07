package com.eventsplay.api.repositories;

import com.eventsplay.api.domain.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.address a WHERE e.date >= :currentDate")
    public Page<Event> findUpcomingEvents(@Param("currentDate") Date currentDate, Pageable pageable);

    @Query("SELECT e FROM Event e "+
            "LEFT JOIN e.address a " +
            "WHERE e.date >= :currentDate AND " +
            "(:title IS NULL OR e.title LIKE %:title%) AND " +
            "(:city IS NULL OR a.city LIKE %:city%) AND " +
            "(:state IS NULL OR a.state LIKE %:state%) AND " +
            "(:startDate IS NULL OR e.date >= :startDate) AND " +
            "(:endDate IS NULL OR e.date >= :endDate)")
    public Page<Event> findFilteredEvents(@Param("currentDate") Date currentDate,
                                          @Param("title") String title,
                                          @Param("city") String city,
                                          @Param("state") String state,
                                          @Param("startDate") Date startDate,
                                          @Param("endDate") Date endDate,
                                          Pageable pageable);
}
