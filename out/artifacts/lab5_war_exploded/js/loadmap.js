var map;
var place;
var autocomplete;
var infowindow = new google.maps.InfoWindow();

function initialization() {
    showAllReports();
    initAutocomplete();
}

function showAllReports() {
    $.ajax({
        url: 'HttpServlet',
        type: 'POST',
        data: { "tab_id": "1"},
        success: function() {
            mapInitialization(reports);
        },
        error: function(xhr, status, error) {
            alert("An AJAX error occurred line 20 loadmap.js: " + status + "\nError: " + error);
        }
    });
}

function mapInitialization(reports) {
    const wisconsin  = { lat: 43.784, lng: -88.787 };

    var mapOptions = {
        mapTypeId : google.maps.MapTypeId.terrain, // Set the type of Map
        center: wisconsin,
    };

    // Render the map within the empty div
    map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

    var bounds = new google.maps.LatLngBounds ();

    $.each(reports, function(i, e) {
        if (e['event_type'] == "hail" || e['event_type'] == "wind"){
            var long = Number(e['longitude']);
            var lat = Number(e['latitude']);
            var latlng = new google.maps.LatLng(lat, long);

            bounds.extend(latlng);
        }
        else if (e['event_type'] == "tornado"){
            const flightPlanCoordinates = [
                { lat: Number(e['start_lat']), lng: Number(e['start_lon']) },
                { lat: Number(e['end_lat']), lng: Number(e['end_lon']) },
            ];
            console.log(flightPlanCoordinates)
            const flightPath = new google.maps.Polyline({
                path: flightPlanCoordinates,
                geodesic: true,
                strokeColor: "#FF0000",
                strokeOpacity: 1.0,
                strokeWeight: 2,
            });
            bounds.extend(flightPath);
        }

        // Create the infoWindow content
        var contentStr = '<h4>'+ e['event_type'] + '</h4><hr>';
        contentStr += '<p><b>' + 'Magnitude' + ':</b>&nbsp' + e['magnitude'] + '</p>';
        contentStr += '<p><b>' + 'County' + ':</b>&nbsp' + e['county'] +
            '</p>';
        contentStr += '<p><b>' + 'Injuries' + ':</b>&nbsp' + e['injuries'] +
            '</p>';
        contentStr += '<p><b>' + 'Fatalities' + ':</b>&nbsp' + e['fatalities'] +
            '</p>';
        if (e['event_type'] == 'tornado') {
            contentStr += '<p><b>' + 'length' + ':</b>&nbsp' +
                e['length'] + '</p>';
        }
        // else if (e['report_type'] == 'damage') {
        //     contentStr += '<p><b>' + 'Damage Type' + ':</b>&nbsp' + e['damage_type']
        //         + '</p>';
        // }

        //code line conditions to add image markers to map based on report type
        //(answer to question 2 in lab 6)
        //images from flaticon.com
        // if (e['report_type'] == 'damage') var icon = {
        //     url: "img/damage.png",
        //     scaledSize: new google.maps.Size(25,25),
        // }
        // else if (e['report_type'] == 'donation') icon = {
        //     url: "img/donation.png",
        //     scaledSize: new google.maps.Size(25,25),
        // }
        // else if (e['report_type'] == 'request') icon = {
        //     url: "img/request.png",
        //     scaledSize: new google.maps.Size(25,25),
        // }

        // Create the marker
        var marker = new google.maps.Marker({ // Set the marker
            position: latlng, // Position marker to coordinates
            map: map, // assign the marker to our map variable
            customInfo: contentStr,
            // icon: icon
        });

        // Add a Click Listener to the marker
        google.maps.event.addListener(marker, 'click', function() {
            // use 'customInfo' to customize infoWindow
            infowindow.setContent(marker['customInfo']);
            infowindow.open(map, marker); // Open InfoWindow
        });

    });

    map.fitBounds (bounds);

}

function initAutocomplete() {
    // Create the autocomplete object
    autocomplete = new google.maps.places.Autocomplete(document
        .getElementById('autocomplete'));

    // When the user selects an address from the dropdown, show the place selected
    autocomplete.addListener('place_changed', onPlaceChanged);
}

function onPlaceChanged() {

    place = autocomplete.getPlace();

    //Code to locate places that have a geometry in the address search bar, then present and zoom to place on map.
    //(answer to question 3 in lab 6)
    const searchMarker = new google.maps.Marker({
        map,
        anchorPoint: new google.maps.Point(0, -29),
    });

    searchMarker.setVisible(false);

    if (!place.geometry) {
        // User entered the name of a Place that was not suggested and
        // pressed the Enter key, or the Place Details request failed.
        window.alert("No details available for input: '" + place.name + "'");
        return;
    }

    if (place.geometry.viewport) {
        map.fitBounds(place.geometry.viewport);
    } else {
        map.setCenter(place.geometry.location);
        map.setZoom(17); // Why 17? Because it looks good.
    }
    searchMarker.setPosition(place.geometry.location);
    searchMarker.setVisible(true);
}

//Execute our 'initialization' function once the page has loaded.
google.maps.event.addDomListener(window, 'load', initialization);