package guild.manager.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Info {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long infoId;
	
	@Column(unique = true)
	private String itemName;
	private Integer itemLevel;
	private String itemDescription;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "info_zone", joinColumns = @JoinColumn(name = "item_info_id"), inverseJoinColumns = @JoinColumn(name = "zone_id"))
	private Set<Zone> zones = new HashSet<>();
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "info_source", joinColumns = @JoinColumn(name = "item_info_id"), inverseJoinColumns = @JoinColumn(name = "source_id"))
	private Set<Source> sources = new HashSet<>();
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
	private Set<Item> items = new HashSet<>();

}
