$(function () {
  $.getJSON("http://status.fnordeingang.de", function ( status ) {
    if ( status ) {
      $("#status").html("Der fNordeingang ist zurzeit offen.");
    }
    else {
      $("#status").html("Der fNordeingang ist zurzeit geschlossen.");
    }
  });
});