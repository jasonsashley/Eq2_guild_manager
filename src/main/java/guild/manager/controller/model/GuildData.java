package guild.manager.controller.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import guild.manager.controller.ApiController;
import guild.manager.entity.Depot;
import guild.manager.entity.Guild;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GuildData extends RepresentationModel<GuildData> {

	private Long guildId;
	private String guildName;
	private Set<DepotResponse> depots = new HashSet<>();

	public GuildData(Guild guild) {
		this.guildId = guild.getGuildId();
		this.guildName = guild.getGuildName();

		for (Depot depot : guild.getDepots()) {
			depots.add(new DepotResponse(depot));
		}
		this.add(linkTo(methodOn(ApiController.class).getGuildById(guildId)).withSelfRel());
	}

	public Guild toGuild(Guild guild) {
		guild.setGuildId(guildId);
		guild.setGuildName(guildName);
		return guild;
	}
}