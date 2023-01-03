package rocks.zipcode.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class FriendsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FriendsDTO.class);
        FriendsDTO friendsDTO1 = new FriendsDTO();
        friendsDTO1.setId(1L);
        FriendsDTO friendsDTO2 = new FriendsDTO();
        assertThat(friendsDTO1).isNotEqualTo(friendsDTO2);
        friendsDTO2.setId(friendsDTO1.getId());
        assertThat(friendsDTO1).isEqualTo(friendsDTO2);
        friendsDTO2.setId(2L);
        assertThat(friendsDTO1).isNotEqualTo(friendsDTO2);
        friendsDTO1.setId(null);
        assertThat(friendsDTO1).isNotEqualTo(friendsDTO2);
    }
}
