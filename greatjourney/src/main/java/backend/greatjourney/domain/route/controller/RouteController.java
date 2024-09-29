package backend.greatjourney.domain.route.controller;

import backend.greatjourney.domain.route.domain.Route;
import backend.greatjourney.domain.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/route-points")
@RequiredArgsConstructor
public class RouteController {


    private final RouteService routeService;

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<Route>> getRoutePointsByRouteId(@PathVariable Integer routeId) {
        List<Route> routePoints = routeService.getRoutePointsByRouteId(routeId);
        if (routePoints.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(routePoints);
    }
}
