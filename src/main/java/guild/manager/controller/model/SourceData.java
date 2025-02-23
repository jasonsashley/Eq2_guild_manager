package guild.manager.controller.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.LinkedList;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import guild.manager.controller.ApiController;
import guild.manager.entity.Info;
import guild.manager.entity.Source;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SourceData extends RepresentationModel<SourceData> {

	private Long sourceId;
	private String sourceName;
	private String sourceType;
	private String sourceWikiLink;
	
	private List<InfoResponse> infos = new LinkedList<>();
	
	public SourceData(Source source) {
		sourceId = source.getSourceId();
		sourceName = source.getSourceName();
		sourceType = source.getSourceType();
		sourceWikiLink = source.getSourceWikiLink();
		
		for (Info info : source.getInfos()) {
			infos.add(new InfoResponse(info));
		}
		this.add(linkTo(methodOn(ApiController.class).getSourceById(sourceId)).withSelfRel());
	}
	
	public SourceData(String sourceName, String sourceType, String sourceWikiLink) {
		this.sourceName = sourceName;
		this.sourceType = sourceType;
		this.sourceWikiLink = sourceWikiLink;
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
