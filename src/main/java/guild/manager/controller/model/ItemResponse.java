package guild.manager.controller.model;

import java.time.LocalDateTime;

import guild.manager.entity.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemResponse {
	
	private Long itemId;
	private Integer itemAmount;
	private String itemNotes;
	private LocalDateTime lastUpdated;
	private InfoResponse info;

	public ItemResponse(Item item) {
		info = new InfoResponse(item.getInfo());
		itemId = item.getItemId();
		itemAmount = item.getItemAmount();
		itemNotes = item.getItemNotes();
		lastUpdated = item.getLastUpdated();
	}

}
