package org.webproject.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webproject.servlet.DBUtility;

/*
 * Servlet implementation class HttpServlet
 */
@WebServlet("/HttpServlet")
public class HttpServlet extends javax.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;

    /*
     * @see javax.servlet.http.HttpServlet#javax.servlet.http.HttpServlet()
     */
    public HttpServlet() {
        super();
    }

    /*
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }


    /*
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String tab_id = request.getParameter("tab_id");

        System.out.println("HttpServlet: starting doPost() tab_id:" + tab_id);

        // create a report
        if (tab_id.equals("0")) {
            System.out.println("A report is submitted!");
            try {
                createReport(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // query reports
        else if (tab_id.equals("1")) {
            try {
                queryReport(request, response);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void createReport(HttpServletRequest request, HttpServletResponse
            response) throws SQLException, IOException {
        DBUtility dbutil = new DBUtility();
        String sql;

        System.out.println("HttpServlet: starting createReport()");

        String is_tornado = request.getParameter("is_tornado");
        String is_hail = request.getParameter("is_hail");
        String is_wind = request.getParameter("is_wind");

        String magnitude = request.getParameter("magnitude");
        String date = request.getParameter("date");
        String year = date.split("-")[0];
        String month = date.split("-")[1];
        String day = date.split("-")[2];
        String time = request.getParameter("time");
        String injuries = request.getParameter("injur");
        String fatalities = request.getParameter("fatal");
        String prop_loss = request.getParameter("prop_loss");
        String crop_loss = request.getParameter("crop_loss");
        String county = request.getParameter("county");

//        // 4. create specific report
//        if (is_tornado != null) {
//            sql = "insert into \"Tornado\" (year, month, day, date, time, magnitude, injuries, fatalities, prop_loss, crop_loss, lat, lon, county) values ('"
//                    + year + "'," + month + "'," + day + "'," + date + "'," + time + "'," + magnitude + injuries + "'," + fatalities + "'," + prop_loss + "'," + crop_loss + "'," + lat + "'," + lon + "'," + county + ")";
//            System.out.println("Success! Tornado event created.");
//        } else if (is_hail != null) {
//            sql = "insert into \"Hail\" (report_id, resource_type) values ('"
//                    + report_id + "'," + add_msg + ")";
//            System.out.println("Success! Hail event created.");
//        } else if (is_wind != null) {
//            sql = "insert into \"Wind\" (report_id, damage_type) values ('"
//                    + report_id + "'," + add_msg + ")";
//            System.out.println("Success! Wind event created.");
//        } else {
//            return;
//        }
//        dbutil.modifyDB(sql);

        // response that the report submission is successful
        JSONObject data = new JSONObject();
        try {
            data.put("status", "success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.getWriter().write(data.toString());

    }

    private void queryReport(HttpServletRequest request, HttpServletResponse
            response) throws JSONException, SQLException, IOException {
        JSONArray list = new JSONArray();
        System.out.println("HttpServlet: starting queryReport()");

        String event_type = request.getParameter("event_type");
        System.out.println("event_type:" + event_type);
        String magnitude = request.getParameter("magnitude");
//        String date = request.getParameter("date");
//        String year = date.split("-")[0];
//        String month = date.split("-")[1];
//        String day = date.split("-")[2];
//        String time = request.getParameter("time");
        String injuries = request.getParameter("injuries");
        String fatalities = request.getParameter("fatalities");
//        String prop_loss = request.getParameter("prop_loss");
//        String crop_loss = request.getParameter("crop_loss");
        String county = request.getParameter("county");
        String length = request.getParameter("length");

        // tornado event
        if (event_type.equalsIgnoreCase("is_tornado")) {
            System.out.println("HttpServlet: starting tornado");
            String sql;
//            if (county != null){
//                sql = "select length, magnitude, fatalities, injuries, start_lat, start_lon, end_lat, end_lon from \"Tornado\" where ";
//            }
//            else {
                sql = "select \"Tornado\".prop_loss, \"Tornado\".crop_loss, \"Tornado\".length, \"Tornado\".magnitude, \"Tornado\".fatalities, \"Tornado\".injuries, \"Tornado\".start_lat, \"Tornado\".start_lon, \"Tornado\".end_lat, \"Tornado\".end_lon from \"Tornado\", \"Counties\" where ST_intersects (ST_Transform(\"Tornado\".geom::geometry, 3071), \"Counties\".geom::geometry)";
                System.out.println(county);
//            }
            if (county !=null) {
                sql += " and county_nam = '" + county + "'";
            }
            if (fatalities !=null && fatalities.equals(">5")) {
                sql += " and fatalities " + fatalities;
            }else if (fatalities != null){
                sql += " and fatalities = " + fatalities;
            }
            System.out.println("Fatalities:" + fatalities);
            if (magnitude != null){
                sql += " and magnitude = " + magnitude;
            }
            System.out.println("Magnitude:" + magnitude);
            if (length != null && length.equals("0-25 miles")){
                sql += " and length between 0.0 and 25.0";
            }
            else if (length != null && length.equals("25-50 miles")){
                sql += " and length between 25.0 and 50.0";
            }
            else if (length != null && length.equals("50-75 miles")){
                sql += " and length between 50.0 and 75.0";
            }
            else if (length != null && length.equals("75-100 miles")){
                sql += " and length between 75.0 and 100.0";
            }
            else if (length != null && length.equals("> 100 miles")){
                sql += " and length > 100.0";
            }
            System.out.println("Length:" + length);
            if (injuries !=null && injuries.equals("0-5")){
                sql += " and injuries between 0 and 5";
            }
            else if (injuries !=null && injuries.equals("6-10")){
                sql += " and injuries between 6 and 10";
            }
            else if (injuries !=null && injuries.equals("11-20")){
                sql += " and injuries between 11 and 20";
            }
            else if (injuries !=null && injuries.equals("21-50")){
                sql += " and injuries between 21 and 50";
            }
            else if (injuries !=null && injuries.equals("> 51")){
                sql += " and injuries > 51";
            }
            System.out.println(sql);
            queryReportHelper(sql,list,"tornado");
        }

        // hail event
        if (event_type.equalsIgnoreCase("is_hail")) {
            System.out.println("HttpServlet: starting hail");
            String sql = "select prop_loss, crop_loss, county, magnitude, injuries, ST_X(geom) as longitude, ST_Y(geom) as latitude from \"Hail\"";
            if (magnitude !=null && magnitude.equals("0-1")){
                sql += " where magnitude between 0.0 and 1.0";
            }
            else if (magnitude !=null && magnitude.equals("1-2")){
                sql += " where magnitude between 1.0 and 2.0";
            }
            else if (magnitude !=null && magnitude.equals("2-3")){
                sql += " where magnitude between 2.0 and 3.0";
            }
            else if (magnitude !=null && magnitude.equals("3-4")){
                sql += " where magnitude between 3.0 and 4.0";
            }
            else if (magnitude !=null && magnitude.equals("4-5")){
                sql += " where magnitude between 4.0 and 5.0";
            }
            else if (magnitude !=null && magnitude.equals("5-6")){
                sql += " where magnitude between 5.0 and 6.0";
            }
            else if (magnitude !=null && magnitude.equals("6-7")){
                sql += " where magnitude between 6.0 and 7.0";
            }
            if (county != null){
                if (sql.endsWith("\"Hail\"")){
                    sql += " where county = '"+ county + "'";
                }
                else{
                    sql += " and county = '"+ county + "'";
                }
            }
            System.out.println("Injuries:" + injuries);
            if (injuries !=null && injuries.equals("> 4")){
                if (sql.endsWith("\"Hail\"")){
                    sql += " where injuries " + injuries;
                }
                else{
                    sql += " and injuries " + injuries;
                }
            }else if (injuries !=null){
                if (sql.endsWith("\"Hail\"")) {
                    sql += " where injuries = " + injuries;
                }
                else{
                    sql += " and injuries = " + injuries;
                }
            }
            System.out.println(sql);
            queryReportHelper(sql,list,"hail");
        }

        // wind event
        if (event_type.equalsIgnoreCase("is_wind")) {
            String sql = "select prop_loss, crop_loss, injuries, magnitude, fatalities, county, ST_X(geom) as longitude, ST_Y(geom) as latitude from \"Wind\"";
            if (magnitude !=null && magnitude.equals("0-25 mph")){
                sql += " where magnitude between 0 and 25";
            }
            else if (magnitude !=null && magnitude.equals("25-50 mph")){
                sql += " where magnitude between 25 and 50";
            }
            else if (magnitude !=null && magnitude.equals("50-75 mph")){
                sql += " where magnitude between 50 and 75";
            }
            else if (magnitude !=null && magnitude.equals("75-100 mph")){
                sql += " where magnitude between 75 and 100";
            }
            else if (magnitude !=null && magnitude.equals("> 100 mph")) {
                sql += " where magnitude > 100";
            }
            if (fatalities != null){
                if (sql.endsWith("\"Wind\"")) {
                    sql += " where fatalities = " + fatalities;
                }
                else{
                    sql += " and fatalities = " + fatalities;
                }
            }
            if (injuries !=null && injuries.equals("> 5")) {
                if (sql.endsWith("\"Wind\"")) {
                    sql += " where injuries " + injuries;
                }
                else{
                    sql += " and injuries " + injuries;
                }
            }else if (injuries !=null){
                if (sql.endsWith("\"Wind\"")) {
                    sql += " where injuries = " + injuries;
                }
                else{
                    sql += " and injuries = " + injuries;
                }
            }
            if (county != null){
                if (sql.endsWith("\"Wind\"")) {
                    sql += " where county = '"+ county + "'";
                }
                else{
                    sql += " and county = '"+ county + "'";
                }
            }
            queryReportHelper(sql,list,"wind");
        }

        response.getWriter().write(list.toString());
    }

    private void queryReportHelper(String sql, JSONArray list, String event_type) throws SQLException {
        DBUtility dbutil = new DBUtility();
        System.out.println("HttpServlet: starting queryReportHelper()");
        ResultSet res = dbutil.queryDB(sql);
        while (res.next()) {
            System.out.println("Event type:" + event_type);
            System.out.println(res);
            // add to response
            HashMap<String, String> m = new HashMap<String,String>();
                m.put("magnitude", res.getString("magnitude"));
                m.put("injuries", res.getString("injuries"));
            if (event_type.equalsIgnoreCase("tornado")) {
                System.out.println("Event type:" + event_type);
                m.put("event_type", event_type);
                m.put("length", res.getString("length"));
//                m.put("county", res.getString("county_nam"));
                m.put("fatalities", res.getString("fatalities"));
                m.put("start_lat", res.getString("start_lat"));
                m.put("start_lon", res.getString("start_lon"));
                m.put("end_lat", res.getString("end_lat"));
                m.put("end_lon", res.getString("end_lon"));
                m.put("county", res.getString("prop_loss"));
                m.put("county", res.getString("crop_loss"));
            }
            if (event_type.equalsIgnoreCase("hail")) {
                m.put("event_type", event_type);
                m.put("longitude", res.getString("longitude"));
                m.put("latitude", res.getString("latitude"));
                m.put("county", res.getString("county"));
                m.put("county", res.getString("prop_loss"));
                m.put("county", res.getString("crop_loss"));
            }
            if (event_type.equalsIgnoreCase("wind")) {
                m.put("event_type", event_type);
                m.put("longitude", res.getString("longitude"));
                m.put("latitude", res.getString("latitude"));
                m.put("fatalities", res.getString("fatalities"));
                m.put("county", res.getString("county"));
                m.put("county", res.getString("prop_loss"));
                m.put("county", res.getString("crop_loss"));
            }
            list.put(m);
        }
    }

    public void main() throws JSONException {
    }
}
