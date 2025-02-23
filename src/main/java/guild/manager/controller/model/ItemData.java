package guild.manager.controller.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import guild.manager.controller.ApiController;
import guild.manager.entity.Item;
import guild.manager.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ItemData extends RepresentationModel<ItemData>{

	private Long itemId;
	private Integer itemAmount;
	private String itemNotes;

	private String itemName;
	private String guildName;
	private String depotName;
	private Long depotId;

	private LocalDateTime lastUpdated;

	private InfoData info;
	private Set<String> tags = new HashSet<>();

	public ItemData(Item item) {
		info = new InfoData(item.getInfo());
		itemName = info.getItemName();
		
		itemId = item.getItemId();
		itemAmount = item.getItemAmount();
		itemNotes = item.getItemNotes();
		lastUpdated = item.getLastUpdated();

		depotName = item.getDepot().getDepotName();
		depotId = item.getDepot().getDepotId();
		guildName = item.getDepot().getGuild().getGuildName();

		for (Tag tag : item.getTags()) {
			tags.add(tag.toString());
		}
		

		this.add(linkTo(methodOn(ApiController.class).getItemById(itemId)).withSelfRel());
	}

	public Item toItem(Item item) {
		item.setItemAmount(itemAmount);
		item.setItemId(itemId);
		item.setItemNotes(itemNotes);
		return item;
	}



	
}
