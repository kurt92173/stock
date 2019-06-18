import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockDao {
    private StockView getStock(Connection conn, String id) throws SQLException {
        PreparedStatement psmt = null;
        String sql = "select * from stock where id = ? ";

        try {
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, id);

            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                return decode(rs);
            }

            return new StockView();
        } finally {
            if (psmt!= null) psmt.close();
        }
    }

    private StockView decode(ResultSet rs) throws SQLException {
        StockView view = new StockView();
        view.setId(rs.getString("id"));
        view.setName(rs.getString("name"));
        view.setType(rs.getString("type"));
        view.setTypeName(rs.getString("typeName"));
        view.setUpdateFlag(rs.getString("updateFlag"));

        return view;
    }
}
