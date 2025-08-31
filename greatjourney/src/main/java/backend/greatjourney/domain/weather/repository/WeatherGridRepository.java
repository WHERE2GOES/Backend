package backend.greatjourney.domain.weather.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import backend.greatjourney.domain.weather.entity.WeatherGrid;

public interface WeatherGridRepository extends JpaRepository<WeatherGrid, Long> {

	Optional<WeatherGrid> findFirstByLevel1AndLevel2AndLevel3(String level1, String level2, String level3);

	// 빈 문자열로 관리되는 경우까지 커버하기 위해 몇 가지 보조 쿼리
	List<WeatherGrid> findByLevel1(String level1);

	Optional<WeatherGrid> findFirstByLevel1AndLevel2AndLevel3In(String level1, String level2, List<String> level3);

	Optional<WeatherGrid> findFirstByLevel1AndLevel2InAndLevel3In(String level1, List<String> level2, List<String> level3);
}
