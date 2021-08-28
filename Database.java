package viewer;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    final static String query = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";
    static SQLiteDataSource dataSource = new SQLiteDataSource();//创建SQLiteDataSource对象

    public static ArrayList<String> forTableComboBox(String database) {
        ArrayList<String> columnNames = new ArrayList<>();
        String url = "jdbc:sqlite:" + database;
        dataSource.setUrl(url);
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)){
            while (rs.next()) {
                columnNames.add(rs.getString("name"));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
//               return null;
        }

        return columnNames;
    }

    public static String forTableTable(String database, String queryTable, myTableModel myTableModel) {
        String url = "jdbc:sqlite:" + database;
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection(); //创立连接
             Statement stmt = con.createStatement();      //创建一个对象数据库操作对象
             ResultSet rs = stmt.executeQuery(queryTable))//执行查询 （）queryTable)
        {
            ResultSetMetaData metaData = rs.getMetaData();
            String[] cols = new String[metaData.getColumnCount()];
            for (int i = 1; i <= cols.length; i++) {
                cols[i - 1] = metaData.getColumnLabel(i); //column number from 1...;
            }
            ArrayList<Object[]> rows = new ArrayList<>();
            Object[] row;
            while (rs.next()) {
                row = new Object[metaData.getColumnCount()];
                for (int i = 1; i <= row.length; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                rows.add(row);
            }
            myTableModel.setTableData(cols,rows);
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }return null;

    }
}
