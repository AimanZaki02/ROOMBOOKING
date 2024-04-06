package com.library;

import com.library.room.RoomService;
import com.library.roombookings.RoomBookingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomBookingsService roombookingsService;

    @GetMapping("/")
    public String showHomePage() {
        logger.debug("Showing home page");
        return "index";
    }

    @GetMapping("/secondindex")
    public String secondIndex(Model model, @ModelAttribute("message") String message,
                              @ModelAttribute("swal") String swal,
                              @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("rooms", roomService.listAll());
        return "secondindex";
    }

    @GetMapping("/neosecondindex")
    public String neosecondIndex(Model model, @ModelAttribute("message") String message,
                              @ModelAttribute("swal") String swal,
                              @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        return "neosecondindex";
    }

    @GetMapping("/makebooking")
    public String makebooking(Model model, @ModelAttribute("message") String message,
                              @ModelAttribute("swal") String swal,
                              @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("rooms", roombookingsService.listAll());
        return "makebooking";
    }

    @GetMapping("/liveTracking")
    public String liveTracking(Model model) {
        logger.debug("Entering liveTracking method");
        return "liveTracking";
    }
}
