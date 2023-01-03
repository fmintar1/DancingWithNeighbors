package rocks.zipcode.service.mapper;

import org.mapstruct.*;
import rocks.zipcode.domain.UserProfile;
import rocks.zipcode.service.dto.UserProfileDTO;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {}
