package guild.manager.controller.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import guild.manager.controller.ApiController;
import guild.manager.entity.Info;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class InfoResponse extends RepresentationModel<InfoResponse>{
	private Long infoId;
	private String itemName;
	private Integer itemLevel;
	private String itemDescription;
	
	public InfoResponse(Info info) {
		infoId = info.getInfoId();
		itemName = info.getItemName();
		itemLevel = info.getItemLevel();
		itemDescription = info.getItemDescription();
		
		this.add(linkTo(methodOn(ApiController.class).getInfoById(infoId)).withSelfRel());	
	}
}
