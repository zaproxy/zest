// At the moment we just assume we're running in nodejs. Obviously this will
// change when we start thinking about an FX impl.
var operations = require('./node-operations');

var log = require('./log');

var ZestConstants = {
  'TYPE_SCRIPT':'ZestScript',
}

function createZestElement(type, obj) {
  var elem = new type();
  for (prop in obj) {
    elem[prop] = obj[prop];
  }
  if (elem.load) {
    elem.load();
  } else {
    log.warn(elem.elementType+' does not support loading');
  }
  return elem;
}

function loadElement(obj) {
  if (obj && obj.elementType) {
    switch (obj.elementType) {
    case 'ZestAssertLength':
      return createZestElement(ZestAssertLength, obj);
    case 'ZestTransformRndIntReplace':
      return createZestElement(ZestTransformRndIntReplace, obj);
    case 'ZestAssertStatusCode':
      return createZestElement(ZestAssertStatusCode, obj);
    case 'ZestRequest':
      return createZestElement(ZestRequest, obj);
    case 'ZestResponse':
      return createZestElement(ZestResponse, obj);
    case 'ZestConditionRegex':
      return createZestElement(ZestConditionRegex, obj);
    case 'ZestActionFail':
      return createZestElement(ZestActionFail, obj);
    case 'ZestActionScan':
      return createZestElement(ZestActionScan, obj);
    default:
      // TODO: replace with a more appropriate error
      throw new Error('This type of element ('+obj.elementType+') is not recognized');
    }
  } else {
    // TODO: replace with a more appropriate error
    throw new Error('this does not look like a Zest element');
  }
}

function loadElements(arr) {
  for(idx in arr) {
    // replace all elements with Zest elements
    arr[idx] = loadElement(arr[idx]);
  }
}

var ZestScript = function() {};

ZestScript.prototype.run = function() {
  for(idx in this.statements) {
    var statement = this.statements[idx];
    if (statement.run) {
      statement.run();
    } else {
      log.warn('The script contains a statement that will not run');
    }
  }
};

ZestScript.parse = function(src) {
  if (src) {
    var obj = JSON.parse(src);
    if (obj && obj.elementType && obj.elementType == ZestConstants.TYPE_SCRIPT) {
      obj.__proto__ = ZestScript.prototype;
      if (obj.statements) {
        loadElements(obj.statements);
      } else {
        // TODO: replace with a more appropriate error
        throw new Error('this script has no statements');
      }
      return obj;
    }
  }
  // TODO: replace with a more appropriate error
  throw new Error('this does not look like a zest script');
}

// TODO: Implement ZestRequest
var ZestRequest = function (){};

ZestRequest.prototype.load = function(){
  if (this.transformations) {
    loadElements(this.transformations);
  }
  if (this.assertions) {
    loadElements(this.assertions);
    this.assertionLookup = {};
    for (idx in this.assertions) {
      var assertion = this.assertions[idx];
      var type = assertion.elementType;
      if (this.assertionLookup[type]) {
        this.assertionLookup[type].push(assertion);
      } else {
        this.assertionLookup[type] = [assertion];
      }
    }
  }
  if (this.response) {
    this.reponse = loadElement(this.response);
  }
};

ZestRequest.prototype.run = function() {
  operations.request(this).then(function(response){
    for(idx in this.assertions) {
      var assertion = this.assertions[idx];
      if (assertion.assert) {
        assertion.assert(response);
      }
    }
  }.bind(this),
  function(error){
    console.log('something went wrong');
  });
}

// TODO: Implement ZestResponse
var ZestResponse = function(){};

// TODO: Implement ZestConditionRegex
var ZestConditionRegex = function(){};

ZestConditionRegex.prototype.load = function(){
  if (this.ifStatements) {
    loadElements(this.ifStatements);
  }
  if (this.elseStatements) {
    loadElements(this.elseStatements);
  }
};

// TODO: Implement ZestAssertStatusCode
var ZestAssertStatusCode = function(){};

ZestAssertStatusCode.prototype.load = function(){};
// TODO: maybe have some kind of assertion base so we can unify reporting
ZestAssertStatusCode.prototype.assert = function(response){
  var value = response.statusCode;
  log.debug('assert: '+value+' == '+this.code+' ?');
  if(this.code == value) {
    log.debug('StatusCode assertion passed');
    return true;
  }
  log.warn('assertion failed');
  return false;
};

// TODO: Implement ZestAssertLength
var ZestAssertLength = function(){};

ZestAssertLength.prototype.load = function(){};
ZestAssertLength.prototype.assert = function(response){
  var value = parseInt(response.headers['content-length']);
  log.debug('assert: '+value+' == '+this.length+' ?');
  if(this.length == value) {
    log.debug('Length assertion passed');
    return true;
  }
  log.warn('assertion failed');
  return false;
};


// TODO: Implement ZestTransformRndIntReplace
var ZestTransformRndIntReplace = function(){};

// TODO: Implement ZestActionFail
var ZestActionFail = function(){};

// TODO: Implement ZestActionScan
var ZestActionScan = function(){};

exports.ZestConstants = ZestConstants;
exports.ZestScript = ZestScript;
exports.ZestResponse = ZestResponse;
