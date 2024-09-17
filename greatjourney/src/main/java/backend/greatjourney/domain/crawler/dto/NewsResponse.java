package backend.greatjourney.domain.crawler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponse {

    private String title;
    private String link;
    private String imageUrl;
    private String article;
    private String summary;

}
