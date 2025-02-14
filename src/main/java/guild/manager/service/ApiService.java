package guild.manager.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guild.manager.controller.model.DepotData;
import guild.manager.controller.model.DepotResponse;
import guild.manager.controller.model.GuildData;
import guild.manager.dao.DepotDao;
import guild.manager.dao.GuildDao;
import guild.manager.entity.Depot;
import guild.manager.entity.Guild;

@Service
public class ApiService {

	@Autowired
	private GuildDao guildDao;

	@Autowired
	private DepotDao depotDao;

	@Transactional(readOnly = true)
	public List<GuildData> returnAllGuilds() {
		return guildDao.findAll().stream().map(GuildData::new).toList();
	}

	@Transactional(readOnly = true)
	public GuildData returnGuildById(Long id) {
		return new GuildData(findGuildById(id));
	}

	@Transactional(readOnly = false)
	public GuildData saveGuild(GuildData guildData) {
		Long guildId = guildData.getGuildId();
		Guild guild = findOrCreateGuild(guildId);
		guildData.toGuild(guild);

		return new GuildData(guildDao.save(guild));
	}

	@Transactional(readOnly = false)
	public void deleteGuild(Long id) {
		guildDao.delete(findGuildById(id));
	}

	private Guild findGuildById(Long id) {
		return guildDao.findById(id) //
				.orElseThrow(() -> new NoSuchElementException("Guild with ID=" + id + " was not found"));
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

	@Transactional(readOnly = true)
	public List<DepotResponse> getDepotsFromGuild(Long id) {
		return depotDao.findAll().stream().map(DepotResponse::new).toList();
	}

	@Transactional(readOnly = false)
	public DepotData createDepot(Long guildId, DepotData data) {
		Guild guild = findGuildById(guildId);
		Depot depot = new Depot();
		data.toDepot(depot);

		guild.getDepots().add(depot);
		depot.setGuild(guild);

		return new DepotData(depotDao.save(depot));
	}

	@Transactional(readOnly = true)
	public List<DepotData> getAllDepots() {
		return depotDao.findAll().stream().map(DepotData::new).toList();
	}

	@Transactional(readOnly = true)
	public DepotData getDepotById(Long id) {
		return new DepotData(findDepotById(id));
	}
	
	@Transactional(readOnly = false)
	public DepotData updateDepot(DepotData data) {
		Depot depot = findDepotById(data.getDepotId());
		data.toDepot(depot);
		return new DepotData(depotDao.save(depot));
	}

	@Transactional(readOnly = false)
	public void deleteDepot(Long id) {
		depotDao.delete(findDepotById(id));
	}

	private Depot findDepotById(Long id) {
		return depotDao.findById(id) //
				.orElseThrow(() -> new NoSuchElementException("Depot with ID=" + id + " was not found"));
	}

}
