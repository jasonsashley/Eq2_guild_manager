package guild.manager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import guild.manager.entity.Item;

public interface ItemDao extends JpaRepository<Item, Long> {

}
