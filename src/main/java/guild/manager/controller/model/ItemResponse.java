package guild.manager.controller.model;

import java.time.LocalDateTime;

import guild.manager.entity.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemResponse {
	
	private Long itemId;
	private String itemName;
	private Integer itemAmount;
	private Integer itemLevel;
	private String itemDescription;
	private String itemNotes;
	private LocalDateTime lastUpdated;

	public ItemResponse(Item item) {
		itemId = item.getItemId();
		itemName = item.getItemName();
		itemAmount = item.getItemAmount();
		itemLevel = item.getItemLevel();
		itemDescription = item.getItemDescription();
		itemNotes = item.getItemNotes();
		lastUpdated = item.getLastUpdated();
	}

}
