package backend.greatjourney.domain.course.dto;

import backend.greatjourney.domain.course.domain.Place;

// 이 DTO는 이전에 만들었던 UncertifiedPlaceDto와 거의 동일할 수 있습니다.
public record CertificationCenterDto(
        long placeId,
        String placeName,
        Double latitude,
        Double longitude
) {
    public static CertificationCenterDto from(Place place) {
        return new CertificationCenterDto(
                place.getId(),
                place.getName(),
                place.getLatitude(),
                place.getLongitude()
        );
    }
}
