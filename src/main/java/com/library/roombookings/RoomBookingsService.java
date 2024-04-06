package com.library.roombookings;

import com.library.room.Room;
import com.library.room.RoomNotFoundException;
import com.library.room.RoomRepository;
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
    private RoomBookingsRepository repo;

    public List<RoomBookings> listAll() {
        return repo.findAll();
    }

    public void save(RoomBookings roombookings) {
        repo.save(roombookings);
    }

    public Room get(Integer id) throws RoomBookingsNotFoundException {
        return repo.findById(id).orElseThrow(RoomBookingsNotFoundException::new);
    }

    public static final int SEARCH_RESULT_PER_PAGE = 10;

    public Page<Room> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULT_PER_PAGE);
        return repo.search(keyword, pageable);
    }

    public void delete(Integer id) throws RoomBookingsNotFoundException {
        Long count = repo.countById(id);
        if (count == null || count == 0) {
            throw new RoomBookingsNotFoundException();
        }
        repo.deleteById(id);
    }
}
