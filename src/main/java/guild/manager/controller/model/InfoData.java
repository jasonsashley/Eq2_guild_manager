package guild.manager.controller.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import guild.manager.controller.ApiController;
import guild.manager.entity.Info;
import guild.manager.entity.Source;
import guild.manager.entity.Zone;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class InfoData extends RepresentationModel<InfoData> {

	private Long infoId;
	private String itemName;
	private Integer itemLevel;
	private String itemDescription;

	private Set<ZoneResponse> zones = new HashSet<>();
	private Set<SourceResponse> sources = new HashSet<>();

	public InfoData(Info info) {
		infoId = info.getInfoId();
		itemName = info.getItemName();
		itemLevel = info.getItemLevel();
		itemDescription = info.getItemDescription();

		for (Zone zone : info.getZones()) {
			zones.add(new ZoneResponse(zone));
		}

		for (Source source : info.getSources()) {
			sources.add(new SourceResponse(source));
		}
		
		this.add(linkTo(methodOn(ApiController.class).getInfoById(infoId)).withSelfRel());	
	}
	
	public InfoData(String itemName, Integer itemLevel, String itemDescription) {
		this.itemName = itemName;
		this.itemLevel = itemLevel;
		this.itemDescription = itemDescription;
	}
	
	public Info toInfo() {
		Info response = new Info();
		response.setItemDescription(itemDescription);
		response.setInfoId(infoId);
		response.setItemLevel(itemLevel);
		response.setItemName(itemName);
		return response;
	}
}