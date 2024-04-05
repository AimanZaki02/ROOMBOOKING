package com.library.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/room")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.listAll());
        return "room"; // Thymeleaf template for displaying rooms
    }

    @PostMapping("/saveRoom")
    public String saveRoom(Room room, RedirectAttributes redirectAttributes) {
        roomService.save(room);
        redirectAttributes.addFlashAttribute("message", "Room saved successfully!");
        return "redirect:/secondindex"; // Redirect to prevent duplicate submissions
    }

    // Additional methods for CRUD operations
}
