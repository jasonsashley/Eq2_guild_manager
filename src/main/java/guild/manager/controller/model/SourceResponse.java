package guild.manager.controller.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import guild.manager.controller.ApiController;
import guild.manager.entity.Source;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SourceResponse extends RepresentationModel<SourceResponse>{

	private Long sourceId;
	private String sourceName;
	private String sourceType;
	private String sourceWikiLink;

	public SourceResponse(Source source) {
		sourceId = source.getSourceId();
		sourceName = source.getSourceName();
		sourceType = source.getSourceType();
		sourceWikiLink = source.getSourceWikiLink();
		
		this.add(linkTo(methodOn(ApiController.class).getSourceById(sourceId)).withSelfRel());
	}

	public Source toSource() {
		Source source = new Source();
		source.setSourceId(sourceId);
		source.setSourceName(sourceName);
		source.setSourceType(sourceType);
		source.setSourceWikiLink(sourceWikiLink);

		return source;
	}
}

