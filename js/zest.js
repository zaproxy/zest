zest = require('./zestscript');
fs = require('fs');

process.argv.splice(2).forEach(function (val, index, array) {
  fs.readFile(val, 'utf8', function(err, data) {
    if (err) throw err;
    var script = zest.ZestScript.parse(data);
    script.run();
  });
});
