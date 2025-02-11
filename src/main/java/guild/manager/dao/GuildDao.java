package guild.manager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import guild.manager.entity.Guild;

public interface GuildDao extends JpaRepository<Guild, Long> {

}
