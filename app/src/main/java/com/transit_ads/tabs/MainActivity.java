package com.transit_ads.tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transit_ads.tabs.database.DatabaseHelper;
import com.transit_ads.tabs.database.SharedPreference;
import com.transit_ads.tabs.model.Advertisements;
import com.transit_ads.tabs.utlities.Constants;

import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity {


 //   ProgressBar mProgressBar;

    Thread timer;
    String mainLogo = null;
    String backroundLogo = null;
    String digiTransitLogo =null;
    RelativeLayout backgroundLogoSet;
    ImageView trasnsitAdLogo;

    DatabaseHelper database;

    Typeface face;
    TextView text_enjoy,campaign_with,contact_us,company_phone,company_email;
    SharedPreference sharedPreference;

    TelephonyManager tel;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

//~~~~~~~~~~~~~~~~~~~~~~~setting the custom font for our texts "myriad pro"~~~~~~~~~~~~~~~~~~
        face= Typeface.createFromAsset(this.getAssets(), "fonts/myriad_pro.ttf");

        text_enjoy = (TextView)findViewById(R.id.text_enjoy);
        campaign_with = (TextView)findViewById(R.id.campaign);
        contact_us = (TextView)findViewById(R.id.contact_us);
        company_phone = (TextView)findViewById(R.id.tvPhone);
        company_email = (TextView)findViewById(R.id.company_email);
        backgroundLogoSet = (RelativeLayout)findViewById(R.id.backgroundLayout);
        trasnsitAdLogo = (ImageView)findViewById(R.id.trasnsitAdLogo);
        sharedPreference = new SharedPreference();


        text_enjoy.setTypeface(face);
        campaign_with.setTypeface(face);
        contact_us.setTypeface(face);
        company_phone.setTypeface(face);
        company_email.setTypeface(face);

//~~~~~~~~~~~~~~~~~~~~~~~setting the custom font for our texts "myriad pro" ends~~~~~~~~~~~~~~~~~~

//~~~~~~~~~~~~~~~~~~~~~Fetching device-id(IMIE num)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        sharedPreference.saveImie(this,tel.getDeviceId().toString());
//        Constants.tablet_imie = tel.getDeviceId().toString();
//        Log.d("printing",""+Constants.tablet_imie);
//~~~~~~~~~~~~~~~~~~~~~~~~end of fetching IMIE num ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Image setting ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String path = Environment.getExternalStorageDirectory()+ "/TrasnsitLogos/";

        File imgFile = new File(path+"main_logo.jpg");
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //  ImageView imageView=(ImageView)findViewById(R.id.imageView);
            trasnsitAdLogo.setImageBitmap(myBitmap);
        }
        File backFile = new File(path+"city_bg.jpg");
        if(backFile.exists()){
            Bitmap bmImg = BitmapFactory.decodeFile(backFile.getAbsolutePath());
            BitmapDrawable background = new BitmapDrawable(bmImg);
            backgroundLogoSet.setBackgroundDrawable(background);
        }


//~~~~~~~~~~~~~~~~~~~~~~~~~~~setting up AlL downloads and database creation~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        database = DatabaseHelper.getInstance(this);
        List<Advertisements> ads = database.getAllAdvertisements();
        if(ads.isEmpty()){
            startActivity(new Intent(getApplicationContext(),DownloadActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }else{
            /*~~~~~~~~~~~~~~~~~Thread for to wait and start next activity~~~~~~~~~~~~~~~~~~~~*/
            timer=  new Thread()
            {
                public void run() {
                    try {
                        //Display for 2 seconds
                        sleep(2000);
                    }
                    catch (InterruptedException e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    finally {
                        //Goes to Activity  StartingPoint.java(STARTINGPOINT)
                        startActivity(new Intent(getApplicationContext() , PoweredByPage.class));
                        finish();
                    }
                }
            };

            timer.start();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~Setting up ends ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

      //  mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

     //    preparing list data
     //    new LogosFetchJson().execute("http://www.transit-ads.com/api/v1/application");
     //    mProgressBar.setVisibility(View.INVISIBLE);

    }

    class LogosFetchJson extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {

            //mProgressBar.setVisibility(ProgressBar.VISIBLE);

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
                    String data = EntityUtils.toString(entity);

                    JSONObject jsonObj = new JSONObject(data);
                    JSONObject media = jsonObj.getJSONObject("data");

                    mainLogo = media.get("main_logo").toString();
                    digiTransitLogo  = media.get("parent_company_logo").toString();
                    backroundLogo = media.get("city_background").toString();

//                    Constants.transitLogo =new BitmapDrawable(getResources(), downloadImage(mainLogo)); ;
//                    Constants.digiTransitLogo =new BitmapDrawable(getResources(), downloadImage(digiTransitLogo));
//                    Constants.backgroundImage = new BitmapDrawable(getResources(),downloadImage(backroundLogo));

                    return true;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            if(result == false) {
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            }
            if(mainLogo != null && digiTransitLogo != null && backroundLogo != null){

//                trasnsitAdLogo.setImageDrawable(Constants.transitLogo);
//                backgroundLogoSet.setBackgroundDrawable(Constants.backgroundImage);
                trasnsitAdLogo.setVisibility(View.VISIBLE);

          //      mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                timer.start();
            }
        }
    }


    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    // Creates Bitmap from InputStream and returns it
    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.
                    decodeStream(stream, null, bmOptions);
            stream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

    // Makes HttpURLConnection and returns InputStream
    private InputStream getHttpConnection(String urlString) throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }
}
