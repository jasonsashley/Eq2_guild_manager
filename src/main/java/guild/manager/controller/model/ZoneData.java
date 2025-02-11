package guild.manager.controller.model;

import java.util.HashSet;
import java.util.Set;

import guild.manager.entity.Item;
import guild.manager.entity.Zone;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZoneData {

	private Long zoneId;
	private String zoneName;
	private Integer zoneLevelLower;
	private Integer zoneLevelUpper;
	private Integer tier;
	private Boolean worldBellAccess;
	private Boolean ulteranSpireAccess;
	private Boolean druidRingAccess;
	private String zoneWikiLink;

	private Set<ItemResponse> items = new HashSet<>();

	public ZoneData(Zone zone) {
		zoneId = zone.getZoneId();
		zoneName = zone.getZoneName();
		zoneLevelUpper = zone.getZoneLevelUpper();
		zoneLevelLower = zone.getZoneLevelLower();
		tier = zone.getTier();
		worldBellAccess = zone.getWorldBellAccess();
		ulteranSpireAccess = zone.getUlteranSpireAccess();
		druidRingAccess = zone.getDruidRingAccess();
		zoneWikiLink = zone.getZoneWikiLink();

		for (Item item : zone.getItems()) {
			items.add(new ItemResponse(item));
		}
	}

}
