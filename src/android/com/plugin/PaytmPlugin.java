/**
 */
package com.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;


public class PaytmPlugin extends CordovaPlugin {
  private static final String TAG = "PaytmPlugin";

  private PaytmPGService Service;

  private static final String STAGIN = "staging";
  private static final String PRODUCTION = "production";
  private static final String PAYTM_PAYMENT = "payWithPaytm";





//  private String PAYTM_GENERATE_URL;
//  private String PAYTM_VALIDATE_URL;

  private String PAYTM_MERCHANT_ID;
  private String PAYTM_INDUSTRY_TYPE_ID;
  private String PAYTM_CHANNEL_ID;
  private String PAYTM_WEBSITE;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Log.d(TAG, "Initializing PaytmPlugin");

    int appResId;
//    int appResId = cordova.getActivity().getResources().getIdentifier("paytm_gen_url", "string", cordova.getActivity().getPackageName());
//    PAYTM_GENERATE_URL = cordova.getActivity().getString(appResId);

//    appResId = cordova.getActivity().getResources().getIdentifier("paytm_chk_url", "string", cordova.getActivity().getPackageName());
//    PAYTM_VALIDATE_URL = cordova.getActivity().getString(appResId);

    appResId = cordova.getActivity().getResources().getIdentifier("paytm_merchant_id", "string", cordova.getActivity().getPackageName());
    PAYTM_MERCHANT_ID = cordova.getActivity().getString(appResId);
    appResId = cordova.getActivity().getResources().getIdentifier("paytm_industry_type_id", "string", cordova.getActivity().getPackageName());
    PAYTM_INDUSTRY_TYPE_ID = cordova.getActivity().getString(appResId);
    appResId = cordova.getActivity().getResources().getIdentifier("paytm_website", "string", cordova.getActivity().getPackageName());
    PAYTM_WEBSITE = cordova.getActivity().getString(appResId);
    appResId = cordova.getActivity().getResources().getIdentifier("paytm_channel_id", "string", cordova.getActivity().getPackageName());
    PAYTM_CHANNEL_ID = cordova.getActivity().getString(appResId);

  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if(action.equals(PAYTM_PAYMENT)) {

      //orderid, cust_id, email, phone, txn_amt,callback_url,checksum_hash,environment
      startPayment(args.getString(0), args.getString(1), args.getString(2), args.getString(3), args.getString(4), args.getString(5),args.getString(6),args.getString(7), callbackContext);
      return true;

    }

    return true;
  }


  private void startPayment(final String order_id,
                            final String cust_id,
                            final String email,
                            final String phone,
                            final String txn_amt,
                            final String callback_url,
                            final String checksum_hash,
                            final String environment,
                            final CallbackContext callbackContext) {


    if(environment.equals(STAGIN)){
      Service = PaytmPGService.getStagingService();
    }else if(environment.equals(PRODUCTION)){
      Service = PaytmPGService.getProductionService();
    }else{
      callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Please valid environment (staging or production)"));
      return;
    }



    PaytmPGService Service = PaytmPGService.getStagingService();

    //Kindly create complete Map and checksum on your server side and then put it here in paramMap.
    Map<java.lang.String, java.lang.String> paramMap = new HashMap<java.lang.String, java.lang.String>();
    paramMap.put("MID" , PAYTM_MERCHANT_ID);
    paramMap.put("ORDER_ID" , order_id);
    paramMap.put("CUST_ID" , cust_id);
    paramMap.put("INDUSTRY_TYPE_ID" , PAYTM_INDUSTRY_TYPE_ID);
    paramMap.put("CHANNEL_ID" , PAYTM_CHANNEL_ID);
    paramMap.put("TXN_AMOUNT" , txn_amt);
    paramMap.put("WEBSITE" , PAYTM_WEBSITE);
    paramMap.put("CALLBACK_URL" , callback_url);
    paramMap.put("EMAIL" , email);
    paramMap.put("MOBILE_NO" , phone);
    paramMap.put("CHECKSUMHASH" ,  checksum_hash); // "XtpRIsmhzV0KAKOAW2fj6tmk4HgIYNwgtqKKohP6nyW/llbnH7Kj5ZB9UOLiH7MTlDoC1ZW2aNtl/cyt8PUjrZPDJsjdiWeVXLsqxJM0Q4Q=");

    PaytmOrder Order = new PaytmOrder(paramMap);

    Service.initialize(Order, null);

    Service.startPaymentTransaction(webView.getContext(), true, true,
      new PaytmPaymentTransactionCallback() {

        @Override
        public void someUIErrorOccurred(java.lang.String inErrorMessage) {
          // Some UI Error Occurred in Payment Gateway Activity.
          // // This may be due to initialization of views in
          // Payment Gateway Activity or may be due to //
          // initialization of webview. // Error Message details
          // the error occurred.
          Log.i("Error","onTransactionFailure :"+inErrorMessage);
          callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, inErrorMessage));

        }

        @Override
        public void onTransactionResponse(Bundle inResponse) {
          Log.d("LOG", "Payment Transaction : " + inResponse);
          callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, inResponse.toString()));
//          Toast.makeText(getApplicationContext(), "Payment Transaction response "+inResponse.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void networkNotAvailable() {
          // If network is not
          // available, then this
          // method gets called.
          Log.i("Error","networkNotAvailable");
          callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Network is not available, please check your network and try again"));
        }

        @Override
        public void clientAuthenticationFailed(java.lang.String inErrorMessage) {
          // This method gets called if client authentication
          // failed. // Failure may be due to following reasons //
          // 1. Server error or downtime. // 2. Server unable to
          // generate checksum or checksum response is not in
          // proper format. // 3. Server failed to authenticate
          // that client. That is value of payt_STATUS is 2. //
          // Error Message describes the reason for failure.
          Log.i("Error","clientAuthenticationFailed :"+inErrorMessage);
          callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, inErrorMessage));
        }

        @Override
        public void onErrorLoadingWebPage(int iniErrorCode,
                                          java.lang.String inErrorMessage, java.lang.String inFailingUrl) {

          Log.i("Error","onErrorLoadingWebPage :"+inErrorMessage);
          callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, inErrorMessage));

        }

        // had to be added: NOTE
        @Override
        public void onBackPressedCancelTransaction() {
          // TODO Auto-generated method stub
          Log.i("Error","BackPressedCancelTransaction");
          callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Transaction cancelled by user"));
        }

        @Override
        public void onTransactionCancel(java.lang.String inErrorMessage, Bundle inResponse) {
          Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
          callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, inErrorMessage));
          //Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
        }

      });



  }


}
