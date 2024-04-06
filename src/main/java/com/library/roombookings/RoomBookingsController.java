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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class RoomBookingsController {
    private static final Logger logger = LoggerFactory.getLogger(RoomBookingsController.class);

    @Autowired
    private RoomBookingsService roombookingsService;

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(Time.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(final String text) throws IllegalArgumentException {
                try {
                    setValue(new Time(timeFormat.parse(text).getTime()));
                } catch (final ParseException e) {
                    throw new IllegalArgumentException("Could not parse time: " + text, e);
                }
            }

            @Override
            public String getAsText() {
                Time value = (Time) getValue();
                return (value != null ? timeFormat.format(value) : "");
            }
        });
    }

    @GetMapping("/roombookings")
    public String listBookings(Model model) {
        try {
            model.addAttribute("bookings", roombookingsService.listAll());
            return "roombookings";
        } catch (Exception e) {
            logger.error("Error listing bookings: ", e);
            model.addAttribute("errorMessage", "Error retrieving bookings");
            return "error"; // Assume there is an 'error.html' Thymeleaf template
        }
    }

    @PostMapping("/saveBooking")
    public String saveBooking(@ModelAttribute("booking") RoomBookings booking, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to save booking: {}", booking); // Log the state of the booking object
        if (bindingResult.hasErrors()) {
            logger.error("Validation errors: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.booking", bindingResult);
            redirectAttributes.addFlashAttribute("booking", booking);
            return "redirect:/makebooking"; // Redirect to the form page to display errors
        }

        try {
            roombookingsService.save(booking);
            logger.info("Booking saved successfully: {}", booking); // Log the successful save
            redirectAttributes.addFlashAttribute("swal", "success");
            redirectAttributes.addFlashAttribute("message", "Booking saved successfully!");
            return "redirect:/makebooking";
        } catch (Exception e) {
            logger.error("Exception occurred during booking save: ", e);
            redirectAttributes.addFlashAttribute("swal", "error");
            redirectAttributes.addFlashAttribute("message", "Error saving booking: " + e.getMessage());
            return "redirect:/makebooking";
        }
    }

    @GetMapping("/liveTracking")
    public String liveBookings(Model model) {
        try {
            List<RoomBookings> liveBookings = roombookingsService.getLiveBookings();
            model.addAttribute("liveBookings", liveBookings);
            return "liveTracking"; // Name of the template that displays live bookings
        } catch (Exception e) {
            logger.error("Error retrieving live bookings: ", e);
            model.addAttribute("errorMessage", "Error retrieving live bookings");
            return "error"; // Error view
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
