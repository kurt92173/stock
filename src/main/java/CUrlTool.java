import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CUrlTool {
    public static String getPrice(String target) throws IOException {
        URL url = new URL(target);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        HttpURLConnection.setFollowRedirects(true);
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
        System.out.println("target = " + target);
        Map<String, String> parameters = new HashMap<>();
//    parameters.put("param1", "val");

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream()); // java.net.ConnectException: Connection refused: connect
//    out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }

    public static int getRandomSleep() {
        int sleep = (int) (Math.random() * 10) + 10;
        System.out.println(new Date() + "...sleep = " + sleep);
        return sleep;
    }

    public static void main(String[] args) throws IOException {
//        String result = getPrice("https://goodinfo.tw/StockInfo/StockList.asp?MARKET_CAT=%E4%B8%8A%E5%B8%82&INDUSTRY_CAT=%E4%B8%8A%E5%B8%82%E5%85%A8%E9%83%A8&SHEET=%E4%BA%A4%E6%98%93%E7%8B%80%E6%B3%81&SHEET2=%E6%97%A5&RPT_TIME=%E6%9C%80%E6%96%B0%E8%B3%87%E6%96%99");
//        System.out.println("result = " + result);
        for (int i = 0; i < 100; i++) {
            System.out.println(getRandomSleep());
        }
    }
}
