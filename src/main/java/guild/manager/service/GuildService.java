package guild.manager.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guild.manager.controller.model.DepotData;
import guild.manager.controller.model.GuildData;
import guild.manager.dao.DepotDao;
import guild.manager.dao.GuildDao;
import guild.manager.entity.Depot;
import guild.manager.entity.Guild;

@Service
public class GuildService {

	@Autowired
	GuildDao guildDao;

	@Autowired
	DepotDao depotDao;

	@Transactional(readOnly = true)
	public List<GuildData> returnAllGuilds() {
		List<Guild> guilds = guildDao.findAll();
		List<GuildData> result = new LinkedList<>();

		for (Guild guild : guilds) {
			result.add(new GuildData(guild));
			result.get(result.size() - 1).setDepots(null);
		}
		return guildDao.findAll().stream().map(GuildData::new).toList();
	}

	@Transactional(readOnly = false)
	public GuildData saveGuild(GuildData guildData) {
		Long guildId = guildData.getGuildId();
		Guild guild = findOrCreateGuild(guildId);
		guildData.toGuild(guild);

		return new GuildData(guildDao.save(guild));
	}

	@Transactional(readOnly = true)
	public GuildData returnGuildById(Long guildId) {
		return new GuildData(findGuildById(guildId));
	}

	@Transactional(readOnly = false)
	public void deleteGuild(Long guildId) {
		guildDao.delete(findGuildById(guildId));
	}

	private Guild findOrCreateGuild(Long guildId) {
		Guild guild;
		if (Objects.isNull(guildId)) {
			guild = new Guild();
		} else {
			guild = findGuildById(guildId);
		}
		return guild;
	}

	private Guild findGuildById(Long guildId) {
		return guildDao.findById(guildId) //
				.orElseThrow(() -> new NoSuchElementException("Guild with ID=" + guildId + " was not found"));
	}

	@Transactional(readOnly = true)
	public List<DepotData> returnDepots(Long guildId) {
		List<Depot> depots = depotDao.findAll();
		List<DepotData> result = new LinkedList<>();

		for (Depot depot : depots) {
			result.add(new DepotData(depot));
		}
		return depotDao.findAll().stream().map(DepotData::new).toList();
	}

	@Transactional(readOnly = false)
	public DepotData saveDepot(DepotData depotData, Long guildId) {
		Guild guild = findGuildById(guildId);
		Long depotId = depotData.getDepotId();
		Depot depot = findOrCreateDepot(depotId, guildId);

		depotData.toDepot(depot);
		relateGuildAndDepot(depot, guild);

		return new DepotData(depot);
	}

	@Transactional(readOnly = false)
	public void deleteAllDepots(Long guildId) {
		Guild guild = findGuildById(guildId);
		for (Depot depot : guild.getDepots()) {
			depotDao.delete(depot);
		}
	}

	@Transactional(readOnly = true)
	public DepotData returnDepotById(Long depotId, Long guildId) {
		return new DepotData(findDepotById(depotId, guildId));
	}

	@Transactional(readOnly = false)
	public void deleteDepot(Long depotId, Long guildId) {
		depotDao.delete(findDepotById(depotId, guildId));
	}

	private Depot findOrCreateDepot(Long depotId, Long guildId) {
		Depot depot;
		if (Objects.isNull(depotId)) {
			depot = new Depot();
		} else {
			depot = findDepotById(depotId, guildId);
		}
		return depot;
	}

	private Depot findDepotById(Long depotId, Long guildId) {
		Depot depot = depotDao.findById(depotId)
				.orElseThrow(() -> new NoSuchElementException("Depot with ID=" + depotId + " was not found"));
		Guild attachedGuild = depot.getGuild();
		if (Objects.nonNull(attachedGuild) && attachedGuild.getGuildId() != guildId) {
			throw new IllegalArgumentException(
					"Depot with ID=" + depotId + " is not owned by guild with ID=" + guildId);
		}
		return depot;
	}

	private void relateGuildAndDepot(Depot depot, Guild guild) {
		depot.setGuild(guild);
		guild.getDepots().add(depot);
	}

}
