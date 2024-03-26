package Connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
    private static String HOST = "jdbc:mysql://techcaredb.cbg6264eke46.ap-southeast-2.rds.amazonaws.com :3306/TaskManagerSytem";
    private static int PORT = 3306;
    private static String DB_NAME = "TaskManagerSytem";
    private static String USERNAME = "admin";
    private static String PASSWORD = "shukri1234";
    public static Connection connection;


    public static Connection getConnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://techcaredb.cbg6264eke46.ap-southeast-2.rds.amazonaws.com :3306/TaskManagerSytem", "admin", "shukri1234");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return connection;
    }
}
