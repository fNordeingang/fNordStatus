var http = require('http'),
    url = require('url'),
    _ = require('underscore');
require('date-utils');

var lastPoll;
var closeAfterMinutes = 5;

http.createServer(function (req, res) {
  res.writeHead(200, {
    'Access-Control-Allow-Origin' : '*',
    'Access-Control-Allow-Methods': 'POST, GET, OPTIONS, PUT, DELETE',
    'Access-Control-Allow-Headers': '*',
    'Cache-Control'               : 'no-cache, no-store, must-revalidate',
    'Content-Type'                : 'application/javascript'
  });

  if (req.method === "GET") {
    var params = url.parse(req.url, true).query;

    if (!_.isUndefined(params.toggle) && (params.toggle === "fnord")) {
      lastPoll = new Date();
    } else {
      respondStatus(res);
    }
  }
}).listen(4242, "0.0.0.0");
console.log('Server running at http://127.0.0.1:4242/');

function respondStatus (res) {
  var minutesSinceLastPoll = _.isUndefined(lastPoll) ?
                                closeAfterMinutes+1 :
                                lastPoll.getMinutesBetween(new Date());

  if(minutesSinceLastPoll > closeAfterMinutes) {
    res.end("false");
  } else {
    res.end("true");
  }
}
