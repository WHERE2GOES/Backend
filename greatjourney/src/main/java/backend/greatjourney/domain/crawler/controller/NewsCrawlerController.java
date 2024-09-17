package backend.greatjourney.domain.crawler.controller;

import backend.greatjourney.domain.crawler.dto.NewsRequest;
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
    public BaseResponse newsCrawling(@RequestBody NewsRequest newsRequest){

        List<NewsResponse> newsResponses = newsCrawlerService.crawl(newsRequest.getKeywordText());
        newsResponses.addAll(newsCrawlerService.crawlWithSelenium(newsRequest.getKeywordText(),newsRequest.getCount()));



        //더미 데이터를 넣음으로써 데이터가 들어온다는 것은 확인했는데 그러면 제대로 만들어지지 않아서 문제가 생기는거지
        return BaseResponse.builder()
                .code(200)
                .message("뉴스 크롤링 데이터")
                .isSuccess(true)
                .data(newsResponses)
                .build();
    }
}
