package org.example.mvc.app.item.service;

import org.example.mvc.app.item.model.Item;

import java.util.List;

public interface ItemService {

    Item saveItem(Item item);

    Item updateItem(Long id, Item item);

    void deleteItem(Long id);

    Item getItemById(Long id);

    List<Item> getAllItems();
}