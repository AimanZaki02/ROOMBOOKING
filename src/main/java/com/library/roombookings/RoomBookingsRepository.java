package com.library.roombookings;

import com.library.roombookings.RoomBookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDate;

@Repository
public interface RoomBookingsRepository extends JpaRepository<RoomBookings, Integer> {

    // Existing method for searching
    @Query(value = "SELECT * FROM room_bookings WHERE MATCH(room_code, room_name, customer_name) " +
            "AGAINST (?1 IN BOOLEAN MODE)", nativeQuery = true)
    Page<RoomBookings> search(String keyword, Pageable pageable);

    // Method to find live bookings for a given date
    @Query("SELECT rb FROM RoomBookings rb WHERE rb.bookingDate = :date")
    List<RoomBookings> findLiveBookings(LocalDate date);

    // Method to count bookings by ID - you already have this
    Long countById(Integer id);

    // Additional repository methods as needed
}
