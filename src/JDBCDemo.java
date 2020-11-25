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

            /* query the database */
            String sql = "select * from \"Tornado\"";
            /*
            String sql = "select r.id as report_id, r.report_type, d.resource_type, f.first_name, " +
                    "r.disaster_type, r.time_stamp, ST_AsText(geom) as geometry from report as r, " +
                    " donation_report as d, person as f where r.id = 1";*/
            stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            // print the result
            if (res != null) {
                while (res.next()) {
                    System.out.println(res.getString("injuries"));
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