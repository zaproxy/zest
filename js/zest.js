var zest = require('./zestscript');
var fs = require('fs');
var operations = require('./node-operations');

process.argv.splice(2).forEach(function (val, index, array) {
  fs.readFile(val, 'utf8', function(err, data) {
    if (err) throw err;
    var script = zest.ZestScript.parse(data, operations);
    script.run();
  });
});
