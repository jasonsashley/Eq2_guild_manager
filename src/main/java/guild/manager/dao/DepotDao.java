package guild.manager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import guild.manager.entity.Depot;

public interface DepotDao extends JpaRepository<Depot, Long> {

}
