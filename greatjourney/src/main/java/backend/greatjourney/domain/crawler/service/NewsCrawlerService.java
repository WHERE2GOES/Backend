package backend.greatjourney.domain.crawler.service;

import backend.greatjourney.domain.crawler.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsCrawlerService {

        //검색할 키워드를 입력하면 해당 키워드에 대해서 크롤링 하는 함수
        public List<NewsResponse> crawl(String keyword) {
            List<NewsResponse> newsList = new ArrayList<>();
            try {
                // 키워드를 URL 인코딩 (공백 및 특수문자 처리)
                String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");

                // Google 뉴스 검색 URL
                //nws로 설정해서 뉴스만 가져올 수 있도록 한다.
                String url = "https://www.google.com/search?q=" + encodedKeyword + "&tbm=nws";

                // Jsoup을 사용해 뉴스 검색 결과 페이지 가져오기
                //이렇게 userAgent를 생성하면
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                        .header("Accept-Language", "en-US,en;q=0.9,ko;q=0.8")
                        .header("Referer", "https://www.google.com")
                        .header("Connection", "keep-alive")
                        .header("Upgrade-Insecure-Requests", "1")
                        .timeout(10000)  // 타임아웃을 늘려 서버 응답 대기 시간 연장
                        .get();


                // 뉴스 검색 결과 추출 (뉴스 제목, 요약, 이미지 URL)
                Elements newsResults = doc.select("div.dbsr");

                // 결과 출력
                for (Element result : newsResults) {
                    // 제목 가져오기
                    String title = result.select("div.JheGif.nDgy9d").text();

                    // 뉴스 기사 링크 가져오기
                    String newsUrl = result.select("a").attr("href");

                    // 뉴스 요약 (일부 뉴스는 요약 제공)
                    String summary = result.select("div.Y3v8qd").text();

                    // 이미지 URL 가져오기 (이미지가 있을 경우)
                    String imageUrl = result.select("img").attr("src");

                    // NewsResponse 객체 생성 후 리스트에 추가 (빌더 패턴 사용)
                    NewsResponse newsResponse = NewsResponse.builder()
                            .title(title)
                            .link(newsUrl)
                            .summary(summary)
                            .imageUrl(imageUrl)
                            .build();

                    newsList.add(newsResponse);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newsList;
        }
    public List<NewsResponse> crawlWithSelenium(String keyword) {
        List<NewsResponse> newsList = new ArrayList<>();

        // 크롬 드라이버 설정 (크롬 드라이버의 경로를 환경에 맞게 설정)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        WebDriver driver = new ChromeDriver(options);

        try {
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
            // 구글 뉴스 검색 URL
            String url = "https://www.google.com/search?q=" + encodedKeyword + "&tbm=nws";

            // 검색 결과 페이지로 이동
            driver.get(url);

            // 뉴스 검색 결과 추출
            List<WebElement> newsResults = driver.findElements(By.cssSelector("div.dbsr"));

            for (WebElement result : newsResults) {
                // 뉴스 기사 링크 가져오기
                String newsUrl = result.findElement(By.cssSelector("a")).getAttribute("href");

                // 새 창에서 뉴스 페이지 열기
                try {
                    driver.get(newsUrl);
                } catch (UnhandledAlertException e) {
                    System.out.println("403 Forbidden 에러 발생: " + newsUrl);
                    continue; // 현재 기사 스킵하고 다음 기사로 진행
                } catch (Exception e) {
                    System.out.println("다른 예외 발생: " + newsUrl);
                    continue; // 다른 에러 발생 시에도 스킵
                }

                // 뉴스 본문 가져오기 (사이트마다 구조가 다르므로 tagName은 예시로 사용)
                String articleContent;
                try {
                    articleContent = driver.findElement(By.tagName("body")).getText(); // 뉴스 본문 가져오기
                } catch (NoSuchElementException e) {
                    articleContent = "본문을 가져오지 못했습니다."; // 예외 처리
                }

                // 뉴스 정보 저장
                NewsResponse newsResponse = NewsResponse.builder()
                        .title(result.findElement(By.cssSelector("div.JheGif.nDgy9d")).getText())  // 제목 가져오기
                        .link(newsUrl)  // 링크 저장
                        .summary(result.findElement(By.cssSelector("div.Y3v8qd")).getText())  // 요약 저장
                        .imageUrl(result.findElement(By.cssSelector("img")).getAttribute("src"))  // 이미지 URL 저장
                        .article(articleContent)  // 본문 저장
                        .build();

                newsList.add(newsResponse);

                // 검색 결과 페이지로 돌아가기
                driver.navigate().back();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();  // 작업 완료 후 브라우저 닫기
        }

        return newsList;
    }

}
