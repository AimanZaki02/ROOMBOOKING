package com.library.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository repo;

    public List<Room> listAll() {
        return repo.findAll();
    }

    public void save(Room room) {
        repo.save(room);
    }

    public Room get(Integer id) throws RoomNotFoundException {
        return repo.findById(id).orElseThrow(RoomNotFoundException::new);
    }

    public void delete(Integer id) throws RoomNotFoundException {
        if (!repo.existsById(id)) {
            throw new RoomNotFoundException();
        }
        repo.deleteById(id);
    }
}
