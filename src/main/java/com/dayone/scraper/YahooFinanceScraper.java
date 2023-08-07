package com.dayone.scraper;
import com.dayone.model.Company;
import com.dayone.model.Dividend;
import com.dayone.model.ScrapedResult;
import com.dayone.model.constants.Month;
import lombok.Builder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Builder
public class YahooFinanceScraper implements Scraper {
    private static final String STASTISTICS_URL=
            "https://finance.yahoo.com/quote/COKE/history?period1=99100800&period2=1690243200&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true";
    private static final String SUMMARY_URL=
            "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME=86400;
    public ScrapedResult scrap(Company company){
        var scrapResult=new ScrapedResult();
        try{long start=0; long end=System.currentTimeMillis()/1000;
            String url=String.format(STASTISTICS_URL,
                    company.getTicker(),start,end);
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();
            Elements eles=document.getElementsByAttributeValue
                    ("data-test","historical-prices");
            Element ele=eles.get(0); //table 전체
            Element tbody=ele.children().get(1);
            List<Dividend> dividends=new ArrayList<>();
            for(Element e:tbody.children()){
                String txt=e.text();
                if(!txt.endsWith("Dividend")) {continue;}
                //System.out.println(txt);
                String[] splits=txt.split(" ");
                int month= Month.strToNumber(splits[0]);
                int day=Integer.valueOf(splits[1].replace
                        (",",""));
                int year=Integer.valueOf(splits[2]);
                String dividend=splits[3];
                //System.out.println(year+"/"+month+"/"+day+"->"+dividend);
                if(month<0){throw new RuntimeException
                        ("Unexpected Month enum value->"+splits[0]);}
                dividends.add(new Dividend(LocalDateTime.of
                                (year,month,day,0,0),dividend));
                //dividends.add(Dividend.builder().date(LocalDateTime.of
                //                (year,month,day,0,0))
                //        .dividend(dividend).build());
            } scrapResult.setDividendEntities(dividends);}
        catch (IOException e){ e.printStackTrace();}
        return scrapResult;}
    @Override public Company scrapCompanyByTicker(String ticker){
        String url=String.format(SUMMARY_URL,ticker,ticker);
        try {Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByTag("h1").get(0);
            String title = titleEle.text().split(" - ")[1].trim();
            return new Company(ticker,title);
            //return Company.builder().ticker(ticker).name(title).build();
        }catch (IOException e){ e.printStackTrace();}return null;}
}