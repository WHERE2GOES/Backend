package backend.greatjourney.domain.route.repository;

import backend.greatjourney.domain.route.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByRouteId(Integer routeId);
}