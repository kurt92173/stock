import com.google.gson.Gson;
import db.DbTool;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Connection;
import java.util.ArrayList;

public class Csv {
    private static final int SIZE = 10;
    private static final int INDEX_ID = 0;
    private static final int INDEX_NAME = 1;
    private static final int INDEX_VOLUME = 2;
    private static final int INDEX_AMOUNT = 3;
    private static final int INDEX_OPEN = 4;
    private static final int INDEX_HIGH = 5;
    private static final int INDEX_LOW = 6;
    private static final int INDEX_PRICE = 7;
    private static final int INDEX_DIFF = 8;
    private static final int INDEX_COUNT = 9;
    private static final String DATE = "2019"+"0618";
    private static final String PATH = "D:\\csv\\"+ DATE+".csv";

    public static ArrayList<StockPriceView> getDayPrice() throws Exception {
        ArrayList<StockPriceView> views = new ArrayList<StockPriceView>();
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(PATH));//檔案讀取路徑
            BufferedReader reader = new BufferedReader(isr);

            String line = null;
            while ((line = reader.readLine()) != null) {
                StockPriceView view = new StockPriceView();
                view.setDate(DATE);

                String item[] = line.split("\",\"");

                for (int index = 0; index < SIZE; index++) {
                    String data = item[index].trim().replace("\"", "");

                    switch (index) {
                        case INDEX_ID:
                            view.setId(data);
                            break;
                        case INDEX_NAME:
//                            view.setId(data);
                            break;
                        case INDEX_VOLUME:
                            view.setVolume(Integer.valueOf(data.replace(",", "")));
                            break;
                        case INDEX_AMOUNT:
                            view.setAmount(Double.valueOf(data.replace(",", "")));
                            break;
                        case INDEX_OPEN:
                            view.setOpen(Double.valueOf(data.replace(",", "")));
                            break;
                        case INDEX_HIGH:
                            view.setHigh(Double.valueOf(data.replace(",", "")));
                            break;
                        case INDEX_LOW:
                            view.setLow(Double.valueOf(data.replace(",", "")));
                            break;
                        case INDEX_PRICE:
                            view.setPrice(Double.valueOf(data.replace(",", "")));
                            break;
                        case INDEX_DIFF:
                            view.setDiff(Double.valueOf(data.replace(",", "").replace("X","")));
                            break;
                        case INDEX_COUNT:
                            view.setCount(Integer.valueOf(data.replace(",", "")));
                            break;
                    }

                }
                System.out.println(new Gson().toJson(view));
                views.add(view);
                System.out.println();
            }

            return views;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    static void  renewPrice() throws Exception {
        Connection conn = null;

        try {
            conn = DbTool.getConn();
            ArrayList<StockPriceView> views = getDayPrice();

            for (StockPriceView view : views) {
                PriceDayInMonth.insert(conn, view);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("壞掉了");
            e.printStackTrace();
        } finally {
            DbTool.closeConnection(conn);
        }
    }

    public static void getCsv() {
        try {
            URL website = new URL("http://www.twse.com.tw/exchangeReport/STOCK_DAY_ALL?response=open_data");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());

            FileOutputStream fos = new FileOutputStream(PATH);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteLineOne() {
        try {
            File inputFile = new File(PATH);
            if (!inputFile.exists()) throw new Exception("Csv is not existed.");

            File tempFile = new File("D:\\csv\\temp.csv");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String lineToRemove = "證券代號,證券名稱,成交股數,成交金額,開盤價,最高價,最低價,收盤價,漲跌價差,成交筆數";
            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if(trimmedLine.equals(lineToRemove)) {
                    System.out.println("trimmedLine:"+trimmedLine);
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            System.out.println("delete:"+inputFile.delete());
            boolean successful = tempFile.renameTo(inputFile);
            System.out.println("successful = " + successful);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        getCsv();
        deleteLineOne();
        renewPrice();
    }
}
