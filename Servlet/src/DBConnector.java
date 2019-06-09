import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;

public class DBConnector {
    //驱动
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    //数据库连接地址
    private static final String URL = "jdbc:mysql://localhost:3306/english?useOldAliasMetadataBehavior=true";
    //用户名d
    private static final String USER_NAME = "root";
    //密码
    private static final String PASSWORD = "123456";

    private Connection connection = null;

    private static DBConnector dbConnection = null;

    private PreparedStatement prst = null;

    Statement statement = null;
    private DBConnector(){
        try {
            //加载数据库类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            System.out.println("连接数据库...");
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            if(connection != null){
                System.out.println("已连接数据库");
                statement = connection.createStatement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DBConnector getConnection() {
        if(dbConnection == null){
            dbConnection = new DBConnector();
        }
        return dbConnection;
    }

    public ResultSet executeQuery(String sql) {
        try {
            System.out.println("正在执行语句:" + sql);
            CachedRowSetImpl rowSet = new CachedRowSetImpl();
            ResultSet rs = statement.executeQuery(sql);
            rowSet.populate(rs);
            rs.close();
            return rowSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean execute(String sql) {
        try {
            System.out.println("正在执行语句:" + sql);
            return statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try {
            prst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
