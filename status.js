var http        = require('http'),
    url         = require('url'),
    _           = require('underscore'),
    twitter     = require('ntwitter'),
    date_utils  = require('date-utils');

/**
 * when did the mpd polled at last?
 * @type {Date}
 */
var lastPoll = null;

/**
 * what was the last state?
 * @type {boolean}
 */
var lastState = false;

/**
 * when to close the fNordeingang
 * @type {number}
 */
var closeAfterMinutes = 5;

/**
 * print usage and exit if correct length of arguments aren't passed
 */
if ( process.argv.length !== 6 ) {
  console.log("usage: node status.js [consumer_key] [consumer_secret] [access_token_key] [access_token_secret]");
  process.exit(1);
}

/**
 * initialize twitter with command line arguments
 */
var twit = new twitter({
    consumer_key: process.argv[2],
    consumer_secret: process.argv[3],
    access_token_key: process.argv[4],
    access_token_secret: process.argv[5]
});


/**
 * create a listening webserver
 */
http.createServer(onRequest).listen(4242, "0.0.0.0");
console.log('Server running at http://127.0.0.1:4242/');


/**
 * main request handling function
 * @param {object} req
 * @param {object} res
 */
function onRequest ( req, res ) {
  setHeaders(res);
  processRequest(req, res);
}


/**
 * set additional headers
 * @param {object} res
 */
function setHeaders ( res ) {
  res.writeHead(200, {
    'Access-Control-Allow-Origin'   : '*',
    'Access-Control-Allow-Methods'  : 'POST, GET, OPTIONS, PUT, DELETE',
    'Access-Control-Allow-Headers'  : '*',
    'Cache-Control'                 : 'no-cache, no-store, must-revalidate',
    'Content-Type'                  : 'application/javascript'
  });
}


/**
 * decide what type of request to process
 * @param {object} req
 * @param {object} res
 */
function processRequest ( req, res ) {
  if ( req.method === "GET" ) {
    doGet(req, res);
  }
}


/**
 * process GET requests
 * @param {object} req
 * @param {object} res
 */
function doGet ( req, res ) {
  var params = url.parse(req.url, true).query;

  if ( !_.isUndefined(params.toggle) && (params.toggle === "fnord") ) {
    lastPoll = new Date();
  }

  respondStatus(res);
}


/**
 * check last status, tweet if needed and then
 * respond the current status
 * @param {object} res
 */
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


/**
 * tweet the given status
 * @param {boolean} status
 */
function tweetFnordStatus (status) {
  if ( status ) {
    tweet("#fNordeingang is now open! " + lastPoll);
  }
  else {
    tweet("#fNordeingang has closed, see you soon! " + lastPoll);
  }
}


/**
 * tweet helper function
 * @param {string} msg
 */
function tweet (msg) {
  twit
    .verifyCredentials(function (err, data) {
      console.log(console.dir(err));
      console.log(console.dir(data));
    })
    .updateStatus(msg, function (err, data) {
      console.log(console.dir(err));
      console.log(console.dir(data));
    });
}