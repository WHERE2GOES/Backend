package backend.greatjourney.domain.route.service;

import backend.greatjourney.domain.route.domain.Route;
import backend.greatjourney.domain.route.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public List<Route> getRoutePointsByRouteId(Integer routeId) {
        return routeRepository.findByRouteId(routeId);
    }
}