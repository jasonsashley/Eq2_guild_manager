package guild.manager.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guild.manager.controller.model.DepotData;
import guild.manager.controller.model.DepotResponse;
import guild.manager.controller.model.GuildData;
import guild.manager.controller.model.InfoData;
import guild.manager.controller.model.ItemData;
import guild.manager.controller.model.ItemResponse;
import guild.manager.controller.model.SourceData;
import guild.manager.controller.model.SourceResponse;
import guild.manager.controller.model.ZoneData;
import guild.manager.dao.DepotDao;
import guild.manager.dao.GuildDao;
import guild.manager.dao.InfoDao;
import guild.manager.dao.ItemDao;
import guild.manager.dao.SourceDao;
import guild.manager.dao.ZoneDao;
import guild.manager.entity.Depot;
import guild.manager.entity.Guild;
import guild.manager.entity.Info;
import guild.manager.entity.Item;
import guild.manager.entity.Source;
import guild.manager.entity.Zone;

@Service
public class ApiService {

	@Autowired
	private GuildDao guildDao;
	@Autowired
	private DepotDao depotDao;
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private InfoDao infoDao;
	@Autowired
	private SourceDao sourceDao;
	@Autowired
	private ZoneDao zoneDao;

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

	@Transactional(readOnly = true)
	public List<ItemResponse> getItemsFromDepot(Long id) {
		Depot depot = findDepotById(id);
		List<ItemResponse> response = new LinkedList<>();
		for (Item item : depot.getItems()) {
			response.add(new ItemResponse(item));
		}
		return response;
	}

	@Transactional(readOnly = true)
	public List<ItemResponse> getAllItems() {
		return itemDao.findAll().stream().map(ItemResponse::new).toList();
	}

	@Transactional(readOnly = false)
	public ItemData createItem(Long id, ItemData data) {
		Depot depot = findDepotById(id);
		Item item = new Item();
		Info info = findInfoByName(data.getItemName());
		data.toItem(item);

		item.setInfo(info);
		info.getItems().add(item);

		depot.getItems().add(item);
		item.setDepot(depot);

		return new ItemData(itemDao.save(item));

	}

	@Transactional(readOnly = true)
	public ItemData getItemById(Long id) {
		return new ItemData(findItemById(id));
	}

	@Transactional(readOnly = false)
	public ItemData updateItem(ItemData data) {
		Item item = findItemById(data.getItemId());
		data.toItem(item);
		return new ItemData(itemDao.save(item));
	}

	@Transactional(readOnly = false)
	public void deleteItem(Long id) {
		itemDao.delete(findItemById(id));
	}

	private Info findInfoByName(String itemName) {
		return infoDao.findByItemName(itemName) //
				.orElseThrow(() -> new NoSuchElementException("Item info with NAME=" + itemName + " was not found"));
	}

	private Item findItemById(Long id) {
		return itemDao.findById(id) //
				.orElseThrow(() -> new NoSuchElementException("Item with ID=" + id + " was not found"));
	}

	@Transactional(readOnly = true)
	public List<InfoData> getAllInfos() {
		return infoDao.findAll().stream().map(InfoData::new).toList();
	}

	@Transactional(readOnly = true)
	public InfoData getInfoById(Long id) {
		return new InfoData(findInfoById(id));
	}

	@Transactional(readOnly = false)
	public InfoData createInfo(InfoData data) {
		return new InfoData(infoDao.save(data.toInfo()));
	}

	private Info findInfoById(Long id) {
		return infoDao.findById(id) //
				.orElseThrow(() -> new NoSuchElementException("Item info with ID=" + id + " was not found"));
	}

	@Transactional(readOnly = false)
	public void deleteInfo(Long id) {
		infoDao.delete(findInfoById(id));
	}

	@Transactional(readOnly = true)
	public List<SourceResponse> getSourcesFromInfo(Long id) {
		Info info = findInfoById(id);
		List<SourceResponse> result = new LinkedList<>();
		for (Source source : info.getSources()) {
			result.add(new SourceResponse(source));
		}
		return result;
	}

	@Transactional(readOnly = false)
	public InfoData attachSourceToInfo(Long sourceId, Long infoId) {
		Info info = findInfoById(infoId);
		Source source = findSourceById(sourceId);

		if (info.getSources().contains(source)) {
			throw new NoSuchElementException("Source with ID=" + source.getSourceId()
					+ " is already attached to item info with ID=" + info.getInfoId());
		}

		info.getSources().add(source);
		source.getInfos().add(info);
		Info newInfo = infoDao.save(info);
		InfoData data = new InfoData(newInfo);

		return data;
	}

	@Transactional(readOnly = false)
	public InfoData detachSourceToInfo(Long sourceId, Long infoId) {
		Info info = findInfoById(infoId);
		Source source = findSourceById(sourceId);

		if (!info.getSources().contains(source)) {
			throw new NoSuchElementException("Source with ID=" + source.getSourceId()
					+ " is already detached from item info with ID=" + info.getInfoId());
		}

		info.getSources().remove(source);
		source.getInfos().remove(info);

		sourceDao.save(source);
		return new InfoData(infoDao.save(info));
	}

	private Source findSourceById(Long id) {
		return sourceDao.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Source with ID=" + id + " was not found"));
	}

	@Transactional(readOnly = true)
	public List<SourceData> getAllSources() {
		return sourceDao.findAll().stream().map(SourceData::new).toList();
	}

	@Transactional(readOnly = false)
	public SourceData createSource(SourceData data) {
		return new SourceData(sourceDao.save(data.toSource()));
	}

	@Transactional(readOnly = true)
	public SourceData getSourceById(Long id) {
		return new SourceData(findSourceById(id));
	}

	@Transactional(readOnly = false)
	public void deleteSource(Long id) {
		sourceDao.delete(findSourceById(id));
	}

	@Transactional(readOnly = true)
	public List<ZoneData> getZonesFromInfo(Long id) {
		return findInfoById(id).getZones().stream().map(ZoneData::new).toList();
	}

	@Transactional(readOnly = false)
	public InfoData attachZoneToInfo(Long zoneId, Long infoId) {
		Info info = findInfoById(infoId);
		Zone zone = findZoneById(zoneId);

		if (info.getZones().contains(zone)) {
			throw new NoSuchElementException(
					"Zone with ID=" + zoneId + " is already attached to item info with ID=" + infoId);
		}

		info.getZones().add(zone);
		zone.getInfos().add(info);

		return new InfoData(infoDao.save(info));
	}

	private Zone findZoneById(Long id) {
		return zoneDao.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Zone with ID=" + id + " was not found"));
	}

	@Transactional(readOnly = false)
	public InfoData detachZoneFromInfo(Long zoneId, Long infoId) {
		Zone zone = findZoneById(zoneId);
		Info info = findInfoById(infoId);

		if (!info.getZones().contains(zone)) {
			throw new NoSuchElementException(
					"Zone with ID=" + zoneId + " is already detached from item info with ID=" + infoId);
		}

		zone.getInfos().remove(info);
		info.getZones().remove(zone);

		zoneDao.save(zone);
		return new InfoData(infoDao.save(info));
	}

	@Transactional(readOnly = true)
	public List<ZoneData> getAllZones() {
		return zoneDao.findAll().stream().map(ZoneData::new).toList();
	}

	@Transactional(readOnly = false)
	public ZoneData createZone(ZoneData data) {
		return new ZoneData(zoneDao.save(data.toZone()));
	}

	@Transactional(readOnly = true)
	public ZoneData getZoneById(Long id) {
		return new ZoneData(findZoneById(id));
	}

	@Transactional(readOnly = false)
	public void deleteZone(Long id) {
		zoneDao.delete(findZoneById(id));	
	}

}
