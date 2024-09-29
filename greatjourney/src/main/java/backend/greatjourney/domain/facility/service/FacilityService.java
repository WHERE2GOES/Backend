package backend.greatjourney.domain.facility.service;

import backend.greatjourney.domain.facility.domain.Facility;
import backend.greatjourney.domain.facility.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public List<Facility> getRoutePointsByRouteId(Integer routeId) {
        return facilityRepository.findByRouteId(routeId);
    }
}