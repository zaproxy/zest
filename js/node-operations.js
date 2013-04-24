var http = require('http');
var url = require('url');

var request = function(aZestRequest) {
  var zUrl = url.parse(aZestRequest.url);

  var options = {
    hostname: zUrl.host,
    port: zUrl.port,
    path: zUrl.path,
    method: aZestRequest.method
  };

  var req = http.request(options, function(res) {
    var scAssertions = aZestRequest.assertionLookup['ZestAssertStatusCode'];
    if(scAssertions) {
      for (idx in scAssertions) {
        scAssertions[idx].assert(res.statusCode);
      }
    }
    //console.log('HEADERS: ' + JSON.stringify(res.headers));
    res.setEncoding('utf8');
    res.on('data', function (chunk) {
      //console.log('BODY: ' + chunk);
    });
  });

  req.on('error', function(e) {
    console.log('problem with request: ' + e.message);
  });

  // write data to request body
  req.write(aZestRequest.data);
  req.end();
}

exports.request = request;
