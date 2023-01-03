package rocks.zipcode.service.mapper;

import org.mapstruct.*;
import rocks.zipcode.domain.Friends;
import rocks.zipcode.domain.User;
import rocks.zipcode.service.dto.FriendsDTO;
import rocks.zipcode.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Friends} and its DTO {@link FriendsDTO}.
 */
@Mapper(componentModel = "spring")
public interface FriendsMapper extends EntityMapper<FriendsDTO, Friends> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    FriendsDTO toDto(Friends s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
