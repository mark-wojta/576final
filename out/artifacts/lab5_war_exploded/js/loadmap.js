var map;
var place;
var autocomplete;
var infowindow = new google.maps.InfoWindow();

function initialization() {
    showAllReports();
    // initAutocomplete();
}

function showAllReports() {
    $.ajax({
        url: 'HttpServlet',
        type: 'POST',
        data: { "tab_id": "1"},
        success: function(reports) {
            mapInitialization(reports);
        },
        error: function(xhr, status, error) {
            alert("An AJAX error occurred line 20 loadmap.js: " + status + "\nError: " + error);
        }
    });
}

function mapInitialization(reports) {
    // Basemap style from Snazzy Maps
    var snazzyMonochrome = [
        {
            "featureType": "all",
            "elementType": "labels.text.fill",
            "stylers": [
                {
                    "saturation": 36
                },
                {
                    "color": "#333333"
                },
                {
                    "lightness": 40
                }
            ]
        },
        {
            "featureType": "all",
            "elementType": "labels.text.stroke",
            "stylers": [
                {
                    "visibility": "on"
                },
                {
                    "color": "#ffffff"
                },
                {
                    "lightness": 16
                }
            ]
        },
        {
            "featureType": "all",
            "elementType": "labels.icon",
            "stylers": [
                {
                    "visibility": "off"
                }
            ]
        },
        {
            "featureType": "administrative",
            "elementType": "geometry.fill",
            "stylers": [
                {
                    "color": "#fefefe"
                },
                {
                    "lightness": 20
                }
            ]
        },
        {
            "featureType": "administrative",
            "elementType": "geometry.stroke",
            "stylers": [
                {
                    "color": "#fefefe"
                },
                {
                    "lightness": 17
                },
                {
                    "weight": 1.2
                }
            ]
        },
        {
            "featureType": "landscape",
            "elementType": "geometry",
            "stylers": [
                {
                    "color": "#f2f2f2"
                },
                {
                    "lightness": "0"
                }
            ]
        },
        {
            "featureType": "poi",
            "elementType": "geometry",
            "stylers": [
                {
                    "color": "#d6d7de"
                },
                {
                    "lightness": "0"
                }
            ]
        },
        {
            "featureType": "poi.park",
            "elementType": "geometry",
            "stylers": [
                {
                    "color": "#d6d7de"
                },
                {
                    "lightness": "0"
                }
            ]
        },
        {
            "featureType": "road.highway",
            "elementType": "geometry.fill",
            "stylers": [
                {
                    "color": "#ffffff"
                },
                {
                    "lightness": 17
                }
            ]
        },
        {
            "featureType": "road.highway",
            "elementType": "geometry.stroke",
            "stylers": [
                {
                    "color": "#ffffff"
                },
                {
                    "lightness": 29
                },
                {
                    "weight": 0.2
                }
            ]
        },
        {
            "featureType": "road.arterial",
            "elementType": "geometry",
            "stylers": [
                {
                    "color": "#ffffff"
                },
                {
                    "lightness": 18
                }
            ]
        },
        {
            "featureType": "road.local",
            "elementType": "geometry",
            "stylers": [
                {
                    "color": "#ffffff"
                },
                {
                    "lightness": 16
                }
            ]
        },
        {
            "featureType": "transit",
            "elementType": "geometry",
            "stylers": [
                {
                    "color": "#f2f2f2"
                },
                {
                    "lightness": 19
                }
            ]
        },
        {
            "featureType": "water",
            "elementType": "geometry",
            "stylers": [
                {
                    "color": "#b9c1c8"
                },
                {
                    "lightness": "0"
                }
            ]
        }
    ]

    const wisconsin  = { lat: 44.3, lng: -88.787 };

    // Render the map within the empty div
    map = new google.maps.Map(document.getElementById('map-canvas'), {
        center: wisconsin,
        zoom: 7,
        styles: snazzyMonochrome,
        // mapTypeId : google.maps.MapTypeId.terrain,
    });

    var bounds = new google.maps.LatLngBounds ();

    $.each(reports, function(i, e) {
        // Create the infoWindow content
        var contentStr = '<h4>'+ e['event_type'] + '</h4><hr>';
        contentStr += '<p><b>' + 'Magnitude' + ':</b>&nbsp' + e['magnitude'] + '</p>';
        contentStr += '<p><b>' + 'County' + ':</b>&nbsp' + e['county'] +
            '</p>';
        contentStr += '<p><b>' + 'Injuries' + ':</b>&nbsp' + e['injuries'] +
            '</p>';
        if (e['event_type'] != 'hail'){
            contentStr += '<p><b>' + 'Fatalities' + ':</b>&nbsp' + e['fatalities'] +
                '</p>';
        }
        contentStr += '<p><b>' + 'Crop Loss' + ':</b>&nbsp' + e['crop_loss'] +
            '</p>';
        contentStr += '<p><b>' + 'Property Loss' + ':</b>&nbsp' + e['prop_loss'] +
            '</p>';
        if (e['event_type'] == 'tornado') {
            contentStr += '<p><b>' + 'length' + ':</b>&nbsp' +
                e['length'] + '</p>';
        }

        if (e['event_type'] == "hail" || e['event_type'] == "wind"){
            var long = Number(e['longitude']);
            var lat = Number(e['latitude']);
            var latlng = new google.maps.LatLng(lat, long);

            const iconBase = "http://maps.google.com/mapfiles/kml/paddle/";
            const icons = {
                hail: {
                    url: iconBase + "blu-blank-lv.png",
                    scaledSize: new google.maps.Size(10, 10)
                },
                wind: {
                    url: iconBase + "ylw-blank-lv.png",
                    scaledSize: new google.maps.Size(10, 10)
                },
            };
            var icon;

            if (e['event_type'] == "hail"){
                icon = icons["wind"];
            }
            else{
                icon = icons["hail"];
            }
            // Create the marker
            var marker = new google.maps.Marker({ // Set the marker
                position: latlng, // Position marker to coordinates
                map: map, // assign the marker to our map variable
                customInfo: contentStr,
                icon: icon
            });

            // Add a Click Listener to the marker
            google.maps.event.addListener(marker, 'click', function() {
                // use 'customInfo' to customize infoWindow
                infowindow.setContent(marker['customInfo']);
                infowindow.open(map, marker); // Open InfoWindow
            });

            bounds.extend(latlng);
        }
        else if (e['event_type'] == "tornado"){
            const tornadoCoordinates = [
                { lat: Number(e['start_lat']), lng: Number(e['start_lon']) },
                { lat: Number(e['end_lat']), lng: Number(e['end_lon']) },
            ];

            var long = Number(e['start_lon']);
            var lat = Number(e['start_lat']);
            var latlng = new google.maps.LatLng(lat, long);

            const tornadoPath = new google.maps.Polyline({
                path: tornadoCoordinates,
                geodesic: true,
                strokeColor: "#FF0000",
                strokeOpacity: 1.0,
                strokeWeight: 3.5,
                customInfo: contentStr,
            });

            // Create the marker
            var marker = new google.maps.Marker({ // Set the marker
                position: latlng, // Position marker to coordinates
                map: map, // assign the marker to our map variable
                customInfo: contentStr,
                icon: {path: google.maps.SymbolPath.CIRCLE,
                    scale: 0},
            });

            // Add a Click Listener to the tornado path
            google.maps.event.addListener(tornadoPath, 'click', function() {
                // use 'customInfo' to customize infoWindow
                infowindow.setContent(marker['customInfo']);
                infowindow.open(map, marker); // Open InfoWindow
            });

            bounds.extend(latlng);
            tornadoPath.setMap(map);
        }

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
