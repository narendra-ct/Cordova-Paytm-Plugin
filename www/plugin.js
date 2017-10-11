
var exec = require('cordova/exec');

var PLUGIN_NAME = 'PaytmPlugin';

var PaytmPlugin = {
   //orderid, cust_id, email, phone, txn_amt,callback_url,checksum_hash,environment
  payWithPaytm: function(orderId, customerId, email, phone, amount,callbackUrl,checksum, method, successCallback, failureCallback) {
    exec(successCallback,
                 failureCallback,
                 PLUGIN_NAME,
                 "payWithPaytm",
                 [orderId, customerId, email, phone, amount, callbackUrl, checksum, method]);
  }




};

module.exports = PaytmPlugin;
