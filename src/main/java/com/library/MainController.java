package com.library;

import com.library.room.RoomService; // Import RoomService
import com.library.roombookings.RoomBookingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    @Autowired
    private RoomService roomService; // Autowire the RoomService

    @Autowired
    private RoomBookingsService roombookingsService;

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/secondindex")
    public String secondIndex(Model model, @ModelAttribute("message") String message,
                              @ModelAttribute("swal") String swal,
                              @ModelAttribute("errorMessage") String errorMessage) {
        // Add room data to the model along with other attributes
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("rooms", roomService.listAll()); // Add this line to pass rooms data
        return "secondindex";
    }

    @GetMapping("/makebooking")
    public String makebooking(Model model, @ModelAttribute("message") String message,
                              @ModelAttribute("swal") String swal,
                              @ModelAttribute("errorMessage") String errorMessage) {
        // Add room data to the model along with other attributes
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("rooms", roombookingsService.listAll()); // Add this line to pass rooms data

        return "makebooking";
    }
}
