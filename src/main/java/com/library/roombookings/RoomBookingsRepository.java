package com.library.roombookings;

import com.library.roombookings.RoomBookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomBookingsRepository extends JpaRepository<RoomBookings, Integer> {
    // Updated query to match the searchable fields from room_bookings table
    // Assuming 'code' and 'name' can be searched for a room booking; you can modify it as needed
    @Query(value = "SELECT * FROM room_bookings WHERE MATCH(room_code, room_name, customer_name) " +
            "AGAINST (?1 IN BOOLEAN MODE)", nativeQuery = true)
    public Page<RoomBookings> search(String keyword, Pageable pageable);

    // Additional repository methods for counting bookings by ID or any other operation required
    public Long countById(Integer id);
}
