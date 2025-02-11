package guild.manager.controller.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import guild.manager.entity.Item;
import guild.manager.entity.Source;
import guild.manager.entity.Tag;
import guild.manager.entity.Zone;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemData {

	private Long itemId;
	private String itemName;
	private Integer itemAmount;
	private Integer itemLevel;
	private String itemDescription;
	private String itemNotes;
	private LocalDateTime lastUpdated;

	private Set<String> tags = new HashSet<>();
	private Set<ZoneResponse> zones = new HashSet<>();
	private Set<SourceResponse> sources = new HashSet<>();

	public ItemData(Item item) {
		itemId = item.getItemId();
		itemName = item.getItemName();
		itemAmount = item.getItemAmount();
		itemLevel = item.getItemLevel();
		itemDescription = item.getItemDescription();
		itemNotes = item.getItemNotes();
		lastUpdated = item.getLastUpdated();

		for (Tag tag : item.getTags()) {
			tags.add(tag.toString());
		}

		for (Zone zone : item.getZones()) {
			zones.add(new ZoneResponse(zone));
		}

		for (Source source : item.getSources()) {
			sources.add(new SourceResponse(source));
		}
	}
	
	public Item toItem() {
		Item item = new Item();
		item.setItemAmount(itemAmount);
		item.setItemDescription(itemDescription);
		item.setItemId(itemId);
		item.setItemLevel(itemLevel);
		item.setItemName(itemName);
		item.setItemNotes(itemNotes);
		item.setLastUpdated(lastUpdated);
		
		return item;
	}

	@Data
	@NoArgsConstructor
	public class ZoneResponse {

		private Long zoneId;
		private String zoneName;
		private Integer zoneLevelLower;
		private Integer zoneLevelUpper;
		private Integer tier;
		private Boolean worldBellAccess;
		private Boolean ulteranSpireAccess;
		private Boolean druidRingAccess;
		private String zoneWikiLink;
		
		public ZoneResponse(Zone zone) {
			zoneId = zone.getZoneId();
			zoneName = zone.getZoneName();
			zoneLevelUpper = zone.getZoneLevelUpper();
			zoneLevelLower = zone.getZoneLevelLower();
			tier = zone.getTier();
			worldBellAccess = zone.getWorldBellAccess();
			ulteranSpireAccess = zone.getUlteranSpireAccess();
			druidRingAccess = zone.getDruidRingAccess();
			zoneWikiLink = zone.getZoneWikiLink();
		}
		
		public Zone toZone() {
			Zone zone = new Zone();
			zone.setDruidRingAccess(druidRingAccess);
			zone.setTier(tier);
			zone.setUlteranSpireAccess(ulteranSpireAccess);
			zone.setWorldBellAccess(worldBellAccess);
			zone.setZoneId(zoneId);
			zone.setZoneLevelLower(zoneLevelLower);
			zone.setZoneLevelUpper(zoneLevelUpper);
			zone.setZoneName(zoneName);
			zone.setZoneWikiLink(zoneWikiLink);
			
			return zone;
		}

	}

	@Data
	@NoArgsConstructor
	public class SourceResponse {
		
		private Long sourceId;
		private String sourceName;
		private String sourceWikiLink;
		
		public SourceResponse(Source source) {
			sourceId = source.getSourceId();
			sourceName = source.getSourceName();
			sourceWikiLink = source.getSourceWikiLink();
		}
		
		public Source toSource() {
			Source source = new Source();
			source.setSourceId(sourceId);
			source.setSourceName(sourceName);
			source.setSourceWikiLink(sourceWikiLink);
			
			return source;
		}
	}
}
