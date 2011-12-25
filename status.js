var http = require('http'),
  url = require('url'),
  _ = require('underscore'),
  twitter = require('ntwitter'),
  date_utils = require('date-utils');

var lastPoll = null;
var lastState = false;
var closeAfterMinutes = 5;

var twit = new twitter({
    consumer_key: process.argv[2],
    consumer_secret: process.argv[3],
    access_token_key: process.argv[4],
    access_token_secret: process.argv[5]
});

http.createServer(onRequest).listen(4242, "0.0.0.0");

console.log('Server running at http://127.0.0.1:4242/');


function onRequest ( req, res ) {
  setHeaders(res);
  processRequest(req, res);
}

function setHeaders ( res ) {
  res.writeHead(200, {
    'Access-Control-Allow-Origin'   : '*',
    'Access-Control-Allow-Methods'  : 'POST, GET, OPTIONS, PUT, DELETE',
    'Access-Control-Allow-Headers'  : '*',
    'Cache-Control'                 : 'no-cache, no-store, must-revalidate',
    'Content-Type'                  : 'application/javascript'
  });
}

function processRequest ( req, res ) {
  if ( req.method === "GET" ) {
    doGet(req, res);
  }
}

function doGet ( req, res ) {
  var params = url.parse(req.url, true).query;

  if ( !_.isUndefined(params.toggle) && (params.toggle === "fnord") ) {
    lastPoll = new Date();
  }

  respondStatus(res);
}

function respondStatus ( res ) {
  var minutesSinceLastPoll = lastPoll === null ?
    closeAfterMinutes + 1 :
    lastPoll.getMinutesBetween(new Date());

  if ( minutesSinceLastPoll > closeAfterMinutes ) {
    if ( lastState ) tweetFnordStatus( !lastState );

    lastState = false;
    res.end("false");
  }
  else {
    if ( !lastState ) tweetFnordStatus( !lastState );

    lastState = true;
    res.end("true");
  }
}

function tweetFnordStatus (status) {
  if ( status ) {
    tweet("#fNordeingang is now open! " + lastPoll);
  }
  else {
    tweet("#fNordeingang has closed, see you soon! " + lastPoll);
  }
}

function tweet (msg) {
  twit
    .updateStatus(msg, function (err, data) {});
}

