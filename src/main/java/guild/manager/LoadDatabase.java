package guild.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import guild.manager.controller.ApiController;
import guild.manager.controller.model.InfoData;
import guild.manager.controller.model.SourceData;
import guild.manager.controller.model.ZoneData;
import guild.manager.dao.GuildDao;
import guild.manager.dao.InfoDao;
import guild.manager.dao.SourceDao;
import guild.manager.dao.ZoneDao;
import guild.manager.entity.Guild;

@Configuration
public class LoadDatabase {

	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	CommandLineRunner initDatabase(GuildDao testDao, InfoDao infoDao, SourceDao sourceDao, ZoneDao zoneDao) {

		return args -> {
			log.info("Preloading " + testDao.save(new Guild("Test")));
			log.info("Preloading " + testDao.save(new Guild("Wolvesgarden")));

			for (InfoData info : infoArray) {
				log.info("Preloading " + infoDao.save(info.toInfo()));
			}

			for (SourceData source : sourceArray) {
				log.info("Preloading " + sourceDao.save(source.toSource()));
			}

			for (ZoneData zone : zoneArray) {
				log.info("Preloading " + zoneDao.save(zone.toZone()));
			}
		};
	}

	public static void buildConnections() {
		ApiController control = new ApiController();
		control.attachSourceToInfo(1L, 1L);
	}

	private InfoData[] infoArray = { //
			new InfoData( //
					"Gold Cluster", //
					20, //
					"This looks as if it could be used for crafting level 20 - 29 handcrafted items."),

			new InfoData("Silver Cluster", //
					10, //
					"This rare harvest looks as if it could be used for crafting level 10 - 19 mastercrafted items."),

			new InfoData( //
					"Duck feather", //
					10, //
					"a feather from a duck.") };

	private SourceData[] sourceArray = { //
			new SourceData( //
					"Gemstone node", //
					"Harvesting", //
					"https://eq2.fandom.com/wiki/Gemstone_Nodes"),

			new SourceData( //
					"Feather collection", //
					"Collections", //
					"https://eq2.fandom.com/wiki/Feather_collection"),

			new SourceData( //
					"Duck and purple", //
					"Collections", //
					"https://eq2.fandom.com/wiki/Duck_and_purple"),

			new SourceData( //
					"Feature", //
					"Collections", //
					"https://eq2.fandom.com/wiki/Features") };

	private ZoneData[] zoneArray = { //
			new ZoneData( //
					"Antonica", //
					new Integer[] { 10, 20 }, //
					new Integer[] { 2 }, //
					true, true, true, //
					"https://eq2.fandom.com/wiki/Antonica"),

			new ZoneData( //
					"The Commonlands", //
					new Integer[] { 10, 20 }, //
					new Integer[] { 2 }, //
					true, true, true, //
					"https://eq2.fandom.com/wiki/The_Commonlands"),

			new ZoneData( //
					"Greater Faydark", //
					new Integer[] { 1, 20 }, //
					new Integer[] { 1, 2 }, //
					true, true, true, //
					"https://eq2.fandom.com/wiki/Greater_Faydark"),

			new ZoneData( //
					"Darklight Wood", //
					new Integer[] { 1, 20 }, //
					new Integer[] { 1, 2 }, //
					false, false, true, //
					"https://eq2.fandom.com/wiki/Darklight_Wood"),

			new ZoneData( //
					"Timorous Deep", //
					new Integer[] { 1, 20 }, //
					new Integer[] { 1, 2 }, //
					true, false, false, //
					"https://eq2.fandom.com/wiki/Timorous_Deep"),

			new ZoneData( //
					"Frostfang Sea", //
					new Integer[] { 1, 20 }, //
					new Integer[] { 1, 2 }, //
					true, false, false, //
					"https://eq2.fandom.com/wiki/Frostfang_Sea"),

			new ZoneData( //
					"Qeynos Districts", //
					new Integer[] { 1, 15 }, //
					new Integer[] { 1 }, //
					true, false, false, //
					"https://eq2.fandom.com/wiki/Qeynos_Outlying_Areas_Timeline"),

			new ZoneData( //
					"Freeport Districts", //
					new Integer[] { 1, 15 }, //
					new Integer[] { 1 }, //
					true, false, false, //
					"https://eq2.fandom.com/wiki/Freeport_Outlying_Areas_Timeline"),

			new ZoneData( //
					"The Thundering Stepps", //
					new Integer[] { 20, 30 }, //
					new Integer[] { 3 }, //
					true, true, false, //
					"https://eq2.fandom.com/wiki/The_Thundering_Steppes"), 
			
			new ZoneData( //
					"Nektulos Forest", //
					new Integer[] { 20, 30 }, //
					new Integer[] { 3 }, //
					true, true, true, //
					"https://eq2.fandom.com/wiki/Nektulos_Forest"), 
			
			new ZoneData( //
					"Butcherblock Mountains", //
					new Integer[] { 20, 35 }, //
					new Integer[] { 3 }, //
					true, false, true, //
					"https://eq2.fandom.com/wiki/Butcherblock_Mountains")

	};

}
