package guild.manager.controller.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import guild.manager.controller.ApiController;
import guild.manager.entity.Info;
import guild.manager.entity.Zone;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ZoneData extends RepresentationModel<ZoneData>{

	private Long zoneId;
	private String zoneName;
	private Integer[] zoneLevelRange;
	private Integer[] tiers;
	private Boolean worldBellAccess;
	private Boolean ulteranSpireAccess;
	private Boolean druidRingAccess;
	private String zoneWikiLink;

	private Set<InfoResponse> items = new HashSet<>();

	public ZoneData(Zone zone) {
		zoneId = zone.getZoneId();
		zoneName = zone.getZoneName();
		zoneLevelRange = zone.getZoneLevelRange();
		tiers = zone.getTiers();
		worldBellAccess = zone.getWorldBellAccess();
		ulteranSpireAccess = zone.getUlteranSpireAccess();
		druidRingAccess = zone.getDruidRingAccess();
		zoneWikiLink = zone.getZoneWikiLink();

		for (Info item : zone.getInfos()) {
			items.add(new InfoResponse(item));
		}
		this.add(linkTo(methodOn(ApiController.class).getZoneById(zoneId)).withSelfRel());
	}

	public ZoneData(String zoneName, Integer[] zoneLevelRange, Integer[] tiers, Boolean worldBellAccess,
			Boolean ulteranSpireAccess, Boolean druidRingAccess, String zoneWikiLink) {
		this.zoneName = zoneName;
		this.zoneLevelRange = zoneLevelRange;
		this.tiers = tiers;
		this.worldBellAccess = worldBellAccess;
		this.ulteranSpireAccess = ulteranSpireAccess;
		this.druidRingAccess = druidRingAccess;
		this.zoneWikiLink = zoneWikiLink;
	}

	public Zone toZone() {
		Zone zone = new Zone();
		zone.setDruidRingAccess(druidRingAccess);
		zone.setTiers(tiers);
		zone.setUlteranSpireAccess(ulteranSpireAccess);
		zone.setWorldBellAccess(worldBellAccess);
		zone.setZoneId(zoneId);
		zone.setZoneLevelRange(zoneLevelRange);
		zone.setZoneName(zoneName);
		zone.setZoneWikiLink(zoneWikiLink);
		return zone;
	}

}
