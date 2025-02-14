package guild.manager.controller.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import guild.manager.controller.ApiController;
import guild.manager.entity.Depot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DepotResponse extends RepresentationModel<DepotData> {
	private Long depotId;
	private String depotName;
	private LocalDateTime lastUpdated;

	public DepotResponse(Depot depot) {
		depotId = depot.getDepotId();
		depotName = depot.getDepotName();
		lastUpdated = depot.getLastUpdated();

		this.add(linkTo(methodOn(ApiController.class).getDepotById(depotId)).withSelfRel());
	}

}
