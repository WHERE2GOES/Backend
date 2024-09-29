package backend.greatjourney.domain.facility.controller;

import backend.greatjourney.domain.facility.domain.Facility;
import backend.greatjourney.domain.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/facility")
@RequiredArgsConstructor
public class FacilityController {


    private final FacilityService facilityService;

    @GetMapping("/certificate/{routeId}")
    public ResponseEntity<List<Facility>> getRoutePointsByRouteId(@PathVariable Integer routeId) {
        List<Facility> facilityPoints = facilityService.getRoutePointsByRouteId(routeId);
        if (facilityPoints.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(facilityPoints);
    }
}
