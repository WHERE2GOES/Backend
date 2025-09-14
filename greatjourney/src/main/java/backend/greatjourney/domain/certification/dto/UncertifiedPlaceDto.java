package backend.greatjourney.domain.certification.dto;


import backend.greatjourney.domain.course.domain.Place;

public record UncertifiedPlaceDto(
        Long placeId,
        String placeName
//        String address,
//        Double latitude,
//        Double longitude
) {
    /**
     * Place 엔티티를 UncertifiedPlaceDto로 변환합니다.
     * @param place 원본 Place 엔티티
     * @return 변환된 DTO
     */
    public static UncertifiedPlaceDto from(Place place) {
        return new UncertifiedPlaceDto(
                place.getId(),
                place.getName()
//                place.getAddress(),
//                place.getLatitude(),
//                place.getLongitude()
        );
    }
}