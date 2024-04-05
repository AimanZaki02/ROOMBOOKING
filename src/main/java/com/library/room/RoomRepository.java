package com.library.room;

import com.library.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
// Annotate as query
    // We create a custom query to implement full text searches for columns...
    // isbn, title, genre
    // Here, nativeQuery = true means it is a native database query (MySQL)
    @Query(value = "SELECT * FROM room WHERE MATCH(id, code, location, capacity) "
            + "AGAINST (?1)", nativeQuery = true)

    // Declare to used in service class
    public Page<Room> search(String keyword, Pageable pageable);
    public Long countById(Integer id);
}
