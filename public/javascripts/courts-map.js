/**
 * Created by taylorak on 3/25/14.
 */

var map;
var infowindow;

function initialize() {
//currently set to Univeristy of Hawaii at Manoa as deafult
    var pos = new google.maps.LatLng(21.2970, -157.8170);

    map = new google.maps.Map(document.getElementById('map-canvas'), {
        center : pos,
        zoom : 15
    });

    infowindow = new google.maps.InfoWindow({
        map : map,
        position : pos,
        content : 'You are here.'
    });

    var request = {
        location : pos,
        radius : 50000,
        query : [ 'basketball' ]
    };

    infowindow = new google.maps.InfoWindow();
    var service = new google.maps.places.PlacesService(map);
    service.textSearch(request, callback);
}


function callback(results, status) {
    if (status == google.maps.places.PlacesServiceStatus.OK) {
        for ( var i = 0; i < results.length; i++) {
            createMarker(results[i]);
        }
    }
}

function createMarker(place) {
    var placeLoc = place.geometry.location;
    var marker = new google.maps.Marker({
        map : map,
        position : place.geometry.location
    });

    google.maps.event.addListener(marker, 'click', function() {
        infowindow.setContent(place.name + "<br />"
            + place.formatted_address);
        infowindow.open(map, this);
    });
}

google.maps.event.addDomListener(window, 'load', initialize);
