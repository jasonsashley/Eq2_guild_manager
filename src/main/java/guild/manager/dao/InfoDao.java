package guild.manager.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import guild.manager.entity.Info;

public interface InfoDao extends JpaRepository<Info, Long> {

	
	Optional<Info> findByItemName(String itemName);
	
}
