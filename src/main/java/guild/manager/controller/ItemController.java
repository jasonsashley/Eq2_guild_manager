package guild.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guild.manager.controller.model.ItemData;
import guild.manager.service.ItemService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {
	
	@Autowired
	ItemService itemService;
	
	@GetMapping("")
	public List<ItemData> getAllItems() {
		log.info("Retrieving all items");
		return itemService.retrieveAllItems();
	}
	
	@PutMapping("/{itemId}")
	public ItemData updateItem(@PathVariable Long itemId, @RequestBody ItemData itemData) {
		log.info("Updating item with ID={}", itemId);
		itemData.setItemId(itemId);
		return itemService.saveItem(itemData);
	}

}
