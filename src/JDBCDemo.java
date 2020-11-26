import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCDemo {

    public static void main(String[] args) {
        Connection conn;
        Statement stmt;
        try {
            // load the JDBC driver
            Class.forName("org.postgresql.Driver");

            // establish connection
            String url = "jdbc:postgresql://wisevere.cmrbjhwgggxl.us-east-2.rds.amazonaws.com:5432/wisevere";
            conn = DriverManager.getConnection(url, "postgres", "admin576");

            // query the database
            String sql = "select * from \"Tornado\" where magnitude = 5";
            stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            // print the result
            if (res != null) {
                while (res.next()) {
                    System.out.println("magnitude: " + res.getString("magnitude"));
                    System.out.println("prop_loss: " + res.getString("prop_loss"));
                }
            }

            // clean up
            stmt.close();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}