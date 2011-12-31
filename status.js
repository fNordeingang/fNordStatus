var http        = require('http'),
    url         = require('url'),
    _           = require('underscore'),
    twitter     = require('ntwitter'),
    irc         = require('irc'),
    $           = require('jquery'),
    request     = require('request'),
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
if ( process.argv.length < 6 ) {
  console.log("usage: node status.js [consumer_key] [consumer_secret] [access_token_key] [access_token_secret]");
  process.exit(1);
}

/**
 * initialize twitter with command line arguments
 */
var twit = new twitter({
    consumer_key        : process.argv[2],
    consumer_secret     : process.argv[3],
    access_token_key    : process.argv[4],
    access_token_secret : process.argv[5]
});

var nickname = 'fNot';
var channel = process.argv[6] || "#fnordeingang";

var ircClient = new irc.Client('irc.freenode.net', nickname, {
    channels: [channel]
});


ircClient.addListener('message', onIrcMessage);

function onIrcMessage ( from, to, message ) {
  var command = message.match(/!(\w+)/);
  command = command ? command.pop() : "";

  if ( from !== nickname ) {
    if ( IrcCommand.notifyList()[from] ) {
      ircClient.say(channel, "message from " + IrcCommand.notifyList()[from].from + " for " + from + ": " + IrcCommand.notifyList()[from].message);
      IrcCommand.notifyList()[from] = null;
    }

    if ( _.isFunction(IrcCommand[command]) ) {
      IrcCommand[command](from, to, message);
    }
  }
}

(function (global) {
  global.IrcCommand = (function () {
    var notifyList = {};

    return {
      status: function () {
        if ( lastState ) {
          ircClient.say(channel, "fNordeingang is open! :)");
        }
        else {
          ircClient.say(channel, "fNordeingang is closed :(");
        }
      },

      notify: function ( from, to, message ) {
        message = message.split(" ");

        if ( message.length < 3 ) {
          ircClient.say(channel, "syntax is: !notify <nickname> <the message>")
        }
        else {
          message.shift();
          var nickname = message.shift();
          message = message.join(" ");
          notifyList[nickname] = { from: from, message: message };
          ircClient.say(channel, nickname + " will be notified as soon as possible!");
        }
      },

      wiki: function ( from, to, message ) {
        message = message.split(" ");
        message.shift();
        var query = message.join(" ");

        request("http://wiki.fnordeingang.de/index.php?title=Spezial%3ASuche&search=" + query + "&fulltext=Suchen", function (error, response, body) {
          if (!error && response.statusCode == 200) {
            var $result = $(body);
            if ( $result.find("div.searchresults ul.mw-search-results") ) {
              var $resultLinks = $result.find("div.mw-search-result-heading a");
              var resultLinks = [];
              $resultLinks.each(function () {
                resultLinks.push("http://wiki.fnordeingang.de" + $(this).prop("href"));
              });

              ircClient.say(channel, resultLinks.join("  "));
            }
          }
        });
      },

      addcommand: function ( from, to, message ) {
        message = message.split(" ");
        message.shift();

        try {
          var command = $.parseJSON(message.join(" "));

          if ( _.isObject(command) ) {
            _.each(command, function ( func, name ) {
              var _func = function () {};
              eval("var _func = " + func + ";");
              IrcCommand[name] = _func;
              ircClient.say(channel, "added function: " + name);
            });
          }
        } catch ( err ) {
          ircClient.say(channel, "could not parse json");
          console.log(err);
        }
      },

      notifyList: function () {
        return notifyList;
      }
    }
  })();

})(global);



/**
 * create a listening webserver
 */
http.createServer(onRequest).listen(4242, "0.0.0.0");
console.log('Server running at http://127.0.0.1:4242/');


/**
 * main request handling function
 * @param {object} req the request object
 * @param {object} res the response object
 */
function onRequest ( req, res ) {
  setHeaders(res);
  processRequest(req, res);
}


/**
 * set additional headers
 * @param {object} res the response object
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
 * @param {object} req the request object
 * @param {object} res the response object
 */
function processRequest ( req, res ) {
  if ( req.method === "GET" ) {
    doGet(req, res);
  }
}


/**
 * process GET requests
 * @param {object} req the request object
 * @param {object} res the response object
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
 * @param {object} res the response object
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
 * @param {boolean} status the status to tweet
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
 * @param {string} msg the message to tweet
 */
function tweet (msg) {
  twit
    .verifyCredentials(function (err, data) {
      if (err) console.log(console.dir(err));
    })
    .updateStatus(msg, function (err, data) {
      if (err) console.log(console.dir(err));
    });
}