package backend.greatjourney.domain.survey.dto;

import lombok.Getter;

@Getter
public class SurveyThemeResponseDto {
    private final String themeName; // 예: "성큼성큼"
    private final String themeCode; // 예: "44444551"

    public SurveyThemeResponseDto(String themeName, String themeCode) {
        this.themeName = themeName;
        this.themeCode = themeCode;
    }
}