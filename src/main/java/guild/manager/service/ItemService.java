package guild.manager.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guild.manager.controller.model.ItemData;
import guild.manager.dao.ItemDao;
import guild.manager.entity.Depot;
import guild.manager.entity.Item;

@Service
public class ItemService {

	@Autowired
	ItemDao itemDao;

	@Autowired
	GuildService guildService;

	@Transactional(readOnly = true)
	public List<ItemData> retrieveAllItems() {
		return itemDao.findAll().stream().map(ItemData::new).toList();
	}

	@Transactional(readOnly = false)
	public ItemData saveItem(ItemData itemData, Long depotId, Long guildId) {
		Depot depot = guildService.findDepotById(depotId, guildId);
		Long itemId = itemData.getItemId();
		Item item = findOrCreateItem(itemId, depotId);
		itemData.toItem(item);
		
		depot.getItems().add(item);
		item.setDepot(depot);

		return new ItemData(itemDao.save(item));
	}
	
	@Transactional(readOnly = false)
	public ItemData saveItem(ItemData itemData) {
		Long itemId = itemData.getItemId();
		Item item = findOrCreateItem(itemId, null);
		itemData.toItem(item);
		return new ItemData(itemDao.save(item));
	}

	private Item findOrCreateItem(Long itemId, Long depotId) {
		Item item;

		if (Objects.isNull(itemId)) {
			item = new Item();
		} else {
			item = findItemById(itemId, depotId);
		}
		return item;
	}

	private Item findItemById(Long itemId, Long depotId) {
		Item item = itemDao.findById(itemId)
				.orElseThrow(() -> new NoSuchElementException("Item with ID=" + itemId + " was not found"));
		Depot attachedDepot = item.getDepot();
		if (Objects.nonNull(attachedDepot) && Objects.nonNull(depotId) && attachedDepot.getDepotId() != depotId) {
			throw new IllegalArgumentException("Item with ID=" + itemId + " is not owned by depot with ID=" + depotId);
		}
		return item;
	}
}
