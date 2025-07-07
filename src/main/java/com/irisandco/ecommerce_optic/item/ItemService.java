package com.irisandco.ecommerce_optic.item;

import com.irisandco.ecommerce_optic.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private final ItemRepository ITEM_REPOSITORY;

    public ItemService(ItemRepository itemRepository) {
        ITEM_REPOSITORY = itemRepository;
    }

    public Item getItemById(Long id){
        return ITEM_REPOSITORY.findById(id).orElseThrow(() -> new EntityNotFoundException(Item.class.getSimpleName(), "id", id.toString()));
    }

    public Item createItem(Item item){
        return ITEM_REPOSITORY.save(item);
    }

    public Item updateItem(Item item, int quantity){
        item.setQuantity(quantity);
        return ITEM_REPOSITORY.save(item);
    }

    public void deleteItemById(Long id){
        getItemById(id);
        ITEM_REPOSITORY.deleteById(id);
    }

}
