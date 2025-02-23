
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
import guild.manager.controller.model.InfoData;
import guild.manager.controller.model.ItemData;
import guild.manager.controller.model.SourceData;
import guild.manager.controller.model.ZoneData;
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
		links.add(linkTo(methodOn(ApiController.class).buildInitConnections()).withRel("init-connect"));
		links.add(linkTo(methodOn(ApiController.class).getAllGuilds()).withRel("guilds"));
		links.add(linkTo(methodOn(ApiController.class).getAllDepots()).withRel("depots"));
		links.add(linkTo(methodOn(ApiController.class).getAllItems()).withRel("items"));
		links.add(linkTo(methodOn(ApiController.class).getAllSources()).withRel("sources"));
		links.add(linkTo(methodOn(ApiController.class).getAllZones()).withRel("zones"));
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET") //
				.body(links);
	}

	@GetMapping("/guilds")
	public ResponseEntity<?> getAllGuilds() {
		log.info("Retrieving all guilds");
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllGuilds()).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).showEntry()).withRel("parent"));
		links.add(linkTo(methodOn(ApiController.class).getAllGuilds()).withRel("new-guild"));

		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, POST") //
				.body(CollectionModel.of(apiService.returnAllGuilds(), links));
	}

	private void addGuildLinks(GuildData response) {
		Long id = response.getGuildId();
		response.add(linkTo(methodOn(ApiController.class).getAllGuilds()).withRel("parent"));
		response.add(linkTo(methodOn(ApiController.class).getGuildById(id)).withRel("update"));
		response.add(linkTo(methodOn(ApiController.class).getGuildById(id)).withRel("delete"));
		response.add(linkTo(methodOn(ApiController.class).getGuildById(id)).slash("depots").withRel("get-depots"));
		response.add(linkTo(methodOn(ApiController.class).getGuildById(id)).slash("depots").withRel("new-depot"));
	}

	@PostMapping("/guilds")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> insertGuild(@RequestBody GuildData guildData) {
		GuildData response = apiService.saveGuild(guildData);
		addGuildLinks(response);
		return ResponseEntity //
				.created(response.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.header("Access-Control-Allow-Methods", "GET, PUT, DELETE") //
				.body(response);
	}

	@GetMapping("/guilds/{id}")
	public ResponseEntity<?> getGuildById(@PathVariable Long id) {
		log.info("Retrieving guild with ID={}", id);
		GuildData response = apiService.returnGuildById(id);
		addGuildLinks(response);
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, PUT, DELETE") //
				.body(response);
	}

	@PutMapping("/guilds/{id}")
	public ResponseEntity<?> updateGuild(@PathVariable Long id, @RequestBody GuildData guildData) {
		log.info("Updating guild with ID={}", id);
		guildData.setGuildId(id);
		GuildData response = apiService.saveGuild(guildData);
		addGuildLinks(response);
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, PUT, DELETE") //
				.body(response);
	}

	@DeleteMapping("/guilds/{id}")
	public ResponseEntity<?> deleteGuild(@PathVariable Long id) {
		log.info("Deleting guild with ID={}", id);
		apiService.deleteGuild(id);
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllGuilds()).withRel("parent"));
		return ResponseEntity.ok() //
				.location(linkTo(methodOn(ApiController.class).getAllGuilds()).toUri()) //
				.header("Access-Control-Allow-Methods", "GET, POST") //
				.body(CollectionModel.of( //
						Map.of("message", "guild with ID=" + id + " has been successfully deleted"), links));
	}

	@GetMapping("/guilds/{id}/depots")
	public ResponseEntity<?> getDepotsFromGuild(@PathVariable Long id) {
		log.info("Retrieving all depots from guild with ID={}", id);
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getDepotsFromGuild(id)).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).getGuildById(id)).withRel("parent"));
		links.add(linkTo(methodOn(ApiController.class).getDepotsFromGuild(id)).withRel("new-depot"));

		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, POST") //
				.body(CollectionModel.of(apiService.getDepotsFromGuild(id), links));
	}

	@PostMapping("/guilds/{id}/depots")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> insertDepot(@PathVariable Long id, @RequestBody DepotData data) {
		log.info("Creating depot {} for guild with ID={}", data, id);
		DepotData response = apiService.createDepot(id, data);
		addDepotLinks(response);
		return ResponseEntity //
				.created(response.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.header("Access-Control-Allow-Methods", "GET, PUT, DELETE") //
				.body(response);
	}

	@GetMapping("/depots")
	public ResponseEntity<?> getAllDepots() {
		log.info("Retrieving all depots");
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllDepots()).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).showEntry()).withRel("parent"));
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET")
				.body(CollectionModel.of(apiService.getAllDepots(), links));
	}

	private void addDepotLinks(DepotData response) {
		Long id = response.getDepotId();
		response.add(linkTo(methodOn(ApiController.class).getGuildById(response.getGuildId())).withRel("parent"));
		response.add(linkTo(methodOn(ApiController.class).getDepotById(id)).withRel("update"));
		response.add(linkTo(methodOn(ApiController.class).getDepotById(id)).withRel("delete"));
		response.add(linkTo(methodOn(ApiController.class).getDepotById(id)).slash("items").withRel("get-items"));
		response.add(linkTo(methodOn(ApiController.class).getDepotById(id)).slash("items").withRel("new-item"));
	}

	@GetMapping("/depots/{id}")
	public ResponseEntity<?> getDepotById(@PathVariable Long id) {
		log.info("Retrieving depot with ID={}", id);
		DepotData response = apiService.getDepotById(id);
		addDepotLinks(response);
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, PUT, DELETE") //
				.body(response);
	}

	@PutMapping("/depots/{id}")
	public ResponseEntity<?> updateDepot(@PathVariable Long id, @RequestBody DepotData data) {
		log.info("Updating depot with ID={}", id);
		data.setDepotId(id);
		DepotData response = apiService.updateDepot(data);
		addDepotLinks(response);
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, PUT, DELETE") //
				.body(response);
	}

	@DeleteMapping("/depots/{id}")
	public ResponseEntity<?> deleteDepot(@PathVariable Long id) {
		log.info("Deleting depot with ID={}", id);
		apiService.deleteDepot(id);
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllDepots()).withRel("parent"));
		return ResponseEntity.ok() //
				.location(linkTo(methodOn(ApiController.class).getAllDepots()).toUri()) //
				.header("Access-Control-Allow-Methods", "GET, POST") //
				.body(CollectionModel.of( //
						Map.of("message", "depot with ID=" + id + " has been successfully deleted"), links));
	}

	@GetMapping("/depots/{id}/items")
	public ResponseEntity<?> getItemsFromDepot(@PathVariable Long id) {
		log.info("Retrieving all items from depot with ID={}", id);
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getItemsFromDepot(id)).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).getDepotById(id)).withRel("parent"));
		links.add(linkTo(methodOn(ApiController.class).getItemsFromDepot(id)).withRel("new-item"));

		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, POST")
				.body(CollectionModel.of(apiService.getItemsFromDepot(id), links));
	}

	@PostMapping("depots/{id}/items")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> insertItem(@PathVariable Long id, @RequestBody ItemData data) {
		log.info("Creating item {} for depot with ID={}", data, id);
		ItemData response = apiService.createItem(id, data);
		addItemLinks(response);
		return ResponseEntity //
				.created(response.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.header("Access-Control-Allow-Methods", "GET, PUT, DELETE") //
				.body(response);
	}

	@GetMapping("/items")
	public ResponseEntity<?> getAllItems() {
		log.info("Retrieving all items");
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllItems()).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).showEntry()).withRel("parent"));
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET")
				.body(CollectionModel.of(apiService.getAllItems(), links));

	}

	private void addItemLinks(ItemData response) {
		Long id = response.getItemId();
		Long infoId = response.getInfo().getInfoId();
		response.add(linkTo(methodOn(ApiController.class).getDepotById(response.getDepotId())).withRel("parent"));
		response.add(linkTo(methodOn(ApiController.class).getItemById(id)).withRel("update"));
		response.add(linkTo(methodOn(ApiController.class).getItemById(id)).withRel("delete"));
		response.add(linkTo(methodOn(ApiController.class).getInfoById(infoId)).withRel("get-info"));
		response.add(linkTo(methodOn(ApiController.class).getInfoById(infoId)).slash("sources").withRel("get-sources"));
		response.add(linkTo(methodOn(ApiController.class).getInfoById(infoId)).slash("zones").withRel("get-zones"));
	}

	@GetMapping("/items/{id}")
	public ResponseEntity<?> getItemById(@PathVariable Long id) {
		log.info("Retrieving item with ID={}", id);
		ItemData response = apiService.getItemById(id);
		addItemLinks(response);
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, PUT, DELETE") //
				.body(response);
	}

	@PutMapping("/items/{id}")
	public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody ItemData data) {
		log.info("Updating item with ID={}", id);
		data.setItemId(id);
		ItemData response = apiService.updateItem(data);
		addItemLinks(response);
		return ResponseEntity.ok() //
				.body(response);
	}

	@DeleteMapping("/items/{id}")
	public ResponseEntity<?> deleteItem(@PathVariable Long id) {
		log.info("Deleting item with ID={}", id);
		apiService.deleteItem(id);
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllItems()).withRel("parent"));
		return ResponseEntity.ok() //
				.location(linkTo(methodOn(ApiController.class).getAllItems()).toUri()) //
				.header("Access-Control-Allow-Methods", "GET") //
				.body(CollectionModel.of( //
						Map.of("message", "item with ID=" + id + " has been successfully deleted"), links));
	}

	@GetMapping("/infos")
	public ResponseEntity<?> getAllInfos() {
		log.info("Retrieving all item infos");

		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllInfos()).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).showEntry()).withRel("parent"));
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, POST")
				.body(CollectionModel.of(apiService.getAllInfos(), links));
	}

	@PostMapping("/infos")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> insertInfo(@RequestBody InfoData data) {
		log.info("Creating item info {}", data);
		InfoData response = apiService.createInfo(data);
		addInfoLinks(response);
		return ResponseEntity //
				.created(response.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.header("Access-Control-Allow-Methods", "GET, DELETE") //
				.body(response);

	}

	private void addInfoLinks(InfoData response) {
		Long id = response.getInfoId();
		response.add(linkTo(methodOn(ApiController.class).getAllInfos()).withRel("parent"));
		response.add(linkTo(methodOn(ApiController.class).getInfoById(id)).withRel("delete"));

		response.add(linkTo(methodOn(ApiController.class).getInfoById(id)).slash("sources").withRel("get-sources"));
		response.add(linkTo(methodOn(ApiController.class).getInfoById(id)).slash("sources").slash("{sourceId}")
				.withRel("attach-source"));
		response.add(linkTo(methodOn(ApiController.class).getInfoById(id)).slash("sources").slash("{sourceId}")
				.withRel("detach-source"));

		response.add(linkTo(methodOn(ApiController.class).getInfoById(id)).slash("zones").withRel("get-zones"));
		response.add(linkTo(methodOn(ApiController.class).getInfoById(id)).slash("zones").slash("{zoneId}")
				.withRel("attach-zone"));
		response.add(linkTo(methodOn(ApiController.class).getInfoById(id)).slash("zones").slash("{zoneId}")
				.withRel("detach-zone"));
	}

	@GetMapping("/infos/{id}")
	public ResponseEntity<?> getInfoById(@PathVariable Long id) {
		log.info("Retrieving info with ID={}", id);
		InfoData response = apiService.getInfoById(id);
		addInfoLinks(response);
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, DELETE") //
				.body(response);
	}

	@DeleteMapping("/infos/{id}")
	public ResponseEntity<?> deleteInfo(@PathVariable Long id) {
		log.info("Deleting info with ID={}", id);
		List<Link> links = new LinkedList<Link>();
		apiService.deleteInfo(id);
		links.add(linkTo(methodOn(ApiController.class).getAllInfos()).withRel("parent"));
		return ResponseEntity.ok() //
				.location(linkTo(methodOn(ApiController.class).getAllInfos()).toUri()) //
				.header("Access-Control-Allow-Methods", "GET, POST") //
				.body(CollectionModel.of( //
						Map.of("message", "info with ID=" + id + " has been successfully deleted"), links));
	}

	@GetMapping("/infos/{id}/sources")
	public ResponseEntity<?> getSourcesFromInfo(@PathVariable Long id) {
		log.info("Retrieving all sources from info with ID={}", id);
		List<Link> links = new LinkedList<Link>();
		links.add(linkTo(methodOn(ApiController.class).getSourcesFromInfo(id)).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).getInfoById(id)).withRel("parent"));
		links.add(linkTo(methodOn(ApiController.class).getSourcesFromInfo(id)).slash("{sourceId}")
				.withRel("attach-source"));
		links.add(linkTo(methodOn(ApiController.class).getSourcesFromInfo(id)).slash("{sourceId}")
				.withRel("detach-source"));

		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, POST")
				.body(CollectionModel.of(apiService.getSourcesFromInfo(id), links));
	}

	@PutMapping("/infos/{infoId}/sources/{sourceId}")
	public ResponseEntity<?> attachSourceToInfo(@PathVariable Long infoId, @PathVariable Long sourceId) {
		log.info("Attaching source with ID={} to item info with ID={}", sourceId, infoId);
		InfoData response = apiService.attachSourceToInfo(sourceId, infoId);

		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("/infos/{infoId}/sources/{sourceId}")
	public ResponseEntity<?> detachSourceFromInfo(@PathVariable Long infoId, @PathVariable Long sourceId) {
		log.info("Detaching source with ID={} from item info with ID={}", sourceId, infoId);
		InfoData response = apiService.detachSourceToInfo(sourceId, infoId);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/infos/{id}/zones")
	public ResponseEntity<?> getZonesFromInfo(@PathVariable Long id) {
		log.info("Retrieving all zones from info with ID={}", id);
		List<Link> links = new LinkedList<Link>();
		links.add(linkTo(methodOn(ApiController.class).getZonesFromInfo(id)).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).getInfoById(id)).withRel("parent"));
		links.add(linkTo(methodOn(ApiController.class).getZonesFromInfo(id)).slash("{zoneId}").withRel("attach-zone"));
		links.add(linkTo(methodOn(ApiController.class).getZonesFromInfo(id)).slash("{zoneId}").withRel("detach-zone"));

		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, POST")
				.body(CollectionModel.of(apiService.getZonesFromInfo(id), links));
	}

	@PutMapping("/infos/{infoId}/zones/{zoneId}")
	public ResponseEntity<?> attachZoneToInfo(@PathVariable Long infoId, @PathVariable Long zoneId) {
		log.info("Attaching zone with ID={} to item info with ID={}", zoneId, infoId);
		InfoData response = apiService.attachZoneToInfo(zoneId, infoId);
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("/infos/{infoId}/zones/{zoneId}")
	public ResponseEntity<?> detachZoneFromInfo(@PathVariable Long infoId, @PathVariable Long zoneId) {
		log.info("Detaching zone with ID={} from item info with ID={}", zoneId, infoId);
		InfoData response = apiService.detachZoneFromInfo(zoneId, infoId);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/sources")
	public ResponseEntity<?> getAllSources() {
		log.info("Retrieving all sources");
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllSources()).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).showEntry()).withRel("parent"));
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, POST")
				.body(CollectionModel.of(apiService.getAllSources(), links));
	}

	@PostMapping("/sources")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> insertSource(@RequestBody SourceData data) {
		log.info("Creating source {}", data);
		SourceData response = apiService.createSource(data);
		addSourceLinks(response);
		return ResponseEntity //
				.created(response.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.header("Access-Control-Allow-Methods", "GET, DELETE") //
				.body(response);
	}

	private void addSourceLinks(SourceData response) {
		Long id = response.getSourceId();
		response.add(linkTo(methodOn(ApiController.class).getAllSources()).withRel("parent"));
		response.add(linkTo(methodOn(ApiController.class).getSourceById(id)).withRel("delete"));
	}

	@GetMapping("/sources/{id}")
	public ResponseEntity<?> getSourceById(@PathVariable Long id) {
		log.info("Retrieving source with ID={}", id);
		SourceData response = apiService.getSourceById(id);
		addSourceLinks(response);
		return ResponseEntity.ok()//
				.header("Access-Control-Allow-Methods", "GET, DELETE") //
				.body(response);
	}

	@DeleteMapping("/sources/{id}")
	public ResponseEntity<?> deleteSource(@PathVariable Long id) {
		log.info("Deleting source with ID={}", id);
		apiService.deleteSource(id);
		List<Link> links = new LinkedList<Link>();
		links.add(linkTo(methodOn(ApiController.class).getAllSources()).withRel("parent"));
		return ResponseEntity.ok() //
				.location(linkTo(methodOn(ApiController.class).getAllSources()).toUri()) //
				.header("Access-Control-Allow-Methods", "GET, POST") //
				.body(CollectionModel.of( //
						Map.of("message", "source with ID=" + id + " has been successfully deleted"), links));
	}
	
	@GetMapping("/zones")
	public ResponseEntity<?> getAllZones() {
		log.info("Retrieving all zones");
		List<Link> links = new LinkedList<>();
		links.add(linkTo(methodOn(ApiController.class).getAllZones()).withSelfRel());
		links.add(linkTo(methodOn(ApiController.class).showEntry()).withRel("parent"));
		return ResponseEntity.ok() //
				.header("Access-Control-Allow-Methods", "GET, POST")
				.body(CollectionModel.of(apiService.getAllZones(), links));
	}
	
	@PostMapping("/zones")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> insertZone(@RequestBody ZoneData data) {
		log.info("Creating zone {}", data);
		ZoneData response = apiService.createZone(data);
		addZoneLinks(response);
		return ResponseEntity //
				.created(response.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.header("Access-Control-Allow-Methods", "GET, DELETE") //
				.body(response);
	}

	private void addZoneLinks(ZoneData response) {
		Long id = response.getZoneId();
		response.add(linkTo(methodOn(ApiController.class).getAllZones()).withRel("parent"));
		response.add(linkTo(methodOn(ApiController.class).getZoneById(id)).withRel("delete"));
	}

	@GetMapping("/zones/{id}")
	public ResponseEntity<?> getZoneById(@PathVariable Long id) {
		log.info("Retrieving zone with ID={}", id);
		ZoneData response = apiService.getZoneById(id);
		addZoneLinks(response);
		return ResponseEntity.ok()//
				.header("Access-Control-Allow-Methods", "GET, DELETE") //
				.body(response);
	}
	
	@DeleteMapping("/zones/{id}")
	public ResponseEntity<?> deleteZone(@PathVariable Long id) {
		log.info("Deleting zone with ID={}", id);
		apiService.deleteZone(id);
		List<Link> links = new LinkedList<Link>();
		links.add(linkTo(methodOn(ApiController.class).getAllZones()).withRel("parent"));
		return ResponseEntity.ok() //
				.location(linkTo(methodOn(ApiController.class).getAllZones()).toUri()) //
				.header("Access-Control-Allow-Methods", "GET, POST") //
				.body(CollectionModel.of( //
						Map.of("message", "zone with ID=" + id + " has been successfully deleted"), links));
	}

	@PostMapping("/connect")
	public Map<String, String> buildInitConnections() {
		attachSourceToInfo(1L, 1L);
		attachSourceToInfo(2L, 1L);
		attachSourceToInfo(3L, 2L);
		attachSourceToInfo(3L, 3L);
		attachSourceToInfo(3L, 4L);

		attachZoneToInfo(1L, 11L);
		attachZoneToInfo(1L, 10L);
		attachZoneToInfo(1L, 9L);

		attachZoneToInfo(2L, 1L);
		attachZoneToInfo(2L, 2L);
		attachZoneToInfo(2L, 3L);
		attachZoneToInfo(2L, 4L);
		attachZoneToInfo(2L, 5L);
		attachZoneToInfo(2L, 6L);

		attachZoneToInfo(3L, 7L);
		attachZoneToInfo(3L, 8L);

		return Map.of("message", "successfully attached inital conditions");
	}
}
