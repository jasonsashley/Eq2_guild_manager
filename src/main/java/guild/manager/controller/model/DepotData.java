package guild.manager.controller.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import guild.manager.entity.Depot;
import guild.manager.entity.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepotData {

	private Long depotId;
	private String depotName;
	private LocalDateTime lastUpdated;
	
	private String guildName;
	private Set<ItemData> items = new HashSet<>();

	public DepotData(Depot depot) {
		depotId = depot.getDepotId();
		depotName = depot.getDepotName();
		lastUpdated = depot.getLastUpdated();
		guildName = depot.getGuild().getGuildName();

		for (Item item : depot.getItems()) {
			items.add(new ItemData(item));
		}
	}

	public void toDepot(Depot depot) {
		depot.setDepotId(depotId);
		depot.setDepotName(depotName);
		depot.setLastUpdated(lastUpdated);
	}

}
