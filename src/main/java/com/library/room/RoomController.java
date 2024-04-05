package com.library.room;

import com.library.room.Room;
import com.library.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    @Autowired
    private RoomService roomService;

    @GetMapping("/room")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.listAll());
        return "room"; // Thymeleaf template for displaying rooms
    }

    @PostMapping("/saveRoom")
    public String saveRoom(@ModelAttribute Room room, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                logger.error("Validation error: " + error.toString());
            });
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.room", bindingResult);
            redirectAttributes.addFlashAttribute("room", room);
            return "secondIndex";
        }
        try {
            roomService.save(room);
            redirectAttributes.addFlashAttribute("message", "Room saved successfully!");
            return "secondIndex";
        } catch (Exception e) {
            logger.error("Exception occurred during room save", e);
            redirectAttributes.addFlashAttribute(" errorMessage", "Error saving room: " + e.getMessage());
            return "secondIndex";
        }
    }

    // Map HTTP GET requests for '/student/search/page/{pageNum}'
    @GetMapping("/room/search/page/{pageNum}")
    public String searchByPage(String keyword, Model model,
                               @PathVariable(name = "pageNum") int pageNum) {

        // Get current page search results
        Page<Room> result = roomService.search(keyword, pageNum);

        // Store contents of current page results in List
        List<Room> listResult = result.getContent();

        // Add attributes to model container so that we can use it in Thymeleaf
        // Get pages total amount of search results
        model.addAttribute("totalPages", result.getTotalPages());
        // Get total amount of results from search
        model.addAttribute("totalItems", result.getTotalElements());
        // Get the current page number
        model.addAttribute("currentPage", pageNum);

        // Start counter to ensure only specified amount of results are displayed per page
        long startCount = (pageNum - 1) * roomService.SEARCH_RESULT_PER_PAGE + 1;
        // Get current start counter value
        model.addAttribute("startCount", startCount);

        // Determine when to stop displaying results.
        long endCount = startCount + RoomService.SEARCH_RESULT_PER_PAGE - 1;

        // We are probably on last page of search, but...
        // endCount cannot exceed total amount of results from search
        if (endCount > result.getTotalElements()) {
            // set counter to end at total amount of results
            endCount = result.getTotalElements();
        }

        // Store as attribute to be used in Thymeleaf
        // Get endCount value as attribute
        model.addAttribute("endCount", endCount);
        // Get List of search results for current page
        model.addAttribute("listResult", listResult);
        // Get keyword used for search
        model.addAttribute("keyword", keyword);

        // Return templates/search.html
        return "search";
    }
}
