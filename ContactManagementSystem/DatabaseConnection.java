package ContactManagementSystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection connect(){
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/contactdb"+"?user=root&password=";
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                System.out.println("Connected to the database!");
            }
            else{
                System.out.println("Database Connect Failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
