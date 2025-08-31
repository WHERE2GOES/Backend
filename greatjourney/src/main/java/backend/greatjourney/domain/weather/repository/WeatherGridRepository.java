package backend.greatjourney.domain.weather.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import backend.greatjourney.domain.weather.entity.WeatherGrid;

public interface WeatherGridRepository extends JpaRepository<WeatherGrid, Long> {

	Optional<WeatherGrid> findFirstByLevel1AndLevel2AndLevel3(String level1, String level2, String level3);

	// level3이 NULL인 행 매칭
	Optional<WeatherGrid> findFirstByLevel1AndLevel2AndLevel3IsNull(String level1, String level2);

	// level2/level3이 모두 NULL
	Optional<WeatherGrid> findFirstByLevel1AndLevel2IsNullAndLevel3IsNull(String level1);

	// 보조: level1만으로 후보 찾기
	List<WeatherGrid> findByLevel1(String level1);
}
