package com.library.roombookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RoomBookingsController {
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    // Additional methods and annotations remain the same but renamed to reflect the booking context

    @GetMapping("/booking")
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.listAll());
        return "booking"; // Updated template name
    }

    @PostMapping("/saveBooking")
    public String saveBooking(@ModelAttribute RoomBooking booking, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // Method body updated with references to RoomBooking and "booking" instead of "room"
        // ...
    }

    @GetMapping("/booking/edit/{id}")
    public String editBooking(@PathVariable Integer id, Model model) {
        try {
            RoomBooking booking = bookingService.get(id);
            model.addAttribute("booking", booking);
            return "editBooking"; // Updated template name
        } catch (BookingNotFoundException e) {
            // Exception handling remains the same
            // ...
        }
    }

    // Update and delete methods similarly refactored to match the booking context

    // Search method remains the same, but you would refactor to fit the booking context
    // ...

}
