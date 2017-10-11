
var exec = require('cordova/exec');

var PLUGIN_NAME = 'PaytmPlugin';

var PaytmPlugin = {
  echo: function(phrase, cb) {
    exec(cb, null, PLUGIN_NAME, 'echo', [phrase]);
  },
  getDate: function(cb) {
    exec(cb, null, PLUGIN_NAME, 'getDate', []);
  },

   //orderid, cust_id, email, phone, txn_amt,callback_url,checksum_hash,environment
  payWithPaytm: function(orderId, customerId, email, phone, amount,callbackUrl,checksum, method, successCallback, failureCallback) {
    exec(successCallback,
                 failureCallback, 
                 "PLUGIN_NAME",
                 "payWithPaytm",
                 [orderId, customerId, email, phone, amount, callbackUrl, checksum, method]);
  }




};

module.exports = PaytmPlugin;
