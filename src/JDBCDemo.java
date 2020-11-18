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
            String url = "jdbc:postgresql://localhost:5432/disastermngt";
            conn = DriverManager.getConnection(url, "postgres", "admin");

            // query the database
            String sql = "select r.id as report_id, r.report_type, d.resource_type, f.first_name, " +
                    "r.disaster_type, r.time_stamp, ST_AsText(geom) as geometry from report as r, " +
                    " donation_report as d, person as f where r.id = 1";
            stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            // print the result
            if (res != null) {
                while (res.next()) {
                    System.out.println("id: " + res.getString("report_id"));
                    System.out.println("report_type: " + res.getString("report_type"));
                    System.out.println("resource_type: " + res.getString("resource_type"));
                    System.out.println("first_name: " + res.getString("first_name"));
                    System.out.println("disaster_type: " + res.getString("disaster_type"));
                    System.out.println("time_stamp: " + res.getString("time_stamp"));
                    System.out.println("geometry: " + res.getString("geometry"));
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