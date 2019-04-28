import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

public class BasicWebCrawler {
    private HashSet<String> links;

    public BasicWebCrawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String URL) {
        try {
            Document document = Jsoup.connect(URL).get();
            Elements elements = document.select("table");
//
//            for (Element page : elements) {
//                System.out.println("get~~~~~~~~~~");
//                System.out.println("id:"+page.id());
//                System.out.println("class:"+page.className());
//                System.out.println("---");
//            }
//            System.out.println(elements.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //1. Pick a URL from the frontier
//        new BasicWebCrawler().getPageLinks("http://www.mkyong.com/");
        new BasicWebCrawler().getPageLinks("http://localhost:8080//");
//        new BasicWebCrawler().getPageLinks("https://goodinfo.tw/StockInfo/StockList.asp?MARKET_CAT=%E4%B8%8A%E5%B8%82&INDUSTRY_CAT=%E4%B8%8A%E5%B8%82%E5%85%A8%E9%83%A8&SHEET=%E4%BA%A4%E6%98%93%E7%8B%80%E6%B3%81&SHEET2=%E6%97%A5&RPT_TIME=%E6%9C%80%E6%96%B0%E8%B3%87%E6%96%99/");
    }
}