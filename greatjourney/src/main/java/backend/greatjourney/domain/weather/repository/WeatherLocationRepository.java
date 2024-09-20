package backend.greatjourney.domain.weather.repository;

import backend.greatjourney.domain.weather.dto.LocationDTO;
import backend.greatjourney.domain.weather.entity.WeatherLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherLocationRepository extends JpaRepository<WeatherLocationEntity,Long> {
    List<WeatherLocationEntity> findByStep1(String Step1);
}
