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

  <!-- Bootstrap -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

  <!-- Google Map js libraries -->
  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC2PuNWeVXTLl_3jEee5Y0dy0YWgrUw6Bs&libraries=geometry,places" type="text/javascript"></script>

</head>

<body>

<div class="container-fluid">
  <div class="row">
    <div class="sidebar col-xs-3">

        <!-- Query Report Tab Panel -->
        <hr class="rounded">
          <title>Our title</title>
          <p1>info about tornadoes in WI</p1>
        </hr>
        <div>
          <h1>Turn on a storm event layer:</h1>
        </div>
        <div >
          <h2>Search tools</h2>
          <form id = "query_report_form">
            <div><label>Search by event type:</label>
              <select onchange="onSelectReportType(this)" name="report_type">
                <option value="">Select an event type</option>
                <option value="donation">Donation</option>
                <option value="request">Request</option>
                <option value="damage">Damage Report</option>
              </select>
            </div>
            <div class="additional_msg_div" style="visibility: hidden"><label class="additional_msg"></label>
              <select class="additional_msg_select" name="resource_or_damage"></select>
            </div>
            <div><label>Search by county:</label>
              <select name="disaster_type">
                <option value="">Select a county</option>
                <option value="flood">flood</option>
                <option value="wildfire">wildfire</option>
                <option value="earthquake">earthquake</option>
                <option value="tornado">tornado</option>
                <option value="hurricane">hurricane</option>
                <option value="other">other</option>
              </select>
            </div>
            <div><label>Search by storm length or storm severity:</label>
              <select name="disaster_type">
                <option value="">Select storm length</option>
                <option value="flood">flood</option>
                <option value="wildfire">wildfire</option>
                <option value="earthquake">earthquake</option>
                <option value="tornado">tornado</option>
                <option value="hurricane">hurricane</option>
                <option value="other">other</option>
              </select>
              <select name="disaster_type">
                <option value="">Select storm severity</option>
                <option value="flood">flood</option>
                <option value="wildfire">wildfire</option>
                <option value="earthquake">earthquake</option>
                <option value="tornado">tornado</option>
                <option value="hurricane">hurricane</option>
                <option value="other">other</option>
              </select>
            </div>
            <div><label>Search by date:</label>
            </div>
            <button type="submit" class="btn btn-default">
              <span class="glyphicon glyphicon-star"></span> Submit the query
            </button>
          </form>
        </div>
    </div>

    <div id="map-canvas" class="col-xs-9"></div>

  </div>
</div>

<script src="js/loadform.js"></script>
<script src="js/loadmap.js"></script>

</body>
</html>