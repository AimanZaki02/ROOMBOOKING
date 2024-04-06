
package com.library.roombookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;  // Import the List class
@Service
public class RoomBookingsService {

    private static final int SEARCH_RESULT_PER_PAGE = 10; // Define the constant

    @Autowired
    private RoomBookingsRepository repo;

    public List<RoomBookings> listAll() {
        return repo.findAll();
    }

    public void save(RoomBookings booking) {
        repo.save(booking);
    }

    public RoomBookings get(Integer id) throws RoomBookingsNotFoundException {
        return repo.findById(id).orElseThrow(RoomBookingsNotFoundException::new);
    }

    public Page<RoomBookings> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULT_PER_PAGE);
        return repo.search(keyword, pageable); // Make sure the search method exists in your repository
    }

    public void delete(Integer id) throws RoomBookingsNotFoundException {
        Long count = repo.countById(id);
        if (count == null || count == 0) {
            throw new RoomBookingsNotFoundException("Room booking not found with ID: " + id);
        }
        repo.deleteById(id);
    }
}
