package guild.manager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import guild.manager.entity.Zone;

public interface ZoneDao extends JpaRepository<Zone, Long> {

}
