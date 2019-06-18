import db.DbTool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class StockPrice {
    public ArrayList<StockPriceView> getList(String date) throws Exception {
        ArrayList<StockPriceView> priceViews = new ArrayList<StockPriceView>();
        Connection conn = null;
        PreparedStatement psmt = null;
        String sql = "select * from stockprice where date = ?";
        try {
            conn = DbTool.getConn();
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, date);

            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                StockPriceView view = new StockPriceView();
                view.setId(rs.getString("id"));
                view.setDate(rs.getString("date"));
                view.setVolume(rs.getInt("volume"));
                view.setAmount(rs.getDouble("amount"));
                view.setOpen(rs.getDouble("open"));
                view.setHigh(rs.getDouble("high"));
                view.setLow(rs.getDouble("low"));
                view.setPrice(rs.getDouble("price"));
                view.setDiff(rs.getDouble("diff"));
                view.setCount(rs.getInt("count"));
                priceViews.add(view);
            }

            return priceViews;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbTool.closeConnection(conn);
        }
    }

    public static void main(String[] args) throws Exception {
        for (StockPriceView view : new StockPrice().getList("20190611")) {
            System.out.println("id:"+view.getId()+", Price:"+view.getPrice()+", Diff:"+view.getDiff()+", Percentage:"+(view.getPercentage())+"%");
//            System.out.println("view = " + view.toString());
        }
    }
}
