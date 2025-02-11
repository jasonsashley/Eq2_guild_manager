package guild.manager.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guild {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long guildId;
	
	private String guildName;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "guild", cascade = CascadeType.ALL)
	private Set<Depot> depots = new HashSet<>();
	
	public Guild(String name) {
		guildName = name;
	}
}
