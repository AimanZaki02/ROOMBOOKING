package com.library.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/room")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.listAll());
        return "rooms"; // Thymeleaf template for displaying rooms
    }

    // Additional methods for CRUD operations
}
