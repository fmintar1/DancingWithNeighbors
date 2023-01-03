package rocks.zipcode.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import rocks.zipcode.domain.enumeration.Styles;

/**
 * A DTO for the {@link rocks.zipcode.domain.Friends} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FriendsDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate birthdate;

    private String location;

    private Styles styles;

    private String availability;

    @Lob
    private byte[] image;

    private String imageContentType;
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Styles getStyles() {
        return styles;
    }

    public void setStyles(Styles styles) {
        this.styles = styles;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FriendsDTO)) {
            return false;
        }

        FriendsDTO friendsDTO = (FriendsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, friendsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FriendsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", location='" + getLocation() + "'" +
            ", styles='" + getStyles() + "'" +
            ", availability='" + getAvailability() + "'" +
            ", image='" + getImage() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
