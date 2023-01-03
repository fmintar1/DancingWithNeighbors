package rocks.zipcode.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.domain.Friends;
import rocks.zipcode.repository.FriendsRepository;
import rocks.zipcode.service.dto.FriendsDTO;
import rocks.zipcode.service.mapper.FriendsMapper;

/**
 * Service Implementation for managing {@link Friends}.
 */
@Service
@Transactional
public class FriendsService {

    private final Logger log = LoggerFactory.getLogger(FriendsService.class);

    private final FriendsRepository friendsRepository;

    private final FriendsMapper friendsMapper;

    public FriendsService(FriendsRepository friendsRepository, FriendsMapper friendsMapper) {
        this.friendsRepository = friendsRepository;
        this.friendsMapper = friendsMapper;
    }

    /**
     * Save a friends.
     *
     * @param friendsDTO the entity to save.
     * @return the persisted entity.
     */
    public FriendsDTO save(FriendsDTO friendsDTO) {
        log.debug("Request to save Friends : {}", friendsDTO);
        Friends friends = friendsMapper.toEntity(friendsDTO);
        friends = friendsRepository.save(friends);
        return friendsMapper.toDto(friends);
    }

    /**
     * Update a friends.
     *
     * @param friendsDTO the entity to save.
     * @return the persisted entity.
     */
    public FriendsDTO update(FriendsDTO friendsDTO) {
        log.debug("Request to update Friends : {}", friendsDTO);
        Friends friends = friendsMapper.toEntity(friendsDTO);
        friends = friendsRepository.save(friends);
        return friendsMapper.toDto(friends);
    }

    /**
     * Partially update a friends.
     *
     * @param friendsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FriendsDTO> partialUpdate(FriendsDTO friendsDTO) {
        log.debug("Request to partially update Friends : {}", friendsDTO);

        return friendsRepository
            .findById(friendsDTO.getId())
            .map(existingFriends -> {
                friendsMapper.partialUpdate(existingFriends, friendsDTO);

                return existingFriends;
            })
            .map(friendsRepository::save)
            .map(friendsMapper::toDto);
    }

    /**
     * Get all the friends.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FriendsDTO> findAll() {
        log.debug("Request to get all Friends");
        return friendsRepository.findAll().stream().map(friendsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one friends by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FriendsDTO> findOne(Long id) {
        log.debug("Request to get Friends : {}", id);
        return friendsRepository.findById(id).map(friendsMapper::toDto);
    }

    /**
     * Delete the friends by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Friends : {}", id);
        friendsRepository.deleteById(id);
    }
}
