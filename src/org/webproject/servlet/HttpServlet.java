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

        // request report
        if (event_type.equalsIgnoreCase("is_tornado")) {
            System.out.println("is_tornado");
            String sql = "select magnitude from \"Tornado\"";
            queryReportHelper(sql,list,"tornado", magnitude, county);
        }

        // donation report
        if (event_type.equalsIgnoreCase("is_hail")) {
            String sql = "select magnitude, ST_X(geom) as " +
                    "longitude, ST_Y(geom) as latitude from \"Hail\"";
            queryReportHelper(sql,list,"hail",magnitude, county);
        }

        // damage report
        if (event_type.equalsIgnoreCase("is_wind")) {
            String sql = "select magnitude, ST_X(geom) as " +
                    "longitude, ST_Y(geom) as latitude from \"Wind\"";
            queryReportHelper(sql,list,"wind",magnitude, county);
        }

        response.getWriter().write(list.toString());
    }

    private void queryReportHelper(String sql, JSONArray list, String event_type,
                                   String mag, String county) throws SQLException {
        DBUtility dbutil = new DBUtility();
        System.out.println("HttpServlet: starting queryReportHelper()");
        if (mag != null) {
            sql += " where magnitude = '" + mag + "'";
        }
//        if (county != null) {
//                sql += " and county = '" + county + "'";
//        }
        ResultSet res = dbutil.queryDB(sql);
        while (res.next()) {
            // add to response
            HashMap<String, String> m = new HashMap<String,String>();
//            m.put("county", res.getString("county"));
//            if (report_type.equalsIgnoreCase("donation") ||
//                    report_type.equalsIgnoreCase("request")) {
//                m.put("resource_type", res.getString("resource_type"));
//            }
//            else if (report_type.equalsIgnoreCase("damage")) {
//                m.put("damage_type", res.getString("damage_type"));
//            }
            m.put("magnitude", res.getString("magnitude"));
            list.put(m);
            System.out.println(res.getString("magnitude"));
        }
    }

    public void main() throws JSONException {
    }
}
