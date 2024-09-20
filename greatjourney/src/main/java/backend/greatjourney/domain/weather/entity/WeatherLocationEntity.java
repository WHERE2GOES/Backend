package backend.greatjourney.domain.weather.entity;


import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="weather_data")
@Entity
@Getter
@Setter
public class WeatherLocationEntity {


    private String step1;
    private String step2;
    private String step3;

    private String X;
    private String Y;

    private String hour1;
    private String hour2;

    private String minute1;
    private String minute2;

    private String second1;
    private String second2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
