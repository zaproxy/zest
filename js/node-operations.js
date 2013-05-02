var http = require('http');
var url = require('url');
var Promise = require('node-promise').Promise;

var request = function(aZestRequest) {
  var zUrl = url.parse(aZestRequest.url);
  var promise = new Promise();
  var response = {};

  var options = {
    hostname: zUrl.host,
    port: zUrl.port,
    path: zUrl.path,
    method: aZestRequest.method
  };

  var req = http.request(options, function(res) {
    response.statusCode = res.statusCode;
    response.headers = res.headers;
    res.setEncoding('utf8');
    response.body = '';
    res.on('data', function (chunk) {
      response.body += chunk;
    });

    res.on('end', function() {
      console.log('ended');
      promise.resolve(response);
    });
  });

  req.on('error', function(e) {
    console.log('problem with request: ' + e.message);
    promise.reject(e);
  });

  // write data to request body
  req.write(aZestRequest.data);
  req.end();
  return promise;
}

exports.request = request;
