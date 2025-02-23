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
public class Source {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sourceId;
	
	private String sourceName;
	private String sourceType;
	private String sourceWikiLink;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(mappedBy = "sources")
	private Set<Info> infos = new HashSet<>();

}
