Cordova Plugin <PAYTM PLGIN For PAYMENTS>
======

This is a simple component building a Cordova plugin on paytm integration with iOS and Android applications developed by Cordova/IONIC

How To Use:

1. Intially Read the paytm documentaion for better understanding at : http://paywithpaytm.com/developer/paytm_sdk_doc/

2. Follow the link to generate checksum URL. https://github.com/Paytm-Payments/Paytm_Sample_Checksum_Utilities/tree/master/Php

3. After Generation of checksum API setup, generate the checsum by making POST request to the checksumGeneration API. (Make sure your generating checsum with valid parameters). You can find the required parameters at https://github.com/Paytm-Payments/Paytm_Sample_Checksum_Utilities/blob/master/Php/generateChecksum.php

4. After getting the checksum hash add the plugin to your currnet project by:

cordova plugin add https://github.com/narendra-ct/Cordova-Paytm-Plugin.git --variable MERCHANT_ID=xxxxxxxxx --variable INDUSTRY_TYPE_ID=xxxxxx --variable CHANNEL_ID=xxx --variable WEBSITE=xxxxx

[update xxxxx with actual values (Provided by Paytm)]

5. After Adding the plugin you can make payments by calling following method, make sure you are pssing required parameters in order,

//orderid, cust_id, email, phone, txn_amt,callback_url,checksum_hash,environment
PaytmPlugin.payWithPaytm('ORDS112', 'CUST001', 'abc@gmail.com', '7777777777', '1', 'https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp', 'uNueM+eqAbZgWLD6aKR6rCk/asvPYuwZjY3u3FgpQw8BFJg/qGmkOeMoHM9Kx5BECdukmtGZ0Iz/tcaCESTe41Zj6VO3k5LGUii=', 'staging', function (success) {
alert(success);
}, function (failure) {
alert(failure);
});

6. In case of production update the environment variale to "production".






