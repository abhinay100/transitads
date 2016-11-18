package com.transit_ads.tabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transit_ads.tabs.utlities.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class OtpPromoCode extends AppCompatActivity {

    Button accessBtn, skip_for_home;
    EditText otpText, promoText;
    TextView user_name, two_digit_num, txt_one, txt_two, hello_txt;
    Typeface face;

    RelativeLayout backgroundLayout;
    ImageView transitLogo;
    String otp;
    ProgressDialog pd;

    boolean wasAPEnabled = false;
    static WifiAP wifiAp;
    private WifiManager wifi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp_promo_code);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
//~~~~~~~~~~~~~~~~~~~~~~~setting the custom font for our texts "myriad pro"~~~~~~~~~~~~~~~~~~
        face = Typeface.createFromAsset(this.getAssets(), "fonts/myriad_pro.ttf");

        two_digit_num = (TextView) findViewById(R.id.two_digit_num);
        txt_one = (TextView) findViewById(R.id.txt_one);
        txt_two = (TextView) findViewById(R.id.txt_two);
        hello_txt = (TextView) findViewById(R.id.hello_txt);
        user_name = (TextView) findViewById(R.id.user_name_txt);
        otpText = (EditText) findViewById(R.id.otpText);
        promoText = (EditText) findViewById(R.id.promoText);
        accessBtn = (Button) findViewById(R.id.access_btn);
        skip_for_home = (Button) findViewById(R.id.skip_for_home);

        backgroundLayout = (RelativeLayout) findViewById(R.id.backgroundLayout);
        transitLogo = (ImageView) findViewById(R.id.transitLogo);

        two_digit_num.setTypeface(face);
        txt_one.setTypeface(face);
        txt_two.setTypeface(face);
        hello_txt.setTypeface(face);
        user_name.setTypeface(face);
        otpText.setTypeface(face);
        promoText.setTypeface(face);
        accessBtn.setTypeface(face);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Image setting ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String path = Environment.getExternalStorageDirectory()+ "/TrasnsitLogos/";

        File imgFile = new File(path+"trasnit_ads_logo.png");
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            transitLogo.setImageBitmap(myBitmap);
        }
        File backFile = new File(path+"backgroundimge.png");
        if(backFile.exists()){
            Bitmap bmImg = BitmapFactory.decodeFile(backFile.getAbsolutePath());
            BitmapDrawable background = new BitmapDrawable(bmImg);
            backgroundLayout.setBackgroundDrawable(background);
        }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Image setting ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


//~~~~~~~~~~~~~~~~WIFI~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        wifiAp = new WifiAP();
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//        moveToHomePage();
//~~~~~~~~~~~~~~~~~~~~~~~setting the custom font for our texts "myriad pro" ends~~~~~~~~~~~~~~~~~~~~

        if (DetaillsFetch.submitted == false) {
            //  Toast.makeText(getApplicationContext() , "user not entered his details" , Toast.LENGTH_SHORT).show();
            user_name.setText("John Doe,");
        } else {
            user_name.setText(Constants.user_name + ",");
            two_digit_num.setText("XXXXXXX" + Constants.user_phone.substring(7, 10));

        }

//        backgroundLayout.setBackgroundDrawable(Constants.backgroundImage);
//        transitLogo.setImageDrawable(Constants.transitLogo);

        accessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = Constants.otp = otpText.getText().toString();

                if (otp.equals("")) {
                    showAlert("Transit-ads Warning ..!","Enter OTP To Continue","OK","CANCEL");
                } else {
                  //new OTPVerification().execute("http://www.transit-ads.com/api/v1/verifyOTP?username=%22" + Constants.user_name + "%22&mobilenumber=" + Constants.user_phone + "&otp=" + otp);

                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                    finish();
                }

                String promo_code = promoText.getText().toString();

//                if(otp.equals("") && promo_code.equals("")){
//                    showAlert("OTP or PromoCode");
//
//                }else if(!otp.equals("") && !promo_code.equals("")) {
//                    showAlert("Any one");
//                }else if(otp.equals("") && promo_code.equals("12345")){
////                    moveToHomePage();
//
//                }else if(!otp.equals("") && promo_code.equals("")){
//                   // entered OTP route to Home page
//                    new OTPVerification().execute("http://www.transit-ads.com/api/v1/verifyOTP?mobilenumber="+Constants.user_phone+"&amp;otp="+otp);
//
//                }else{
//                    showAlert(" Correct OTP/PromoCode");
//                }
            }
        });

        skip_for_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), HomePage.class));
                finish();
            }
        });


    }

    class OTPVerification extends AsyncTask<String, Void, Boolean> {

        String data;
        String result_json;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(OtpPromoCode.this);
            pd.setMessage("loading");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    data = EntityUtils.toString(entity);
                    JSONObject jsonObj = new JSONObject(data);
                    result_json = jsonObj.get("result").toString();
                    return true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result == false) {

                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

            } else {

                pd.dismiss();

                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();

                if (result_json.equals("Successfully Verified")) {
                    moveToHomePage();
                } else if (result_json.equals("Invalid OTP")) {
                    showAlert("Transit-ads Warning ..!","please Enter Correct OTP","OK","CANCEL");
                }
            }
        }
    }

    private void moveToHomePage() {
        delayOftwentyMin();

        // enabling wifi tethering service
        wifiAp.toggleWiFiAP(wifi, OtpPromoCode.this);

        startActivity(new Intent(getApplicationContext(), HomePage.class));
        finish();

    }

    private void delayOftwentyMin() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //tethering will disable if enabled
                boolean wifiApIsOn = wifiAp.getWifiAPState() == wifiAp.WIFI_AP_STATE_ENABLED || wifiAp.getWifiAPState() == wifiAp.WIFI_AP_STATE_ENABLING;
                if (wifiApIsOn) {
                    wasAPEnabled = false;
                    wifiAp.toggleWiFiAP(wifi, OtpPromoCode.this);
                }
            }
        }, 2 * 60 * 1000);
    }

    private void showAlert(String heading_txt,String message_txt,String btn_yes_txt,String btn_no_txt) {
        final Dialog dialog1 = new Dialog(OtpPromoCode.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.customised_warning_dailog);
        TextView heading = (TextView)dialog1.findViewById(R.id.title_heading);
        TextView message = (TextView)dialog1.findViewById(R.id.message);
        Button yes = (Button) dialog1.findViewById(R.id.btn_submit);
        Button no = (Button) dialog1.findViewById(R.id.btn_cancel);

        heading.setText(heading_txt);
        message.setText(message_txt);
        yes.setText(btn_yes_txt);
        no.setText(btn_no_txt);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();

            }
        });
        dialog1.show();
    }
}
