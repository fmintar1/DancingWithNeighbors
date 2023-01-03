package rocks.zipcode.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.zipcode.repository.FriendsRepository;
import rocks.zipcode.service.FriendsService;
import rocks.zipcode.service.dto.FriendsDTO;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.Friends}.
 */
@RestController
@RequestMapping("/api")
public class FriendsResource {

    private final Logger log = LoggerFactory.getLogger(FriendsResource.class);

    private static final String ENTITY_NAME = "friends";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FriendsService friendsService;

    private final FriendsRepository friendsRepository;

    public FriendsResource(FriendsService friendsService, FriendsRepository friendsRepository) {
        this.friendsService = friendsService;
        this.friendsRepository = friendsRepository;
    }

    /**
     * {@code POST  /friends} : Create a new friends.
     *
     * @param friendsDTO the friendsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new friendsDTO, or with status {@code 400 (Bad Request)} if the friends has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/friends")
    public ResponseEntity<FriendsDTO> createFriends(@RequestBody FriendsDTO friendsDTO) throws URISyntaxException {
        log.debug("REST request to save Friends : {}", friendsDTO);
        if (friendsDTO.getId() != null) {
            throw new BadRequestAlertException("A new friends cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FriendsDTO result = friendsService.save(friendsDTO);
        return ResponseEntity
            .created(new URI("/api/friends/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /friends/:id} : Updates an existing friends.
     *
     * @param id the id of the friendsDTO to save.
     * @param friendsDTO the friendsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated friendsDTO,
     * or with status {@code 400 (Bad Request)} if the friendsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the friendsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/friends/{id}")
    public ResponseEntity<FriendsDTO> updateFriends(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FriendsDTO friendsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Friends : {}, {}", id, friendsDTO);
        if (friendsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, friendsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!friendsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FriendsDTO result = friendsService.update(friendsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, friendsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /friends/:id} : Partial updates given fields of an existing friends, field will ignore if it is null
     *
     * @param id the id of the friendsDTO to save.
     * @param friendsDTO the friendsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated friendsDTO,
     * or with status {@code 400 (Bad Request)} if the friendsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the friendsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the friendsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/friends/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FriendsDTO> partialUpdateFriends(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FriendsDTO friendsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Friends partially : {}, {}", id, friendsDTO);
        if (friendsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, friendsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!friendsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FriendsDTO> result = friendsService.partialUpdate(friendsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, friendsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /friends} : get all the friends.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of friends in body.
     */
    @GetMapping("/friends")
    public List<FriendsDTO> getAllFriends() {
        log.debug("REST request to get all Friends");
        return friendsService.findAll();
    }

    /**
     * {@code GET  /friends/:id} : get the "id" friends.
     *
     * @param id the id of the friendsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the friendsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/friends/{id}")
    public ResponseEntity<FriendsDTO> getFriends(@PathVariable Long id) {
        log.debug("REST request to get Friends : {}", id);
        Optional<FriendsDTO> friendsDTO = friendsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(friendsDTO);
    }

    /**
     * {@code DELETE  /friends/:id} : delete the "id" friends.
     *
     * @param id the id of the friendsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/friends/{id}")
    public ResponseEntity<Void> deleteFriends(@PathVariable Long id) {
        log.debug("REST request to delete Friends : {}", id);
        friendsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
