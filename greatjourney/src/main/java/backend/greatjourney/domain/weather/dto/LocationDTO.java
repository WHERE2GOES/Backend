package backend.greatjourney.domain.weather.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="weatherData2")
public class LocationDTO {

    private String Step1;
    private String Step2;
    private String Step3;

    private String X;
    private String Y;

    private String Hour1;
    private String Hour2;

    private String Minute1;
    private String Minute2;

    private String Second1;
    private String Second2;



}
