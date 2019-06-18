import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import db.DbTool;
import tool.DateTool;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

// 個股每月成交資訊
public class PriceDayInMonth {
    private static String data = "data";
    private static final int INDEX_DATE = 0;
    private static final int INDEX_VOLUME = 1;
    private static final int INDEX_AMOUNT = 2;
    private static final int INDEX_OPEN = 3;
    private static final int INDEX_HIGH = 4;
    private static final int INDEX_LOW = 5;
    private static final int INDEX_PRICE = 6;
    private static final int INDEX_DIFF = 7;
    private static final int INDEX_COUNT = 8;
    private static String TODAY;

    static {
//        TODAY = DateTool.getCurrentDate();
        TODAY = "20190614";
    }

    private static boolean renewPrice() throws Exception {
        int count = 0;
        Connection conn = null;

        try {
            conn = DbTool.getConn();

            for (EType type : EType.values()) {
                boolean isDone;

                do {
                        System.out.println("---> Type Strat: " + type.getType());
                    for (String id : getStock(conn, type.getType())) {
                            System.out.println("---> id Strat: " + id);
                        ArrayList<StockPriceView> arrayList = parsePriceResult(getPrice(id), id);
                        add(conn, arrayList);
                        update(conn, id, "Y");
                        count++;

                            System.out.println("---> id done: " + id);
                            System.out.println("---");
                        Thread.sleep(CUrlTool.getRandomSleep() * 1000);

                        if (count%50==49) {
                            System.out.println("在睡一次");
                            Thread.sleep(CUrlTool.getRandomSleep() * 1000);
                        }
                    }
                    isDone = isTypeDone(conn, type.getType());
                } while (!isDone);
                    System.out.println("---> Type Done: " + type.getType());
                    System.out.println();
            }

            return true;
        } catch (Exception e) {
            System.out.println("壞掉了");
            e.printStackTrace();
            return false;
        } finally {
            DbTool.closeConnection(conn);
        }
    }

    private static String getPrice(String id) throws IOException {
        System.out.println("TODAY = " + TODAY);
        String result = CUrlTool.getPrice("http://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=" + TODAY + "&stockNo=" + id);
        System.out.println("id = " + id);
        System.out.println("result = " + result);
        System.out.println();
        return result;
    }

    static ArrayList<StockPriceView> parsePriceResult(String result, String id) throws Exception {
        ArrayList<StockPriceView> views = new ArrayList<StockPriceView>();
        try {
            JsonReader reader = new JsonReader(new StringReader(result));
            reader.setLenient(true);
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (!data.equals(name)) {
                    reader.skipValue();
                    continue;
                }

                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginArray();

                    int index = 0;
                    StockPriceView view = new StockPriceView();
                    view.setId(id);
                    String temp = "";
                    while (reader.hasNext()) {
                        switch (index) {
                            case INDEX_DATE:
                                String date = reader.nextString();
                                date = date.replace("108","2019").replace("/","");
                                view.setDate(date);
                                break;
                            case INDEX_VOLUME:
                                view.setVolume(Integer.valueOf(reader.nextString().replace(",", "")));
                                break;
                            case INDEX_AMOUNT:
                                view.setAmount(Double.valueOf(reader.nextString().replace(",", "")));
                                break;
                            case INDEX_OPEN:
                                temp = reader.nextString();
                                if (!temp.contains("-")) {
                                    view.setOpen(Double.valueOf(temp.replace(",", "")));
                                }
                                break;
                            case INDEX_HIGH:
                                temp = reader.nextString();
                                if (!temp.contains("-")) {
                                    view.setHigh(Double.valueOf(temp.replace(",", "")));
                                }
                                break;
                            case INDEX_LOW:
                                temp = reader.nextString();
                                if (!temp.contains("-")) {
                                    view.setLow(Double.valueOf(temp.replace(",", "")));
                                }
                                break;
                            case INDEX_PRICE:
                                temp = reader.nextString();
                                if (!temp.contains("-")) {
                                    view.setPrice(Double.valueOf(temp.replace(",", "")));
                                }
                                break;
                            case INDEX_DIFF:
                                view.setDiff(Double.valueOf(reader.nextString().replace(",", "").replace("X", "")));
                                break;
                            case INDEX_COUNT:
                                view.setCount(Integer.valueOf(reader.nextString().replace(",", "")));
                                break;
                        }
                        index++;
                    }

                    DecimalFormat df = new DecimalFormat("##.00");
                    if (view.getPrice()!=0)
                    view.setPercentage(Double.parseDouble(df.format(view.getDiff()/view.getPrice()*100)));

                    views.add(view);
                    System.out.println(new Gson().toJson(view));
                    reader.endArray();
                }
                reader.endArray();
            }

            return views;
        } catch (Exception e) {
            keepLog(result, id);
            throw e;
        }
    }

    private static void add(Connection conn, ArrayList<StockPriceView> views) {
        try {
            for (StockPriceView view : views) {
                insert(conn, view);
            }
        } catch (SQLException e) {
            if (1062 != e.getErrorCode()) {
                e.printStackTrace();
            }
        }
    }

    public static void insert(Connection conn, StockPriceView view) throws SQLException {
        String sql = "INSERT INTO StockPrice (id,date,volume,amount,open,high,low,price,diff,count) VALUES (?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, view.getId());
            pstmt.setString(2, view.getDate());
            pstmt.setInt(3, view.getVolume());
            pstmt.setDouble(4, view.getAmount());
            pstmt.setDouble(5, view.getOpen());
            pstmt.setDouble(6, view.getHigh());
            pstmt.setDouble(7, view.getLow());
            pstmt.setDouble(8, view.getPrice());
            pstmt.setDouble(9, view.getDiff());
            pstmt.setInt(10, view.getCount());
            System.out.println(view.toString());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            if (1062 == e.getErrorCode()) {
//                System.out.println(view.getId() + "@" + view.getDate() + " is already exists.");
                return;
            }

            throw e;
        }
    }

    private static ArrayList<String> getStock(Connection dbConnection, String type) throws SQLException {
        ArrayList<String> ids = new ArrayList<String>();
        PreparedStatement psmt = null;

        String sql = "select id from stock where type = ? and updateFlag = 'N' order by id limit 10";

        try {
            psmt = dbConnection.prepareStatement(sql);
            psmt.setString(1, type);

            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getString("id"));
            }

            return ids;
        } finally {
            if (psmt!= null) psmt.close();
        }
    }

    private static void update(Connection conn, String id, String flag) throws SQLException {
        PreparedStatement psmt = null;
        try {
            String query = "update stock set updateFlag = ? where id = ?";
            psmt = conn.prepareStatement(query);
            psmt.setString(1, flag);
            psmt.setString(2, id);
            psmt.executeUpdate();
        } finally {
            if (null != psmt) psmt.close();
        }
    }

    private static boolean isTypeDone(Connection dbConnection, String type) throws SQLException {
        ArrayList<String> ids = new ArrayList<String>();
        PreparedStatement psmt = null;

        String sql = "select count(*) number from stock where type = ? and updateFlag = 'N'";

        try {
            psmt = dbConnection.prepareStatement(sql);
            psmt.setString(1, type);

            ResultSet rs = psmt.executeQuery();
            return !rs.next() || 0 == rs.getInt("number");

        } finally {
            if (psmt!= null) psmt.close();
        }
    }

    private static void keepLog(String result, String id) {
        System.out.println("keepLog");
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO parseerror (id, result) VALUES (?,?)";

        try {
            conn = DbTool.getConn();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, result);

            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbTool.closeConnection(conn);
        }
    }

    public static void main(String[] args) throws Exception {
//        String result = "{\"stat\":\"OK\",\"date\":\"20190408\",\"title\":\"108年04月 2429 銘旺科           各日成交資訊\",\"fields\":[\"日期\",\"成交股數\",\"成交金額\",\"開盤價\",\"最高價\",\"最低價\",\"收盤價\",\"漲跌價差\",\"成交筆數\"],\"data\":[[\"108/04/01\",\"5,001\",\"70,513\",\"14.10\",\"14.10\",\"14.10\",\"14.10\",\"X0.00\",\"3\"],[\"108/04/02\",\"3,170\",\"44,646\",\"14.10\",\"14.10\",\"14.10\",\"14.10\",\" 0.00\",\"2\"],[\"108/04/03\",\"0\",\"0\",\"--\",\"--\",\"--\",\"--\",\" 0.00\",\"0\"]],\"notes\":[\"符號說明:+/-/X表示漲/跌/不比價\",\"當日統計資訊含一般、零股、盤後定價、鉅額交易，不含拍賣、標購。\",\"ETF證券代號第六碼為K、M、S、C者，表示該ETF以外幣交易。\"]}";
//        System.out.println("result.length() = " + result.length());
//        parsePriceResult(result, "2429");
        System.out.println("date = " + TODAY);
        boolean isDone = false;
        System.out.println("Main Start:"+new Date());
        do {
            isDone = renewPrice();
            System.out.println("isDone"+isDone);

            if (!isDone) {
                System.out.println("等待重新跑............"+new Date());
                Thread.sleep(57000);
            };
        } while (!isDone);
        System.out.println("Main end:"+new Date());
    }
}