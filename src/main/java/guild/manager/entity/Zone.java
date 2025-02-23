package guild.manager.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Zone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long zoneId;
	
	private String zoneName;
	private Integer[] zoneLevelRange;
	private Integer[] tiers;
	private Boolean worldBellAccess;
	private Boolean ulteranSpireAccess;
	private Boolean druidRingAccess;
	private String zoneWikiLink;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(mappedBy = "zones")
	private Set<Info> infos = new HashSet<>();
	
}
