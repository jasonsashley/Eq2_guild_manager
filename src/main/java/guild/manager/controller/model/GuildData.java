package guild.manager.controller.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import guild.manager.entity.Depot;
import guild.manager.entity.Guild;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GuildData {

	private Long guildId;
	private String guildName;
	private Set<DepotResponse> depots = new HashSet<>();

	public GuildData(Guild guild) {
		this.guildId = guild.getGuildId();
		guildName = guild.getGuildName();

		for (Depot depot : guild.getDepots()) {
			depots.add(new DepotResponse(depot));
		}
	}

	public Guild toGuild(Guild guild) {
		guild.setGuildId(guildId);
		guild.setGuildName(guildName);
		return guild;
	}

	@Data
	@NoArgsConstructor
	public class DepotResponse {
		private Long depotId;
		private String depotName;
		private LocalDateTime lastUpdated;

		public DepotResponse(Depot depot) {
			depotId = depot.getDepotId();
			depotName = depot.getDepotName();
			lastUpdated = depot.getLastUpdated();
		}

	}
}