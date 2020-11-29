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
            String sql = "select \"Tornado\".prop_loss, \"Tornado\".crop_loss, \"Tornado\".length, \"Tornado\".magnitude, \"Tornado\".fatalities, \"Tornado\".injuries, \"Tornado\".start_lat, \"Tornado\".start_lon, \"Tornado\".end_lat, \"Tornado\".end_lon from \"Tornado\", \"Counties\" where ST_intersects (ST_Transform(\"Tornado\".geom::geometry, 3071), \"Counties\".geom::geometry) and \"Counties\".county_nam = 'Sauk'";
            stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            // print the result
            if (res != null) {
                while (res.next()) {
                    System.out.println("length: " + res.getString("length"));
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