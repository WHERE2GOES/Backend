package backend.greatjourney.domain.weather.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weather_grid",
	uniqueConstraints = @UniqueConstraint(name = "uk_weather_grid", columnNames = {"level1","level2","level3"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class WeatherGrid {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false)
	private String level1;  // 시/도

	@Column(length = 50)
	private String level2;  // 시/군/구

	@Column(length = 100)
	private String level3;  // 읍/면/동

	@Column(nullable = false)
	private Integer nx;

	@Column(nullable = false)
	private Integer ny;
}
