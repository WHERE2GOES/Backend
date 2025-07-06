package backend.greatjourney.domain.course.service;

import backend.greatjourney.domain.course.domain.Place;
import backend.greatjourney.domain.course.dto.PlaceDetailResponse;
import backend.greatjourney.domain.course.repository.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {
    private final PlaceRepository placeRepo;

    public PlaceDetailResponse getPlaceDetail(Long id){
        Place p = placeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("place " + id + " not found"));
        return new PlaceDetailResponse(p.getId(), p.getName(), p.getCategory(), p.getLatitude(), p.getLongitude());
    }
}