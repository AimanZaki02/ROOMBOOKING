package com.library.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.List;

@Controller
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    @GetMapping("/room")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.listAll());
        return "room"; // Make sure 'room' is the correct Thymeleaf template for displaying rooms
    }

    @PostMapping("/saveRoom")
    public String saveRoom(@ModelAttribute Room room, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> logger.error("Validation error: " + error.toString()));
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.room", bindingResult);
            redirectAttributes.addFlashAttribute("room", room);
            return "redirect:/secondindex";
        }
        try {
            roomService.save(room);
            redirectAttributes.addFlashAttribute("swal", "success");
            redirectAttributes.addFlashAttribute("message", "Room saved successfully!");
            return "redirect:/secondindex";
        } catch (Exception e) {
            logger.error("Exception occurred during room save", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving room: " + e.getMessage());
            return "redirect:/secondindex";
        }
    }

    @GetMapping("/room/edit/{id}")
    public String editRoom(@PathVariable Integer id, Model model) {
        try {
            Room room = roomService.get(id);
            model.addAttribute("room", room);
            return "editRoom"; // This should be the name of the Thymeleaf template for editing the room
        } catch (RoomNotFoundException e) {
            logger.error("Error finding room: " + e.getMessage());
            return "redirect:/secondindex";
        }
    }

    @PostMapping("/room/update/{id}")
    public String updateRoom(@PathVariable Integer id, @ModelAttribute Room room, RedirectAttributes redirectAttributes) {
        try {
            room.setId(id);
            roomService.save(room);
            redirectAttributes.addFlashAttribute("message", "Room updated successfully!");
            return "redirect:/secondindex";
        } catch (Exception e) {
            logger.error("Error updating room: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating room: " + e.getMessage());
            return "redirect:/secondindex";
        }
    }

    @GetMapping("/room/delete/{id}")
    public String deleteRoom(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            roomService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Room deleted successfully!");
            return "redirect:/secondindex";
        } catch (RoomNotFoundException e) {
            logger.error("Error deleting room: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting room: " + e.getMessage());
            return "redirect:/secondindex";
        }
    }

    // Map HTTP GET requests for '/student/search/page/{pageNum}'
    @GetMapping("/room/search/page/{pageNum}")
    public String searchByPage(@PathVariable(name = "pageNum") int pageNum, String keyword, Model model) {
        Page<Room> result = roomService.search(keyword, pageNum);
        List<Room> listResult = result.getContent();
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("totalItems", result.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        long startCount = (pageNum - 1) * roomService.SEARCH_RESULT_PER_PAGE + 1;
        model.addAttribute("startCount", startCount);
        long endCount = startCount + RoomService.SEARCH_RESULT_PER_PAGE - 1;
        if (endCount > result.getTotalElements()) {
            endCount = result.getTotalElements();
        }
        model.addAttribute("endCount", endCount);
        model.addAttribute("listResult", listResult);
        model.addAttribute("keyword", keyword);
        return "search";
    }
}