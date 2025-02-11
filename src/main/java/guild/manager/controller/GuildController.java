package guild.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import guild.manager.controller.model.DepotData;
import guild.manager.controller.model.GuildData;
import guild.manager.service.GuildService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/guilds")
public class GuildController {

	@Autowired
	GuildService guildService;

	@GetMapping("")
	public List<GuildData> getAllGuilds() {
		log.info("Retrieving all guilds");
		return guildService.returnAllGuilds();
	}

	@PostMapping("")
	@ResponseStatus(code = HttpStatus.CREATED)
	public GuildData insertGuild(@RequestBody GuildData guildData) {
		log.info("Creating guild {}", guildData);
		return guildService.saveGuild(guildData);
	}

	@DeleteMapping("")
	@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
	public void deleteAllGuilds() {
		log.info("Attempt to delete all guilds");
		throw new UnsupportedOperationException("Deleting all guilds is not allowed");
	}

	@GetMapping("/{guildId}")
	public GuildData getGuildById(@PathVariable Long guildId) {
		log.info("Retrieving guild with ID={}", guildId);
		return guildService.returnGuildById(guildId);
	}

	@PutMapping("/{guildId}")
	public GuildData updateGuildById(@PathVariable Long guildId, @RequestBody GuildData guildData) {
		guildData.setGuildId(guildId);
		log.info("Updating guild {}", guildData);
		return guildService.saveGuild(guildData);
	}

	@DeleteMapping("/{guildId}")
	public Map<String, String> deleteGuild(@PathVariable Long guildId) {
		log.info("Deleting guild with ID={}", guildId);
		guildService.deleteGuild(guildId);
		return Map.of("message", "guild with ID=" + guildId + " hes been successfully deleted");
	}

	@GetMapping("/{guildId}/depots")
	public List<DepotData> getAllDepots(@PathVariable Long guildId) {
		log.info("Getting all depots from guild with ID={}", guildId);
		return guildService.returnDepots(guildId);
	}

	@PostMapping("/{guildId}/depots")
	public DepotData insertDepot(@PathVariable Long guildId, @RequestBody DepotData depotData) {
		log.info("Create depot {} for guild with ID={}", depotData, guildId);
		return guildService.saveDepot(depotData, guildId);
	}

	@DeleteMapping("/{guildId}/depots")
	public Map<String, String> deleteAllDepots(@PathVariable Long guildId) {
		log.info("Deleting all depots from guild with ID={}", guildId);
		guildService.deleteAllDepots(guildId);
		return Map.of("message", "all depots from guild with ID=" + guildId + " has been successfully deleted");
	}

	@GetMapping("/{guildId}/depots/{depotId}")
	public DepotData getDepotById(@PathVariable Long guildId, @PathVariable Long depotId) {
		log.info("Retriving depot with ID={} from guild with ID={}", depotId, guildId);
		return guildService.returnDepotById(depotId, guildId);
	}

	@PutMapping("/{guildId}/depots/{depotId}")
	public DepotData updateDepotById(@PathVariable Long guildId, @PathVariable Long depotId,
			@RequestBody DepotData depotData) {
		log.info("Updating depot with ID={} from guild with ID={}", depotId, guildId);
		depotData.setDepotId(depotId);
		return guildService.saveDepot(depotData, guildId);
	}

	@DeleteMapping("/{guildId}/depots/{depotId}")
	public Map<String, String> deleteDepotById(@PathVariable Long guildId, @PathVariable Long depotId) {
		log.info("Deleting depot with ID={} from guild with ID={}", depotId, guildId);
		guildService.deleteDepot(depotId, guildId);
		return Map.of("message",
				"Depot with ID=" + depotId + " has been successfully deleted from guild with ID=" + guildId);
	}
}
