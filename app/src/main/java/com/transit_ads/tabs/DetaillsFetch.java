package com.transit_ads.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transit_ads.tabs.database.DatabaseHelper;
import com.transit_ads.tabs.database.SharedPreference;
import com.transit_ads.tabs.model.UserDetails;
import com.transit_ads.tabs.utlities.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class DetaillsFetch extends Activity implements View.OnClickListener {

    ProgressDialog pd;

    EditText nameTv, phoneTv;
    Button submit, skip;

    TextView terms_conditions;
    RelativeLayout tc_layout;

    RelativeLayout backgroundLayout;
    ImageView transitLogo;
    TextView details_txt_one , details_txt_two;
    Typeface face;
    boolean tc_layout_flag = false;
    public static boolean submitted = false;
    SharedPreference sharedPreference;

    DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detaills_fetch);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
//~~~~~~~~~~~~~~~~~~~~~~font apply for  Textview~~~~~~~~~~~~~~~~~~~~~~~~~~~
        details_txt_one =(TextView)findViewById(R.id.details_one);
        details_txt_two =(TextView)findViewById(R.id.details_two);
        backgroundLayout = (RelativeLayout) findViewById(R.id.backgroundLayout);
        transitLogo = (ImageView) findViewById(R.id.transitLogo);
        nameTv = (EditText) findViewById(R.id.tvName);
        phoneTv = (EditText) findViewById(R.id.tvPhone);
        submit = (Button) findViewById(R.id.submit_btn);
        skip = (Button) findViewById(R.id.skip_btn);
        terms_conditions = (TextView)findViewById(R.id.terms_conditions);
        tc_layout = (RelativeLayout)findViewById(R.id.tc_layout);

        face= Typeface.createFromAsset(this.getAssets(), "fonts/myriad_pro.ttf");
        sharedPreference = new SharedPreference();

        nameTv.setTypeface(face);
        phoneTv.setTypeface(face);
//        details_txt_one.setTypeface(face);
//        details_txt_two.setTypeface(face);
        submit.setTypeface(face);
        skip.setTypeface(face);
        terms_conditions.setTypeface(face);
        submit.setOnClickListener(this);
        skip.setOnClickListener(this);
        terms_conditions.setOnClickListener(this);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Image setting ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String path = Environment.getExternalStorageDirectory()+ "/TrasnsitLogos/";

        File imgFile = new File(path+"main_logo.png");
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //  ImageView imageView=(ImageView)findViewById(R.id.imageView);
            transitLogo.setImageBitmap(myBitmap);
        }
        File backFile = new File(path+"city_bg.jpg");
        if(backFile.exists()){
            Bitmap bmImg = BitmapFactory.decodeFile(backFile.getAbsolutePath());
            BitmapDrawable background = new BitmapDrawable(bmImg);
            backgroundLayout.setBackgroundDrawable(background);
        }

    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Image setting ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submit_btn:
                String name = nameTv.getText().toString();
                String phoneNum = phoneTv.getText().toString();
                if (name.equals("") && name.equals("")) {
                    showAlert("Transit-ads Warning ..!","please Enter Name & Phone number","OK","CANCEL");
                } else if (name.equals("")) {
                    showAlert("Transit-ads Warning ..!","please Enter Your Name","OK","CANCEL");
                } else if (phoneNum.equals("")) {
                    showAlert("Transit-ads Warning ..!","please Enter Phone number","OK","CANCEL");
                } else {
                    if(phoneNum.length() < 10){
                        showAlert("Transit-ads Warning ..!","please Enter 10 digit phone number","OK","CANCEL");
                    }else {

                        database = DatabaseHelper.getInstance(this);
                        UserDetails todo1 = new UserDetails(name,phoneNum,"No",null);
                        long todo_id = database.createUserDetails(todo1);
                        UserDetails entered_User = database.getSingleUserLastRecord();
                        Constants.user_id = entered_User.getId();

                        sharedPreference.saveUserName_phone(this,entered_User.getId(),name,phoneNum);
                        submitted = true;

                            Constants.user_name = name;
                            if (name.length() >= 11) {
                                Constants.user_name = "Hi "+name.substring(0, 9);
                            } else {
                                Constants.user_name = "Hi "+name;
                            }
                            Constants.user_phone = phoneNum;

                          //  new OTPFetchJson().execute("http://www.transit-ads.com/api/v1/sendOTP?username=%22"+name+"%22&mobilenumber="+phoneNum);

                            startActivity(new Intent(getApplicationContext(), OtpPromoCode.class));
                            finish();
                    }
                }
                break;

            case R.id.skip_btn:
                startActivity(new Intent(getApplicationContext(), HomePage.class));
                finish();
                break;
            case R.id.terms_conditions:
//                if(tc_layout_flag){
//                    tc_layout.setVisibility(View.INVISIBLE);
//                    tc_layout_flag = false;
//                }else {
//                    tc_layout.setVisibility(View.VISIBLE);
//                    tc_layout_flag = true;
//                }
                terms_and_conditions();
                break;
        }

    }

    private void showAlert(String heading_txt,String message_txt,String btn_yes_txt,String btn_no_txt) {
        final Dialog dialog1 = new Dialog(DetaillsFetch.this);
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

    private void moveToOtp(){

        startActivity(new Intent(getApplicationContext(), OtpPromoCode.class));
        finish();

    }
    class OTPFetchJson extends AsyncTask<String, Void, Boolean> {

        String data,result_json;
        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(DetaillsFetch.this);
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
            pd.dismiss();

            if(result == false) {
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Success" , Toast.LENGTH_SHORT).show();
                if(result_json.equals("Successfully Sent OTP")){
                    moveToOtp();
                }else if(result_json.equals("OTP Not Sent")){
                   // showAlert("Correct OTP");
                    Toast.makeText(getApplicationContext(), "Something wrong while sending", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void terms_and_conditions() {
        final Dialog dialog1 = new Dialog(DetaillsFetch.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.terms_and_conditions);

        Button agree = (Button) dialog1.findViewById(R.id.agree_button);

        agree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog1.dismiss();

            }
        });
        dialog1.show();
    }
}