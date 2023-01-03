package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import rocks.zipcode.IntegrationTest;
import rocks.zipcode.domain.Friends;
import rocks.zipcode.domain.enumeration.Styles;
import rocks.zipcode.repository.FriendsRepository;
import rocks.zipcode.service.dto.FriendsDTO;
import rocks.zipcode.service.mapper.FriendsMapper;

/**
 * Integration tests for the {@link FriendsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FriendsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Styles DEFAULT_STYLES = Styles.BACHATA;
    private static final Styles UPDATED_STYLES = Styles.BALLET;

    private static final String DEFAULT_AVAILABILITY = "AAAAAAAAAA";
    private static final String UPDATED_AVAILABILITY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/friends";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private FriendsMapper friendsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFriendsMockMvc;

    private Friends friends;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Friends createEntity(EntityManager em) {
        Friends friends = new Friends()
            .name(DEFAULT_NAME)
            .birthdate(DEFAULT_BIRTHDATE)
            .location(DEFAULT_LOCATION)
            .styles(DEFAULT_STYLES)
            .availability(DEFAULT_AVAILABILITY)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return friends;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Friends createUpdatedEntity(EntityManager em) {
        Friends friends = new Friends()
            .name(UPDATED_NAME)
            .birthdate(UPDATED_BIRTHDATE)
            .location(UPDATED_LOCATION)
            .styles(UPDATED_STYLES)
            .availability(UPDATED_AVAILABILITY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return friends;
    }

    @BeforeEach
    public void initTest() {
        friends = createEntity(em);
    }

    @Test
    @Transactional
    void createFriends() throws Exception {
        int databaseSizeBeforeCreate = friendsRepository.findAll().size();
        // Create the Friends
        FriendsDTO friendsDTO = friendsMapper.toDto(friends);
        restFriendsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(friendsDTO)))
            .andExpect(status().isCreated());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeCreate + 1);
        Friends testFriends = friendsList.get(friendsList.size() - 1);
        assertThat(testFriends.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFriends.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testFriends.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testFriends.getStyles()).isEqualTo(DEFAULT_STYLES);
        assertThat(testFriends.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
        assertThat(testFriends.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testFriends.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createFriendsWithExistingId() throws Exception {
        // Create the Friends with an existing ID
        friends.setId(1L);
        FriendsDTO friendsDTO = friendsMapper.toDto(friends);

        int databaseSizeBeforeCreate = friendsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFriendsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(friendsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFriends() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        // Get all the friendsList
        restFriendsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(friends.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].styles").value(hasItem(DEFAULT_STYLES.toString())))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getFriends() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        // Get the friends
        restFriendsMockMvc
            .perform(get(ENTITY_API_URL_ID, friends.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(friends.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.styles").value(DEFAULT_STYLES.toString()))
            .andExpect(jsonPath("$.availability").value(DEFAULT_AVAILABILITY))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingFriends() throws Exception {
        // Get the friends
        restFriendsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFriends() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();

        // Update the friends
        Friends updatedFriends = friendsRepository.findById(friends.getId()).get();
        // Disconnect from session so that the updates on updatedFriends are not directly saved in db
        em.detach(updatedFriends);
        updatedFriends
            .name(UPDATED_NAME)
            .birthdate(UPDATED_BIRTHDATE)
            .location(UPDATED_LOCATION)
            .styles(UPDATED_STYLES)
            .availability(UPDATED_AVAILABILITY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        FriendsDTO friendsDTO = friendsMapper.toDto(updatedFriends);

        restFriendsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, friendsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(friendsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
        Friends testFriends = friendsList.get(friendsList.size() - 1);
        assertThat(testFriends.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFriends.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testFriends.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testFriends.getStyles()).isEqualTo(UPDATED_STYLES);
        assertThat(testFriends.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        assertThat(testFriends.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFriends.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // Create the Friends
        FriendsDTO friendsDTO = friendsMapper.toDto(friends);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, friendsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(friendsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // Create the Friends
        FriendsDTO friendsDTO = friendsMapper.toDto(friends);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(friendsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // Create the Friends
        FriendsDTO friendsDTO = friendsMapper.toDto(friends);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(friendsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFriendsWithPatch() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();

        // Update the friends using partial update
        Friends partialUpdatedFriends = new Friends();
        partialUpdatedFriends.setId(friends.getId());

        partialUpdatedFriends
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restFriendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFriends.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFriends))
            )
            .andExpect(status().isOk());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
        Friends testFriends = friendsList.get(friendsList.size() - 1);
        assertThat(testFriends.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFriends.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testFriends.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testFriends.getStyles()).isEqualTo(DEFAULT_STYLES);
        assertThat(testFriends.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
        assertThat(testFriends.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFriends.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateFriendsWithPatch() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();

        // Update the friends using partial update
        Friends partialUpdatedFriends = new Friends();
        partialUpdatedFriends.setId(friends.getId());

        partialUpdatedFriends
            .name(UPDATED_NAME)
            .birthdate(UPDATED_BIRTHDATE)
            .location(UPDATED_LOCATION)
            .styles(UPDATED_STYLES)
            .availability(UPDATED_AVAILABILITY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restFriendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFriends.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFriends))
            )
            .andExpect(status().isOk());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
        Friends testFriends = friendsList.get(friendsList.size() - 1);
        assertThat(testFriends.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFriends.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testFriends.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testFriends.getStyles()).isEqualTo(UPDATED_STYLES);
        assertThat(testFriends.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        assertThat(testFriends.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFriends.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // Create the Friends
        FriendsDTO friendsDTO = friendsMapper.toDto(friends);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, friendsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(friendsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // Create the Friends
        FriendsDTO friendsDTO = friendsMapper.toDto(friends);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(friendsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFriends() throws Exception {
        int databaseSizeBeforeUpdate = friendsRepository.findAll().size();
        friends.setId(count.incrementAndGet());

        // Create the Friends
        FriendsDTO friendsDTO = friendsMapper.toDto(friends);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(friendsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Friends in the database
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFriends() throws Exception {
        // Initialize the database
        friendsRepository.saveAndFlush(friends);

        int databaseSizeBeforeDelete = friendsRepository.findAll().size();

        // Delete the friends
        restFriendsMockMvc
            .perform(delete(ENTITY_API_URL_ID, friends.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Friends> friendsList = friendsRepository.findAll();
        assertThat(friendsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
