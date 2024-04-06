package com.library.roombookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RoomBookingsController {
    private static final Logger logger = LoggerFactory.getLogger(RoomBookingsController.class);

    @Autowired
    private RoomBookingsService roombookingsService;

    @GetMapping("/roombookings")
    public String listBookings(Model model) {
        model.addAttribute("bookings", roombookingsService.listAll());
        return "roombookings"; // Ensure this is the correct Thymeleaf template
    }

    @PostMapping("/saveBooking")
    public String saveBooking(@ModelAttribute RoomBookings booking, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> logger.error("Validation error: " + error.toString()));
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.booking", bindingResult);
            redirectAttributes.addFlashAttribute("booking", booking);
            return "redirect:/makebooking"; // Modify as necessary
        }
        try {
            roombookingsService.save(booking);
            redirectAttributes.addFlashAttribute("swal", "success");
            redirectAttributes.addFlashAttribute("message", "Booking saved successfully!");
            return "redirect:/makebooking"; // Modify as necessary
        } catch (Exception e) {
            logger.error("Exception occurred during booking save", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving booking: " + e.getMessage());
            return "redirect:/makebooking"; // Modify as necessary
        }
    }

    @GetMapping("/roombookings/edit/{id}")
    public String editBooking(@PathVariable Integer id, Model model) {
        try {
            RoomBookings booking = roombookingsService.get(id);
            model.addAttribute("booking", booking);
            return "editRoomBookings"; // Ensure this is the correct Thymeleaf template
        } catch (RoomBookingsNotFoundException e) {
            logger.error("Error finding booking: " + e.getMessage());
            return "redirect:/makebooking"; // Modify as necessary
        }
    }

    @PostMapping("/roombookings/update/{id}")
    public String updateBooking(@PathVariable Integer id, @ModelAttribute RoomBookings booking, RedirectAttributes redirectAttributes) {
        try {
            booking.setId(id);
            roombookingsService.save(booking);
            redirectAttributes.addFlashAttribute("message", "Booking updated successfully!");
            return "redirect:/makebooking"; // Modify as necessary
        } catch (Exception e) {
            logger.error("Error updating booking: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating booking: " + e.getMessage());
            return "redirect:/makebooking"; // Modify as necessary
        }
    }

    @GetMapping("/roombookings/delete/{id}")
    public String deleteBooking(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            roombookingsService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Booking deleted successfully!");
            return "redirect:/makebooking"; // Modify as necessary
        } catch (RoomBookingsNotFoundException e) {
            logger.error("Error deleting booking: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting booking: " + e.getMessage());
            return "redirect:/makebooking"; // Modify as necessary
        }
    }

    @GetMapping("/roombookings/search/page/{pageNum}")
    public String searchBookingsByPage(@PathVariable(name = "pageNum") int pageNum,
                                       @RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            Page<RoomBookings> result = roombookingsService.search(keyword, pageNum);
            model.addAttribute("bookings", result.getContent());
            model.addAttribute("totalPages", result.getTotalPages());
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalItems", result.getTotalElements());
            model.addAttribute("keyword", keyword);
        }
        return "makebooking"; // Ensure this is the correct Thymeleaf template for search results
    }
}
