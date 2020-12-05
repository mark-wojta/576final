<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Wisconsin Severe Storm Events</title>

  <!-- Custom styles -->
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="css/style.css">

  <!-- jQuery -->
  <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
  <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

  <!-- Bootstrap -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

  <!-- Google Map js libraries -->
  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCi4AhR1NvdOFDiotj-_XmElmzDcVhvH5U&libraries=geometry,places" type="text/javascript"></script>

</head>

<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation" align="center">
  <a class="navbar-brand">Severe Weather Wisconsin</a>
</nav>

<div class="container-fluid">
  <div class="row">
    <div class="sidebar col-xs-3">

      <!-- Tab Navis-->
      <ul class="nav nav-pills red" id="pills-tab" role="tablist">
        <li><a href="#create_event" data-toggle="tab">Report Event</a></li>
        <li class="active"><a href="#query_report" data-toggle="tab">Query Event</a></li>
        <li><a href="#more_report" data-toggle="tab">More</a></li>
      </ul>

      <!-- Tab panes -->
      <div class="tab-content" id="pills-tabContent">
        <!-- Create Report Tab Panel -->
        <div class="tab-pane" id="create_event" role="tabpanel">
          <form id = "create_event_form">
            <div class="form-group">
              <h3 style="color:white;">Submit a Report</h3>
              <p>Use the form below to submit a Severe Weather Report. Report hail size, wind speed, tornadoes,
              storm severity, injuries, and fatalities. If a value is unknown, leave the category blank.</p>
              <p>The National Weather Service defines a severe weather event as a storm producing quarter sized (1 inch)
                or larger hail, wind speeds greater than 58 mph, tornadoes, or damage to trees and property.</p>
              <hr>
              <h3 style="color:white;">Event Type</h3>
              <select class="form-control" onchange="onSelectReportType(this)" name="report_type">
                <option value="">Choose the Event Type</option>
                <option value="is_tornado">Tornado</option>
                <option value="is_hail">Hail</option>
                <option value="is_wind">Wind</option>
              </select>
            </div>
            <hr>
            <h3 style="color:white;">Severe Event Details</h3>
            <div class="form-group">
              <label class="control-label col-sm-3">Fatalities:&nbsp</label>
              <div class="col-sm-9">
                <input class="form-control" placeholder="Number of Fatalities" name="fatalities">
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-sm-3">Injuries:&nbsp</label>
              <div class="col-sm-9">
                <input class="form-control" placeholder="Number of injuries" name="injuries">
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-sm-3">Date:&nbsp</label>
              <div class="col-sm-9">
                <input class="form-control" placeholder="YYYY-MM-DD" name="date">
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-sm-3">Time:&nbsp</label>
              <div class="col-sm-9">
                <input class="form-control" placeholder="HH:MM:SS" name="time">
              </div>
            </div>
<%--            <div class="additional_msg_div" style="visibility: hidden"><label class="additional_msg"></label>--%>
<%--              <select class="additional_msg_select" name="additional_message"></select>--%>
<%--            </div>--%>
            <div class="form-group">
              <label class="control-label col-sm-3">Magnitude:&nbsp</label>
              <div class="col-sm-7" id="change1">
                <input class="form-control" placeholder="Input EF Scale Rating" id="mag_select1" name="magnitude1">
              </div>
              <div class="col-sm-2">
                <button type="button" class="button button5" id="btnPopover" data-container="body" data-toggle="popover" data-trigger="hover" data-placement="right" data-content="Determining Tornado EF Scale Rating">
                  <a class="hyperlink color" href="https://www.weather.gov/oun/efscale" target="_blank"><i class="glyphicon glyphicon-info-sign"></i></a>
                </button>
              </div>
            </div>
            <br>
            <div class="form-group">
              <label class="control-label col-sm-3" id="change2">Property Loss:&nbsp</label>
              <div class="col-sm-9" id="change3">
                <input class="form-control" placeholder="Estimated Dollar Value (millions)" name="prop_loss">
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-sm-3">Crop Loss:&nbsp</label>
              <div class="col-sm-9">
                <input class="form-control" placeholder="Estimated Dollar Value (millions)" name="crop_loss">
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-sm-3">Address:</label>
              <div class="col-sm-9">
                <input class="form-control" id="autocomplete" placeholder="Address" >
              </div>
            </div>
            <br>
            <div class="form-group">
              <div class="col-sm-offset-3 col-sm-9">
                <button type="submit" class="btn btn-default" id="report_submit_btn" style="margin-top:20px;">Submit Report</button>
              </div>
            </div>
          </form>
        </div>

        <!-- Query Report Tab Panel -->
        <div class="tab-pane active" id="query_report" role="tabpanel">
          <form id = "query_report_form">
            <div class="form-group">
              <h3>Search Tools</h3>
              <p>Use the tools below to search for events by severe weather type, counties,
                storm severity, injuries, fatalities, tornado path length, and event date.</p>
              <p>Using the default value will include all values of that search parameter.</p>
              <hr>
              <h3>Event Type</h3>
                <select class="form-control" onchange="onSelectReportType(this)" name="event_type">
                  <option value="">Choose the Event Type</option>
                  <option value="is_tornado">Tornado</option>
                  <option value="is_hail">Hail</option>
                  <option value="is_wind">Wind</option>
                </select>
            </div>
            <hr>
            <div class="form-group">
              <h3>Location</h3>
              <select class="form-control" name="county">
                <option value="">Select a County</option>
                <option value="Adams">Adams</option>
                <option value ="Ashland">Ashland</option>
                <option value="Barron">Barron</option>
                <option value="Bayfield">Bayfield</option>
                <option value="Brown">Brown</option>
                <option value="Buffalo">Buffalo</option>
                <option value="Burnett">Burnett</option>
                <option value="Calumet">Calumet</option>
                <option value="Chippewa">Chippewa</option>
                <option value="Clark">Clark</option>
                <option value="Columbia">Columbia</option>
                <option value="Crawford">Crawford</option>
                <option value="Dane">Dane</option>
                <option value="Dodge">Dodge</option>
                <option value="Door">Door</option>
                <option value="Douglas">Douglas</option>
                <option value="Dunn">Dunn</option>
                <option value="Eau Claire">Eau Claire</option>
                <option value="Florence">Florence</option>
                <option value="Fond Du Lac">Fond Du Lac</option>
                <option value="Forest">Forest</option>
                <option value="Grant">Grant</option>
                <option value="Green">Green</option>
                <option value="Green Lake">Green Lake</option>
                <option value="Iowa">Iowa</option>
                <option value="Iron">Iron</option>
                <option value="Jackson">Jackson</option>
                <option value="Jefferson">Jefferson</option>
                <option value="Juneau">Juneau</option>
                <option value="Kenosha">Kenosha</option>
                <option value="Kewaunee">Kewaunee</option>
                <option value="La Crosse">La Crosse</option>
                <option value="Lafayette">Lafayette</option>
                <option value="Langlade">Langlade</option>
                <option value="Lincoln">Lincoln</option>
                <option value="Manitowoc">Manitowoc</option>
                <option value="Marathon">Marathon</option>
                <option value="Marinette">Marinette</option>
                <option value="Marquette">Marquette</option>
                <option value="Menominee">Menominee</option>
                <option value="Milwaukee">Milwaukee</option>
                <option value="Monroe">Monroe</option>
                <option value="Oconto">Oconto</option>
                <option value="Oneida">Oneida</option>
                <option value="Outagamie">Outagamie</option>
                <option value="Ozaukee">Ozuakee</option>
                <option value="Pepin">Pepin</option>
                <option value="Pierce">Pierce</option>
                <option value="Polk">Polk</option>
                <option value="Portage">Portage</option>
                <option value="Price">Price</option>
                <option value="Racine">Racine</option>
                <option value="Richland">Richland</option>
                <option value="Rock">Rock</option>
                <option value="Rusk">Rusk</option>
                <option value="St. Croix">St. Croix</option>
                <option value="Sauk">Sauk</option>
                <option value="Sawyer">Sawyer</option>
                <option value="Shawano">Shawano</option>
                <option value="Sheboygan">Sheboygan</option>
                <option value="Taylor">Taylor</option>
                <option value="Trempealeau">Trempeleau</option>
                <option value="Vernon">Vernon</option>
                <option value="Vilas">Vilas</option>
                <option value="Walworth">Walworth</option>
                <option value="Washburn">Washburn</option>
                <option value="Washington">Washington</option>
                <option value="Waukesha">Waukesha</option>
                <option value="Waupaca">Waupaca</option>
                <option value="Waushara">Waushara</option>
                <option value="Winnebago">Winnebago</option>
                <option value="Wood">Wood</option>
              </select>
            </div>
            <hr>
            <div class="severity">
              <div class="form-group">
                <h3 class="mag">Storm Severity</h3>
                  <label class="control-label col-sm-3" style="font-style: italic">Magnitude:</label>
                  <div class="col-sm-9">
                    <select class="form-control mag_select" id="mag_select" name="magnitude">
                      <option value="">Magnitude</option>
                      <option value="1">1</option>
                      <option value="2">2</option>
                      <option value="3">3</option>
                      <option value="4">4</option>
                      <option value="5">5</option>
                    </select>
                  </div>
              </div>
              <div class="form-group">
                <div class="fatal_div" style="visibility: hidden">
                  <label class="control-label col-sm-3 fatalities" style="font-style: italic"></label>
                  <div class="col-sm-9">
                    <select class="form-control fatal_select" id="fatal_select" name="fatalities"></select>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-sm-3 injuries" style="font-style: italic">Injuries:</label>
                <div class="col-sm-9">
                  <select class="form-control inj_select" name="injuries">
                    <option value="">Injuries</option>
                    <option value="0-5">0-5</option>
                    <option value="6-10">6-10</option>
                    <option value="11-20">11-20</option>
                    <option value="21-50">21-50</option>
                    <option value="> 50">Over 50</option>
                  </select>
                </div>
              </div>
              <div class="form-group length_div" style="visibility: hidden">
                <label class="control-label col-sm-3 length" style="font-style: italic"></label>
                <div class="col-sm-9">
                  <select class="form-control length_select" name="length"></select>
                </div>
              </div>
            </div>
            <div class="form-group">
              <hr>
              <h3 style="padding-top: 20px;">Search by Event Date</h3>
              <p>
                <label for="date">Filter by year:</label>
                <input type="text" id="date" name="date" readonly style="border:0; color:#ffffff; font-weight:bold;">
              </p>
            </div>
            <div class="form-group" id="slider-range"></div>
            <p>
             <label for="month">Filter by month:</label>
             <input type="text" id="month" name="month" readonly style="border:0; color:#ffffff; font-weight:bold;">
            </p>
            <div id="slider-range-month"></div>
            <hr>
            <button type="submit" class="btn btn-default">Submit the query</button>
          </form>
        </div>

        <div class="tab-pane" id="more_report" role="tabpanel">
          <form id = "more_info_form">
            <h4>About:</h4>
            <p>This app displays tornado, wind, and hail events in Wisconsin. The user can view storm event
              attributes including fatalities,
              injuries, crop loss, property loss, date, length in miles as popups on the map.
              The user can then determine where and how many storm events occurred in a given time period,
              where and how many severe storms occurred, and where/how many fatalities, injuries, or damage
              to property resulted from various storm events.
              The purpose of this app is to provide data to aid in formulating a mitigation plan
              or disaster response plan for areas of Wisconsin that are likely to experience severe storms.
              It could also indicate where funding should be focused on projects like tornado saferooms and
              storm shelters. </p>
            <h5>Links:</h5>
            <ul>
              <li><a href="https://www.spc.noaa.gov/gis/svrgis/">National Oceanic and Atmospheric Administration (Source Data and More National Weather Data)</a></li>
              <br>
              <li><a href="https://search.library.wisc.edu/search/digital?q=tornadoes">University of Wisconsin-Madison Libraries (Archived Wisconsin Tornado Imagery)</a></li>
            </ul>
            <p><small>Map designed and created by Abby Gleason, Mark Wojta, and Kerry Hanko</small></p>
          </form>
        </div>
      </div>
    </div>

    <div id="map-canvas" class="col-xs-9"></div>

  </div>
</div>

<script type="text/javascript">

</script>
<script src="js/loadform.js"></script>
<script src="js/loadmap.js"></script>

</body>
</html>