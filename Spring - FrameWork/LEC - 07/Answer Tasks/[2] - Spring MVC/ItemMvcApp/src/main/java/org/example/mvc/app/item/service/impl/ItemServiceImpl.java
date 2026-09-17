package org.example.mvc.app.item.service.impl;

import org.example.mvc.app.item.exception.DuplicateItemException;
import org.example.mvc.app.item.model.Item;
import org.example.mvc.app.item.repository.ItemRepository;
import org.example.mvc.app.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    // Save item
    @Override
    public Item saveItem(Item item) {
        if (itemRepository.existsByNameIgnoreCase(item.getName())) {
            throw new DuplicateItemException("An item named \"" + item.getName() + "\" already exists.");
        }
        return itemRepository.save(item);
    }

    // Update item
    @Override
    public Item updateItem(Long id, Item item) {
        if (itemRepository.existsByNameIgnoreCaseAndIdNot(item.getName(), id)) {
            throw new DuplicateItemException("An item named \"" + item.getName() + "\" already exists.");
        }

        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        existingItem.setName(item.getName());
        existingItem.setPrice(item.getPrice());
        existingItem.setQuantity(item.getQuantity());

        return itemRepository.save(existingItem);
    }

    // Delete item
    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    // Get item by ID
    @Override
    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    // Get all items
    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}