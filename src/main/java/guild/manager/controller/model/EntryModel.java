package guild.manager.controller.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EntryModel extends RepresentationModel<EntryModel> {
	
	private Long version;
	private String description;

}
