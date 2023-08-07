package com.dayone.scheduler;
import com.dayone.model.Company;
import com.dayone.model.ScrapedResult;
import com.dayone.persist.CompanyRepository;
import com.dayone.persist.DividendRepository;
import com.dayone.persist.entity.CompanyEntity;
import com.dayone.persist.entity.DividendEntity;
import com.dayone.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j @Component @EnableCaching
@AllArgsConstructor
public class ScraperScheduler {
    //@Scheduled(cron="0/5 * * * * *")
    //public void test(){System.out.println
    //        ("now-> "+System.currentTimeMillis());}
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;
//    @Scheduled(fixedDelay =1000)
//    public void test1() throws InterruptedException{
//        Thread.sleep(10000); // 10초 간 일시정지
//        System.out.println(Thread.currentThread().getName()+
//                " -> 테스트1 : "+ LocalDateTime.now());}
//    @Scheduled(fixedDelay =1000) public void test2() {
//        System.out.println(Thread.currentThread().getName()+
//                " -> 테스트2 : "+ LocalDateTime.now());}
    //일정 주기마다 수행
    @CacheEvict(value = "finance",allEntries = true)
    @Scheduled(cron="${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling(){
        log.info("scraping scheduler is started");
        //저장된 회사 목록 조회
        List<CompanyEntity> companies=this.companyRepository.findAll();
        //회사마다 배당금 정보 새로 스크래핑
        for(var company: companies){
            log.info("scraping scheduler is started->"+company.getName());
            ScrapedResult scrapedResult=this.yahooFinanceScraper.scrap
                    (new Company(company.getTicker(),company.getName()));
                            //(Company.builder().name(company.getName()).
                            // ticker(company.getTicker()).build());
            //스크래핑한 대방금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
                    .map(e->new DividendEntity(company.getId(),e))
                    .forEach(e->{boolean exists =this.dividendRepository
                            .existsByCompanyIdAndDate(e.getCompanyId(),e.getDate());
                        if(!exists){this.dividendRepository.save(e);}});
            //연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
            try {Thread.sleep(3000); //3초
            }catch (InterruptedException e){ e.printStackTrace();
            Thread.currentThread().interrupt();}}}
}