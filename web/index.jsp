<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Web Project</title>

  <!-- Custom styles -->
  <link rel="stylesheet" href="css/style.css">

  <!-- jQuery -->
  <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
  <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

  <!-- Bootstrap -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

  <!-- Google Map js libraries -->
  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC2PuNWeVXTLl_3jEee5Y0dy0YWgrUw6Bs&libraries=geometry,places" type="text/javascript"></script>

</head>

<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
  <a class="navbar-brand">Severe Weather Wisconsin</a>
</nav>

<div class="container-fluid">
  <div class="row">
    <div class="sidebar col-xs-3">

      <!-- Tab Navis-->
      <ul class="nav nav-tabs">
        <li class="active"><a href="#create_event" data-toggle="tab">Report Event</a></li>
        <li><a href="#query_report" data-toggle="tab">Search</a></li>
      </ul>

      <!-- Tab panes -->
      <div class="tab-content ">
        <!-- Create Report Tab Panel -->
        <div class="tab-pane active" id="create_event">
          <form id = "create_event_form">
            <div><label>Select a disaster event type: </label>
              <select onchange="onSelectReportType(this)" name="report_type">
                <option value="">Choose the event type</option>
                <option value="is_tornado">Tornado</option>
                <option value="is_hail">Hail</option>
                <option value="is_wind">Wind</option>
              </select>
            </div>
            <hr weight = "2">
            <div><label>Fatalities:&nbsp</label><input placeholder="Number of fatalities" name="fatal"></div>
            <div><label>Injuries:&nbsp</label><input placeholder="Number of injuries" name="injur"></div>
            <div><label>Date:&nbsp</label><input placeholder="year-month-day" name="date"></div>
<%--            <div class="additional_msg_div" style="visibility: hidden"><label class="additional_msg"></label>--%>
<%--              <select class="additional_msg_select" name="additional_message"></select>--%>
<%--            </div>--%>
            <div><label class="mag">Magnitude:</label>
              <select class="mag_select" name="magnitude">
                <option value="">Magnitude</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
              </select>
            </div>
            <div><label>Address:</label>
              <input id="autocomplete" placeholder="Address" >
            </div>
            <button type="submit" class="btn btn-default" id="report_submit_btn">
              <span class="glyphicon glyphicon-star"></span> Submit
            </button>
          </form>
        </div>

        <!-- Query Report Tab Panel -->
        <div class="tab-pane" id="query_report">
          <form id = "query_report_form">
            <select onchange="onSelectReportType(this)" name="event_type">
              <option value="">Choose the event type</option>
              <option value="is_tornado">Tornado</option>
              <option value="is_hail">Hail</option>
              <option value="is_wind">Wind</option>
            </select>
            <hr weight = "2">
            <div><label>Search Tools:</label></div>
              <div><label>Select a county:</label>
                <select name="county">
                  <option value="">County</option>
                  <option value="adams">Adams</option>
                  <option value="ashland">Ashland</option>
                  <option value="barron">Barron</option>
                  <option value="dane">Dane</option>
                </select>
              </div>
              <div><label>Select by severity or length:</label>
<%--                <div class="additional_msg_div" style="visibility: hidden"><label class="additional_msg"></label>--%>
<%--                  <select class="additional_msg_select" name="additional_message"></select>--%>
<%--                </div>--%>
                <div><label class="mag">Magnitude:</label>
                  <select class="mag_select" name="magnitude">
                    <option value="">Magnitude</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                  </select>
                </div>
              <select name="length"><label class="mag">Length:</label>
                <option value="">Length</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
              </select>
              </div>
            <hr weight = "2">
            <p>
              <label for="amount">Filter by date:</label>
              <input type="text" id="amount" readonly style="border:0; color:#f6931f; font-weight:bold;">
            </p>
            <div id="slider-range"></div>

            <button type="submit" class="btn btn-default">
              <span class="glyphicon glyphicon-star"></span> Submit the query
            </button>
          </form>
        </div>
      </div>
    </div>

    <div id="map-canvas" class="col-xs-9"></div>

  </div>
</div>

<script src="js/loadform.js"></script>
<script src="js/loadmap.js"></script>

</body>
</html>