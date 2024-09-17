package backend.greatjourney.domain.crawler.controller;

import backend.greatjourney.domain.crawler.dto.NewsResponse;
import backend.greatjourney.domain.crawler.service.NewsCrawlerService;
import backend.greatjourney.global.exception.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NewsCrawlerController {

    private final NewsCrawlerService newsCrawlerService;

    @GetMapping("/api/news")
    public BaseResponse newsCrawling(@RequestBody String keywordText){

        List<NewsResponse> newsResponses = newsCrawlerService.crawlWithSelenium(keywordText);
        newsResponses.addAll(newsCrawlerService.crawlWithSelenium(keywordText));

        return BaseResponse.builder()
                .code(200)
                .message("뉴스 크롤링 데이터")
                .isSuccess(true)
                .data(newsResponses)
                .build();
    }
}
