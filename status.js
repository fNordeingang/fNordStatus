var http = require('http'),
    url = require('url'),
    _ = require('underscore');
require('date-utils');

var lastPoll;
var closeAfterMinutes = 5;

http.createServer(function (req, res) {
  res.writeHead(200, {'Content-Type': 'text/plain'});
  var params = url.parse(req.url, true).query;
  if (!_.isUndefined(params.toggle) && (params.toggle === "fnord")) {
    lastPoll = new Date();
  }
  respondStatus(res);
}).listen(4242, "0.0.0.0");
console.log('Server running at http://127.0.0.1:4242/');

function respondStatus (res) {
  var minutesSinceLastPoll = _.isUndefined(lastPoll) ? closeAfterMinutes+1 : lastPoll.getMinutesBetween(new Date());
  if(minutesSinceLastPoll > closeAfterMinutes) {
    res.end("false");
  } else {
    res.end("true");
  }
}
