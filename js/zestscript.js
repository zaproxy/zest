var log = require('./log');

var ZestConstants = {
  'TYPE_SCRIPT':'ZestScript',
};

var ZestLoader = function(operations) {
  this.operations = operations;
};

ZestLoader.prototype.loadElement = function(obj) {
  if (obj && obj.elementType) {
    var type = ZestStatements[obj.elementType];
    if (!type) {
      throw new Error('This type of element ('+obj.elementType+') is not recognized');
    } else {
      var elem = new type(this);
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
  } else {
    // TODO: replace with a more appropriate error
    throw new Error('this does not look like a Zest element');
  }
};

ZestLoader.prototype.loadElements = function(arr) {
  for(idx in arr) {
    // replace all elements with Zest elements
    arr[idx] = this.loadElement(arr[idx]);
  }
};

var ZestScript = function(loader) {
  this.loader = loader;
};

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

ZestScript.parse = function(src, operations) {
  if (src) {
    var obj = JSON.parse(src);
    var loader = new ZestLoader(operations);
    if (obj && obj.elementType && obj.elementType == ZestConstants.TYPE_SCRIPT) {
      obj = loader.loadElement(obj);
      return obj;
    }
  }
  // TODO: replace with a more appropriate error
  throw new Error('this does not look like a zest script');
}

ZestScript.prototype.load = function(){
  if (this.statements) {
    this.loader.loadElements(this.statements);
  } else {
    // TODO: replace with a more appropriate error
    throw new Error('this script has no statements');
  }
};


// TODO: Implement ZestRequest
var ZestRequest = function (loader){
  this.loader = loader;
};

ZestRequest.prototype.load = function(){
  if (this.transformations) {
    this.loader.loadElements(this.transformations);
  }
  if (this.assertions) {
    this.loader.loadElements(this.assertions);
  }
  if (this.response) {
    this.reponse = this.loader.loadElement(this.response);
  }
};

ZestRequest.prototype.run = function() {
  this.loader.operations.request(this).then(function(response){
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
var ZestResponse = function(loader){
  this.loader = loader;
};

// TODO: Implement ZestConditionRegex
var ZestConditionRegex = function(loader){
  this.loader = loader;
};

ZestConditionRegex.prototype.load = function(){
  if (this.ifStatements) {
    this.loader.loadElements(this.ifStatements);
  }
  if (this.elseStatements) {
    this.loader.loadElements(this.elseStatements);
  }
};

// TODO: Implement ZestAssertStatusCode
var ZestAssertStatusCode = function(loader){
};

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

var ZestStatements = {
  'ZestScript' : ZestScript,
  'ZestAssertLength' : ZestAssertLength,
  'ZestTransformRndIntReplace' : ZestTransformRndIntReplace,
  'ZestAssertStatusCode' : ZestAssertStatusCode,
  'ZestRequest' : ZestRequest,
  'ZestResponse' : ZestResponse,
  'ZestConditionRegex' : ZestConditionRegex,
  'ZestActionFail' : ZestActionFail,
  'ZestActionScan' : ZestActionScan,
};

exports.ZestConstants = ZestConstants;
exports.ZestScript = ZestScript;
exports.ZestResponse = ZestResponse;
