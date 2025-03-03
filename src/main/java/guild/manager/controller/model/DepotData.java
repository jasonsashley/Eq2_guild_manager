package guild.manager.controller.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import guild.manager.controller.ApiController;
import guild.manager.entity.Depot;
import guild.manager.entity.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DepotData extends RepresentationModel<DepotData>{

	private Long depotId;
	private String depotName;
	private LocalDateTime lastUpdated;
	
	private String guildName;
	private Long guildId;
	private Set<ItemResponse> items = new HashSet<>();

	public DepotData(Depot depot) {
		depotId = depot.getDepotId();
		depotName = depot.getDepotName();
		lastUpdated = depot.getLastUpdated();
		guildName = depot.getGuild().getGuildName();
		guildId = depot.getGuild().getGuildId();

		for (Item item : depot.getItems()) {
			items.add(new ItemResponse(item));
		}
		
		this.add(linkTo(methodOn(ApiController.class).getDepotById(depotId)).withSelfRel());
	}

	public void toDepot(Depot depot) {
		depot.setDepotId(depotId);
		depot.setDepotName(depotName);
		depot.setLastUpdated(lastUpdated);
	}

}
