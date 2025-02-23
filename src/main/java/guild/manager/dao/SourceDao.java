package guild.manager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import guild.manager.entity.Source;

public interface SourceDao extends JpaRepository<Source, Long> {

}
