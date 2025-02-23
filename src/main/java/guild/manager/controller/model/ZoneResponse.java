package guild.manager.controller.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import guild.manager.controller.ApiController;
import guild.manager.entity.Zone;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ZoneResponse extends RepresentationModel<ZoneResponse>{
	
	private Long zoneId;
	private String zoneName;
	private String zoneWikiLink;
	
	public ZoneResponse(Zone zone) {
		zoneId = zone.getZoneId();
		zoneName = zone.getZoneName();
		zoneWikiLink = zone.getZoneWikiLink();
		this.add(linkTo(methodOn(ApiController.class).getZoneById(zoneId)).withSelfRel());
	}

}
