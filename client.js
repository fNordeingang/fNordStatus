$(function () {
  $.getJSON("http://status.fnordeingang.de", setFnordStatus);

  function setFnordStatus ( status ) {
    if ( status ) {
      $("#status").html("Der fNordeingang ist zurzeit offen.");
    }
    else {
      $("#status").html("Der fNordeingang ist zurzeit geschlossen.");
    }
  }
});