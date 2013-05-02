var zest = require('./zestscript');
var Promise = require('node-promise').Promise;

// runs a targeted Zest script against its own response properties

var Tester = function() {
};

Tester.prototype.parseHeaders = function(headers) {
  var parsed = {};
  var lines = headers.split('\r\n');
  for (idx in lines) {
    var line = lines[idx];
    var arr = line.split(':');
    if (arr.length > 1) {
      parsed[arr[0].trim().toLowerCase()] = arr[1].trim();
    }
  }
  console.log(JSON.stringify(parsed));
  return parsed;
}

Tester.prototype.request = function(aZestRequest) {
    var promise = new Promise();
    var response = aZestRequest.response;
    response.headers = this.parseHeaders(response.headers);
    promise.resolve(response);
    return promise;
  };

var fs = require('fs');

process.argv.splice(2).forEach(function (val, index, array) {
  fs.readFile(val, 'utf8', function(err, data) {
    if (err) throw err;
    var script = zest.ZestScript.parse(data, new Tester());
    script.run();
  });
});
