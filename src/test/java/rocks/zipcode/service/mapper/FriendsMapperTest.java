package rocks.zipcode.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FriendsMapperTest {

    private FriendsMapper friendsMapper;

    @BeforeEach
    public void setUp() {
        friendsMapper = new FriendsMapperImpl();
    }
}
