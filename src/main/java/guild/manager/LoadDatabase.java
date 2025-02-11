package guild.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import guild.manager.dao.GuildDao;
import guild.manager.entity.Guild;

@Configuration
public class LoadDatabase {
	
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
	
	@Bean
	CommandLineRunner initDatabase(GuildDao testDao) {
		return args -> {
			log.info("Preloading " + testDao.save(new Guild("Test")));
			log.info("Preloading " + testDao.save(new Guild("Wolvesgarden")));
		};
	}

}
