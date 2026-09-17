package org.example.mvc.app.item.controller;

import jakarta.validation.Valid;
import org.example.mvc.app.item.exception.DuplicateItemException;
import org.example.mvc.app.item.model.Item;
import org.example.mvc.app.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public String getAllItems(Model model) {
        List<Item> items = itemService.getAllItems();
        model.addAttribute("items", items);
        return "items-list";
    }

    // Show add form
    @GetMapping("/add")
    public String showAddForm() {
        return "add-item";
    }

    // Save item
    @PostMapping("/save")
    public String saveItem(@Valid @ModelAttribute Item item, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("validationErrors", extractErrorMessages(bindingResult));
            return "add-item";
        }
        try {
            itemService.saveItem(item);
            return "redirect:/items";
        } catch (DuplicateItemException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("item", item);
            return "add-item";
        }
    }

    // Update item
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Item item = itemService.getItemById(id);
        model.addAttribute("item", item);
        return "update-item";
    }

    @PostMapping("/update/{id}")
    public String updateItem(@PathVariable Long id, @Valid @ModelAttribute Item item, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            item.setId(id);
            model.addAttribute("validationErrors", extractErrorMessages(bindingResult));
            return "update-item";
        }
        try {
            itemService.updateItem(id, item);
            return "redirect:/items";
        } catch (DuplicateItemException e) {
            item.setId(id);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("item", item);
            return "update-item";
        }
    }

    // Delete item
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return "redirect:/items";
    }

    // Get item by ID
    @GetMapping("/{id}")
    public String getItemById(@PathVariable Long id, Model model) {
        Item item = itemService.getItemById(id);
        model.addAttribute("item", item);
        return "item-details";
    }

    private List<String> extractErrorMessages(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
    }
}