ERROR = 1;
WARN = ERROR << 1;
DEBUG = WARN << 1;

LEVEL = ERROR | WARN | DEBUG;

function debug(message) {
  if(LEVEL & DEBUG) {
    console.log('debug: '+message);
  }
}

function warn(message) {
  if(LEVEL & WARN) {
    console.log('warning: '+message);
  }
}

function error(message) {
  if(LEVEL & ERROR) {
    console.log('error: '+message);
  }
}

exports.debug = debug;
exports.warn = warn;
exports.error = error;
