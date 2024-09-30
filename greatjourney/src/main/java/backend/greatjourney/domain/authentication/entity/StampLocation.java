package backend.greatjourney.domain.authentication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class StampLocation {
    @Id
    private Long id;

    private String locationName;

    private String locationDescription;

}
