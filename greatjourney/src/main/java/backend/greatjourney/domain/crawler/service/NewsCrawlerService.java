package backend.greatjourney.domain.crawler.service;

import backend.greatjourney.domain.crawler.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsCrawlerService {

//        // 검색할 키워드를 입력하면 해당 키워드에 대해 크롤링하는 함수
//        public List<NewsResponse> crawl(String keyword) {
//            List<NewsResponse> newsList = new ArrayList<>();
//            try {
//                // 키워드를 URL 인코딩 (공백 및 특수문자 처리)
//                String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
//
//                // Google 뉴스 검색 URL (뉴스만 가져오기)
//                String url = "https://www.google.com/search?q=" + encodedKeyword + "&tbm=nws";
//
//                // Jsoup을 사용해 뉴스 검색 결과 페이지 가져오기
//                Document doc = Jsoup.connect(url)
//                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
////                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
////                        .header("Accept-Language", "en-US,en;q=0.9,ko;q=0.8")
////                        .header("Referer", "https://www.google.com")
//                        .header("Connection", "keep-alive")
//                        .header("Upgrade-Insecure-Requests", "1")
//                        .timeout(50000)  // 타임아웃을 늘려 서버 응답 대기 시간 연장
//                        .get();
//
//                // 뉴스 검색 결과 추출 (뉴스 제목, 요약, 이미지 URL)
//                Elements newsResults = doc.select("div.SoaBEf");
//
//                if (newsResults.isEmpty()) {
//                    System.err.println("경고: Jsoup 크롤링에서 데이터가 없습니다11."+url);
//                }
//
//                for (Element result : newsResults) {
//                    // 제목 가져오기
//                    String title = result.select("div.n0jPhd.ynAwRc.MBeuO.nDgy9d").text();
//
//                    // 뉴스 기사 링크 가져오기
//                    String newsUrl = result.select("a").attr("href");
//
//                    // 뉴스 요약
//                    String summary = result.select("div.GI74Re.nDgy9d").text();
//
//                    // 이미지 URL 가져오기
//                    String imageUrl = result.select("img").attr("src");
//
//                    // NewsResponse 객체 생성 후 리스트에 추가 (빌더 패턴 사용)
//                    NewsResponse newsResponse = NewsResponse.builder()
//                            .title(title)
//                            .link(newsUrl)
//                            .summary(summary)
//                            .imageUrl(imageUrl)
//                            .build();
//
//                    newsList.add(newsResponse);
//                }
//
//            } catch (IOException e) {
//                System.err.println("Jsoup 크롤링 중 예외 발생: " + e.getMessage());
//                e.printStackTrace();
//            }
//
//            if (newsList.isEmpty()) {
//                System.err.println("경고: Jsoup 크롤링에서 수집된 뉴스가 없습니다.");
//            }
//
//            return newsList;
//        }
//
////        // Selenium을 사용한 크롤링 함수
////        public List<NewsResponse> crawlWithSelenium(String keyword) {
////            List<NewsResponse> newsList = new ArrayList<>();
////
////            // 크롬 드라이버 설정 (크롬 드라이버의 경로를 환경에 맞게 설정)
////            ChromeOptions options = new ChromeOptions();
//////            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
//////            options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
//////            options.setExperimentalOption("useAutomationExtension", false);
////            options.addArguments("--headless");
////
////            WebDriver driver = new ChromeDriver(options);
////
////            try {
////                String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
////                // 구글 뉴스 검색 URL
////                String url = "https://www.google.com/search?q=" + encodedKeyword + "&tbm=nws";
////
////                // 검색 결과 페이지로 이동
////                driver.get(url);
////
////                // 뉴스 검색 결과 추출
//////                List<WebElement> newsResults = driver.findElements(By.cssSelector("div.SoaBEf"));
////                List<WebElement> newsResults = driver.findElements(By.cssSelector("div.SoaBEf"));
////
////                if (newsResults.isEmpty()) {
////                    System.err.println("경고: Selenium 크롤링에서 데이터가 없습니다."+url);
////                }
////
////                for (WebElement result : newsResults) {
////                    // 뉴스 기사 링크 가져오기
////                    String newsUrl = result.findElement(By.cssSelector("a")).getAttribute("href");
////
////                    // 새 창에서 뉴스 페이지 열기
////                    try {
////                        driver.get(newsUrl);
////                    } catch (UnhandledAlertException e) {
////                        System.err.println("403 Forbidden 에러 발생: " + newsUrl);
////                        continue;
////                    } catch (Exception e) {
////                        System.err.println("다른 예외 발생: " + newsUrl + ", 에러 메시지: " + e.getMessage());
////                        continue;
////                    }
////
////                    // 뉴스 본문 가져오기
////                    String articleContent;
////                    try {
////                        articleContent = driver.findElement(By.tagName("body")).getText();
////                    } catch (NoSuchElementException e) {
////                        articleContent = "본문을 가져오지 못했습니다.";
////                        System.err.println("경고: 뉴스 본문을 가져오지 못했습니다.");
////                    }
////
////                    // 뉴스 정보 저장
////                    NewsResponse newsResponse = NewsResponse.builder()
////                            .title(result.findElement(By.cssSelector("div.n0jPhd ynAwRc MBeuO nDgy9d")).getText())  // 제목 가져오기
////                            .link(newsUrl)  // 링크 저장
////                            .summary(result.findElement(By.cssSelector("div.Y3v8qd")).getText())  // 요약 저장
////                            .imageUrl(result.findElement(By.cssSelector("img")).getAttribute("src"))  // 이미지 URL 저장
////                            .article(articleContent)  // 본문 저장
////                            .build();
////
////                    newsList.add(newsResponse);
////
////                    // 검색 결과 페이지로 돌아가기
////                    driver.navigate().back();
////                }
////
////            } catch (Exception e) {
////                System.err.println("Selenium 크롤링 중 예외 발생: " + e.getMessage());
////                e.printStackTrace();
////            } finally {
////                driver.quit();
////            }
////
////            if (newsList.isEmpty()) {
////                System.err.println("경고: Selenium 크롤링에서 수집된 뉴스가 없습니다.");
////            }
////
////            return newsList;
////        }
//        public List<NewsResponse> crawlWithSelenium(String keyword, int limit) {
//            List<NewsResponse> newsList = new ArrayList<>();
//
//            // 크롬 드라이버 설정
//            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--disable-gpu");  // GPU 비활성화
//            options.addArguments("--no-sandbox");  // 샌드박스 모드 비활성화
//            options.addArguments("--disable-dev-shm-usage");  // 메모리 문제 해결
//            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
//
//            WebDriver driver = new ChromeDriver(options);
//
//            try {
//                // 키워드 인코딩
//                String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
//                String url = "https://search.naver.com/search.naver?where=news&query=" + encodedKeyword;
//
//                // 네이버 뉴스 검색 페이지로 이동
//                driver.get(url);
//
//                // 검색 결과가 로드될 때까지 대기 (최대 10초)
//                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul.list_news")));
//
//                // 뉴스 검색 결과 추출
//                List<WebElement> newsResults = driver.findElements(By.cssSelector("ul.list_news > li"));
//                int count = 0;
//
//                for (WebElement result : newsResults) {
//                    if (count >= limit) {
//                        break;  // 설정한 뉴스 개수만큼 수집하면 종료
//                    }
//
//                    // 뉴스 제목 가져오기
//                    String title = result.findElement(By.cssSelector("a.news_tit")).getAttribute("title");
//
//                    // 뉴스 기사 링크 가져오기
//                    String newsUrl = result.findElement(By.cssSelector("a.news_tit")).getAttribute("href");
//
//                    // 뉴스 요약 가져오기
//                    String summary = result.findElement(By.cssSelector("div.news_dsc")).getText();
//
//                    // 뉴스 이미지 URL 가져오기 (이미지가 있을 때만)
//                    String imageUrl = "";
//                    try {
//                        imageUrl = result.findElement(By.cssSelector("img")).getAttribute("src");
//                    } catch (Exception e) {
//                        imageUrl = "이미지가 없습니다.";
//                    }
//
//
//                    // 뉴스 정보 저장
//                    NewsResponse newsResponse = NewsResponse.builder()
//                            .title(title)  // 제목 저장
//                            .link(newsUrl)  // 링크 저장
//                            .summary(summary)  // 요약 저장
//                            .imageUrl(imageUrl)  // 이미지 URL 저장
//                            .build();
//
//                    newsList.add(newsResponse);
//                    count++;
//
//                }
//
//            } catch (Exception e) {
//                System.err.println("Selenium 크롤링 중 예외 발생: " + e.getMessage());
//                e.printStackTrace();
//            } finally {
//                driver.quit();
//            }
//
//            if (newsList.isEmpty()) {
//                System.err.println("경고: Selenium 크롤링에서 수집된 뉴스가 없습니다.");
//            }
//
//            return newsList;
//        }
//
//
        public List<NewsResponse> crawlWithSelenium(String keyword, int limit) {
            List<NewsResponse> newsList = new ArrayList<>();

            // 크롬 드라이버 설정
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");

            WebDriver driver = null;

            try {
                driver = new ChromeDriver(options);

                // 키워드 인코딩
                String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
                String url = "https://search.naver.com/search.naver?where=news&query=" + encodedKeyword;

                // 네이버 뉴스 검색 페이지로 이동
                driver.get(url);

                // 검색 결과가 로드될 때까지 대기 (최대 10초)
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ul.list_news")));

                // 뉴스 검색 결과 추출
                List<WebElement> newsResults = driver.findElements(By.cssSelector("ul.list_news > li"));
                int count = 0;

                for (WebElement result : newsResults) {
                    if (count >= limit) {
                        break;  // 설정한 뉴스 개수만큼 수집하면 종료
                    }

                    // 뉴스 제목 가져오기
                    String title = result.findElement(By.cssSelector("a.news_tit")).getAttribute("title");

                    // 뉴스 기사 링크 가져오기
                    String newsUrl = result.findElement(By.cssSelector("a.news_tit")).getAttribute("href");

                    // 뉴스 요약 가져오기
                    String summary = result.findElement(By.cssSelector("div.news_dsc")).getText();

                    // 뉴스 이미지 URL 가져오기 (이미지가 있을 때만)
                    String imageUrl = "";
                    try {
                        imageUrl = result.findElement(By.cssSelector("img")).getAttribute("src");
                    } catch (Exception e) {
                        log.warn("이미지 URL을 가져오는 중 예외 발생: {}", e.getMessage());
                        imageUrl = "이미지가 없습니다.";
                    }

                    // 뉴스 정보 저장
                    NewsResponse newsResponse = NewsResponse.builder()
                            .title(title)
                            .link(newsUrl)
                            .summary(summary)
                            .imageUrl(imageUrl)
                            .build();

                    newsList.add(newsResponse);
                    count++;
                }

            } catch (IOException e) {
                log.error("URL에 대한 네트워크 오류 발생: {}", e.getMessage(), e);
            } catch (TimeoutException e) {
                log.error("페이지 로드 시간 초과: {}", e.getMessage(), e);
            } catch (NoSuchElementException e) {
                log.error("필수 요소를 찾지 못했습니다: {}", e.getMessage(), e);
            } catch (Exception e) {
                log.error("크롤링 중 예외 발생: {}", e.getMessage(), e);
            } finally {
                if (driver != null) {
                    driver.quit();  // 드라이버 종료
                }
            }

            // 만약 수집된 뉴스가 없으면 경고 메시지와 함께 빈 리스트를 반환
            if (newsList.isEmpty()) {
                log.warn("경고: Selenium 크롤링에서 수집된 뉴스가 없습니다.");
            }

            return newsList;
        }
    }






