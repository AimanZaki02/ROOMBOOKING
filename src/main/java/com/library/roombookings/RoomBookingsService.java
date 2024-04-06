package com.library.roombookings;

import com.library.roombookings.RoomBookingsNotFoundException;
import com.library.roombookings.RoomBookings;
import com.library.roombookings.RoomBookingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomBookingsService {

    @Autowired
    private BookingRepository bookingRepo;

    public List<RoomBooking> listAll() {
        return bookingRepo.findAll();
    }

    public void save(RoomBooking booking) {
        bookingRepo.save(booking);
    }

    public RoomBooking get(Integer id) throws BookingNotFoundException {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking with ID: " + id + " not found."));
    }

    public static final int SEARCH_RESULT_PER_PAGE = 10;

    public Page<RoomBooking> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULT_PER_PAGE);
        return bookingRepo.search(keyword, pageable);
    }

    public void delete(Integer id) throws BookingNotFoundException {
        Long count = bookingRepo.countById(id);
        if (count == null || count == 0) {
            throw new BookingNotFoundException("Booking with ID: " + id + " not found.");
        }
        bookingRepo.deleteById(id);
    }
}
