
package guild.manager.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import guild.manager.controller.model.DepotData;
import guild.manager.controller.model.GuildData;
import guild.manager.service.ApiService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ApiController {

	@Autowired
	private ApiService apiService;

	@GetMapping("/")
	public ResponseEntity<?> showEntry() {
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllGuilds()).withRel("guilds"));
		links.add(linkTo(methodOn(ApiController.class).getAllDepots()).withRel("depots"));
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET") //
				.body(links);
	}

	@GetMapping("/guilds")
	public ResponseEntity<?> getAllGuilds() {
		log.info("Retrieving all guilds");
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllGuilds()).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).getAllGuilds()).slash("new").withRel("insert-new"));

		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET") //
				.body(CollectionModel.of(apiService.returnAllGuilds(), links));
	}

	@PostMapping("/guilds/new")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> insertGuild(@RequestBody GuildData guildData) {
		GuildData dataResponse = apiService.saveGuild(guildData);
		return ResponseEntity //
				.created(dataResponse.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.header("Access-Control-Allow-Methods", "POST") //
				.body(dataResponse);
	}

	@GetMapping("/guilds/{id}")
	public ResponseEntity<?> getGuildById(@PathVariable Long id) {
		log.info("Retrieving guild with ID={}", id);
		GuildData response = apiService.returnGuildById(id);
		response.add(linkTo(methodOn(ApiController.class).getGuildById(id)).slash("update").withRel("update"));
		response.add(linkTo(methodOn(ApiController.class).getGuildById(id)).slash("delete").withRel("delete"));
		response.add(linkTo(methodOn(ApiController.class).getGuildById(id)).slash("depots").withRel("get-depots"));
		response.add(linkTo(methodOn(ApiController.class).getGuildById(id)).slash("depots").slash("new")
				.withRel("new-depot"));
		response.add(linkTo(methodOn(ApiController.class).getGuildById(id)).slash("depots").slash("delete")
				.withRel("delete-all-depots"));

		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, PUT, DELETE") //
				.body(response);
	}

	@PutMapping("/guilds/{id}/update")
	public ResponseEntity<?> updateGuild(@PathVariable Long id, @RequestBody GuildData guildData) {
		log.info("Updating guild with ID={}", id);
		guildData.setGuildId(id);
		GuildData response = apiService.saveGuild(guildData);
		return ResponseEntity.ok() //
				.body(response);
	}

	@DeleteMapping("/guilds/{id}/delete")
	public ResponseEntity<?> deleteGuild(@PathVariable Long id) {
		log.info("Deleting guild with ID={}", id);
		apiService.deleteGuild(id);
		return ResponseEntity.ok() //
				.location(linkTo(methodOn(ApiController.class).getAllGuilds()).toUri()) //
				.body(Map.of("message", "guild with ID=" + id + " has been successfully deleted"));
	}

	@GetMapping("/guilds/{id}/depots")
	public ResponseEntity<?> getDepotsFromGuild(@PathVariable Long id) {
		log.info("Retrieving all depots from guild with ID={}", id);
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getDepotsFromGuild(id)).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).getDepotsFromGuild(id)).slash("new").withRel("insert-new"));
		links.add(linkTo(methodOn(ApiController.class).getGuildById(id)).slash("depots").slash("delete")
				.withRel("delete-all-depots"));

		return ResponseEntity.ok() //
				.body(CollectionModel.of(apiService.getDepotsFromGuild(id), links));
	}

	@PostMapping("/guilds/{guildId}/depots/new")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> insertDepot(@PathVariable Long guildId, @RequestBody DepotData data) {
		log.info("Creating depot {} for guild with ID={}", data, guildId);
		DepotData response = apiService.createDepot(guildId, data);
		return ResponseEntity //
				.created(response.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(response);
	}

	@GetMapping("/depots")
	public ResponseEntity<?> getAllDepots() {
		log.info("Retrieving all depots");
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllDepots()).withSelfRel());
		return ResponseEntity.ok() //
				.body(CollectionModel.of(apiService.getAllDepots(), links));
	}

	@GetMapping("/depots/{id}")
	public ResponseEntity<?> getDepotById(@PathVariable Long id) {
		log.info("Retrieving depot with ID={}", id);
		DepotData response = apiService.getDepotById(id);
		response.add(linkTo(methodOn(ApiController.class).getGuildById(response.getGuildId())).withRel("parent-guild"));
		response.add(linkTo(methodOn(ApiController.class).getDepotById(id)).slash("update").withRel("update"));
		response.add(linkTo(methodOn(ApiController.class).getDepotById(id)).slash("delete").withRel("delete"));
		response.add(linkTo(methodOn(ApiController.class).getDepotById(id)).slash("items").withRel("get-items"));
		response.add(
				linkTo(methodOn(ApiController.class).getDepotById(id)).slash("items").slash("new").withRel("new-item"));
		response.add(linkTo(methodOn(ApiController.class).getDepotById(id)).slash("items").slash("delete")
				.withRel("delete-all-items"));
		return ResponseEntity.ok() //
				.body(response);
	}

	@PutMapping("/depots/{id}")
	public ResponseEntity<?> updateDepot(@PathVariable Long id, @RequestBody DepotData data) {
		log.info("Updating depot with ID={}", id);
		data.setDepotId(id);
		return ResponseEntity.ok() //
				.body(apiService.updateDepot(data));
	}

	@DeleteMapping("/depots/{id}")
	public ResponseEntity<?> depleteDepot(@PathVariable Long id) {
		log.info("Deleting depot with ID={}", id);
		apiService.deleteDepot(id);
		return ResponseEntity.ok() //
				.body(Map.of("message", "depot with ID=" + id + " was successfully deleted"));
	}
}
