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

        String report_type = request.getParameter("report_type");
        String magnitude = request.getParameter("magnitude1");
        String date = request.getParameter("date");
        String year = date.split("-")[0];
        String month = date.split("-")[1];
        String day = date.split("-")[2];
        String time = request.getParameter("time");
        String injuries = request.getParameter("injuries");
        String fatalities = request.getParameter("fatalities");
        String prop_loss = request.getParameter("prop_loss");
        String crop_loss = request.getParameter("crop_loss");
        String lon = request.getParameter("lon");
        String lat = request.getParameter("lat");
        System.out.println(report_type + "," + magnitude + "," + date + "," + year + "," + month + "," + day + "," + time + "," + injuries + "," + fatalities + "," + lon + "," + lat);



        // 4. create specific report
        if (report_type.equals("is_tornado")) {
            sql = "insert into \"Tornado\" (year, month, day, date, time, magnitude, injuries, fatalities, prop_loss, crop_loss," +
                    " start_lat, start_lon) values ('" + year + "','" + month + "','" + day + "','" + date + "','"
                    + time + "','" + magnitude + "','" + injuries + "','" + fatalities + "','" + prop_loss + "','" + crop_loss + "','" + lat + "','" + lon + "')";
            System.out.println("Success! Tornado event created.");
        } else if (report_type.equals("is_hail")) {
            sql = "insert into \"Hail\" (geom, year, month, day, date, time, magnitude, injuries, fatalities, prop_loss, crop_loss," +
                    " lat, lon) values (" + "ST_GeomFromText('POINT(" + lon + " " + lat +
                    ")', 4326)" + ",'" + year + "','" + month + "','" + day + "','" + date + "','" + time + "','" + magnitude + "','" +
                    injuries + "','" + fatalities + "','" + prop_loss + "','" + crop_loss + "','" + lat + "','" + lon + "')";
            System.out.println("Success! Hail event created.");
        } else if (report_type.equals("is_wind")) {
            sql = "insert into \"Wind\" (geom, year, month, day, date, time, magnitude, injuries, fatalities, prop_loss, crop_loss," +
                    " lat, lon) values (" + "ST_GeomFromText('POINT(" + lon + " " + lat +
                    ")', 4326)" + ",'" + year + "','" + month + "','" + day + "','" + date + "','" + time + "','" + magnitude + "','" +
                    injuries + "','" + fatalities + "','" + prop_loss + "','" + crop_loss + "','" + lat + "','" + lon + "')";
            System.out.println("Success! Wind event created.");
        } else {
            return;
        }
        dbutil.modifyDB(sql);

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
        String magnitude = request.getParameter("magnitude");
        String date = request.getParameter("date");
        if (date == null){
            date = "1950-2020";
        }
        System.out.println("Date:" + date);
        String startYear = date.split("-")[0];
        String endYear = date.split("-")[1];
        System.out.println("Dates: " + startYear + " " + endYear);
        String month = request.getParameter("month");
        if (month == null){
            month = "1-12";
        }
        System.out.println("Month:" + month);
        String startMonth = month.split("-")[0];
        String endMonth = month.split("-")[1];
        System.out.println("Months: " + startMonth + " " + endMonth);
        String injuries = request.getParameter("injuries");
        String fatalities = request.getParameter("fatalities");
        String county = request.getParameter("county");
        String length = request.getParameter("length");

        if (event_type == null){
            event_type = "Tornado";
            String sql = "select \"Tornado\".prop_loss, \"Tornado\".date, \"Tornado\".crop_loss, \"Tornado\".length, \"Tornado\".magnitude, \"Tornado\".fatalities, \"Tornado\".injuries, \"Tornado\".start_lat, \"Tornado\".start_lon, \"Tornado\".end_lat, \"Tornado\".end_lon, \"Counties\".county_nam from \"Tornado\", \"Counties\" where ST_intersects (ST_Transform(\"Tornado\".geom::geometry, 3071), \"Counties\".geom::geometry)";
            queryReportHelper(sql,list,"tornado");
        }

        // tornado event
        if (event_type.equalsIgnoreCase("is_tornado")) {
            System.out.println("HttpServlet: starting tornado");
            String sql;
            sql = "select \"Tornado\".prop_loss, \"Tornado\".crop_loss, \"Tornado\".date, \"Tornado\".length, \"Tornado\".magnitude, \"Tornado\".fatalities, \"Tornado\".injuries, \"Tornado\".start_lat, \"Tornado\".start_lon, \"Tornado\".end_lat, \"Tornado\".end_lon, \"Counties\".county_nam from \"Tornado\", \"Counties\" where ST_intersects (ST_Transform(\"Tornado\".geom::geometry, 3071), \"Counties\".geom::geometry)";
            if (county !=null) {
                sql += " and county_nam = '" + county + "'";
            }
            if (fatalities !=null && fatalities.equals("> 5")) {
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
            if (startYear != null && endYear != null){
                sql += " and year between " + startYear + " and " + endYear;
            }
            if (startMonth != null && endMonth != null){
                sql += " and month between " + startMonth + " and " + endMonth;
            }
            System.out.println(sql);
            queryReportHelper(sql,list,"tornado");
        }

        // hail event
        if (event_type.equalsIgnoreCase("is_hail")) {
            System.out.println("HttpServlet: starting hail");
            String sql = "select prop_loss, date, crop_loss, county, fatalities, magnitude, injuries, ST_X(geom) as lon, ST_Y(geom) as lat from \"Hail\"";
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
            if (fatalities !=null){
                if (sql.endsWith("\"Hail\"")) {
                    sql += " where fatalities = " + fatalities;
                }
                else{
                    sql += " and fatalities = " + fatalities;
                }
            }
            if (startYear != null && endYear != null){
                if (sql.endsWith("\"Hail\"")) {
                    sql += " where year between " + startYear + " and " + endYear;
                }
                else{
                    sql += " and year between " + startYear + " and " + endYear;
                }
            }
            if (startMonth != null && endMonth != null){
                if (sql.endsWith("\"Hail\"")) {
                    sql += " where month between " + startMonth + " and " + endMonth;
                }
                else{
                    sql += " and month between " + startMonth + " and " + endMonth;
                }
            }
            System.out.println(sql);
            queryReportHelper(sql,list,"hail");
        }

        // wind event
        if (event_type.equalsIgnoreCase("is_wind")) {
            String sql = "select prop_loss, crop_loss, date, injuries, magnitude, fatalities, county, ST_X(geom) as lon, ST_Y(geom) as lat from \"Wind\"";
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
            if (startYear != null && endYear != null){
                if (sql.endsWith("\"Wind\"")) {
                    sql += " where year between " + startYear + " and " + endYear;
                }
                else{
                    sql += " and year between " + startYear + " and " + endYear;
                }
            }
            if (startMonth != null && endMonth != null){
                if (sql.endsWith("\"Wind\"")) {
                    sql += " where month between " + startMonth + " and " + endMonth;
                }
                else{
                    sql += " and month between " + startMonth + " and " + endMonth;
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
            // add to response
            HashMap<String, String> m = new HashMap<String,String>();
            m.put("magnitude", res.getString("magnitude"));
            m.put("injuries", res.getString("injuries"));
            m.put("date", res.getString("date"));
            if (event_type.equalsIgnoreCase("tornado")) {
                m.put("event_type", event_type);
                m.put("length", res.getString("length"));
                m.put("county_nam", res.getString("county_nam"));
                m.put("fatalities", res.getString("fatalities"));
                m.put("start_lat", res.getString("start_lat"));
                m.put("start_lon", res.getString("start_lon"));
                m.put("end_lat", res.getString("end_lat"));
                m.put("end_lon", res.getString("end_lon"));
                m.put("prop_loss", res.getString("prop_loss"));
                m.put("crop_loss", res.getString("crop_loss"));
            }
            if (event_type.equalsIgnoreCase("hail")) {
                m.put("event_type", event_type);
                m.put("lon", res.getString("lon"));
                m.put("lat", res.getString("lat"));
                m.put("fatalities", res.getString("fatalities"));
                m.put("county", res.getString("county"));
                m.put("prop_loss", res.getString("prop_loss"));
                m.put("crop_loss", res.getString("crop_loss"));
            }
            if (event_type.equalsIgnoreCase("wind")) {
                m.put("event_type", event_type);
                m.put("lon", res.getString("lon"));
                m.put("lat", res.getString("lat"));
                m.put("fatalities", res.getString("fatalities"));
                m.put("county", res.getString("county"));
                m.put("prop_loss", res.getString("prop_loss"));
                m.put("crop_loss", res.getString("crop_loss"));
            }
            list.put(m);
        }
    }

    public void main() throws JSONException {
    }
}
