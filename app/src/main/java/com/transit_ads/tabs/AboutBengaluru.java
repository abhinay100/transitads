package com.transit_ads.tabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.transit_ads.tabs.database.DatabaseHelper;
import com.transit_ads.tabs.model.Advertisements;
import com.transit_ads.tabs.model.Feedback;
import com.transit_ads.tabs.model.Passenger;
import com.transit_ads.tabs.model.PassengerHasFeedback;
import com.transit_ads.tabs.utlities.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.attr.uiOptions;

public class AboutBengaluru extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Button homeButton;
    TextView  display_Date;
    TextView area_name;
    TextView dispUserName;
    ImageView user_icon;
    TextView imie_num;
    TextView details_one , details_two ,abt_bng_head;
    TextClock dispTime;
    Typeface face;
    String areaLocation;
    ImageView transit_main_logo;

    protected Context context;

    // Database Helper
    DatabaseHelper db;

    //location purpose
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_bengaluru);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~Location name display~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        buildGoogleApiClient();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~Location name display ends~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        face= Typeface.createFromAsset(this.getAssets(), "fonts/myriad_pro.ttf");
//~~~~~~~~~~~~~~~~~abt banglore details displaytext font "myriad pro"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        details_one = (TextView)findViewById(R.id.details_one);
        details_two = (TextView)findViewById(R.id.details_two);
        abt_bng_head = (TextView)findViewById(R.id.abt_bng_head);
        dispTime = (TextClock) findViewById(R.id.dispTime);
        imie_num = (TextView) findViewById(R.id.imie_num);
        dispUserName = (TextView)findViewById(R.id.dispUserName);
        display_Date = (TextView)findViewById(R.id.dispDate);
        homeButton = (Button)findViewById(R.id.homeBtn);
        transit_main_logo = (ImageView)findViewById(R.id.transit_main_logo);

        user_icon = (ImageView)findViewById(R.id.user_icon);
        area_name = (TextView) findViewById(R.id.dispLocation);

        abt_bng_head.setTypeface(face);
        details_one.setTypeface(face);
        details_two.setTypeface(face);
        dispTime.setTypeface(face);
        imie_num.setTypeface(face);
        area_name.setTypeface(face);
        dispUserName.setTypeface(face);
        display_Date.setTypeface(face);
        homeButton.setTypeface(face);
//~~~~~~~~~~~~~~~~~abt banglore details displaytext font "myriad pro" ends~~~~~~~~~~~~~~~~~~~~~~~~~

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Image setting ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String path = Environment.getExternalStorageDirectory()+ "/TrasnsitLogos/";

        File imgFile = new File(path+"trasnit_ads_logo.png");
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            transit_main_logo.setImageBitmap(myBitmap);
        }

//~~~~~~~~~~~~~~~~user name~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     //   String str = Constants.user_name;

        if(Constants.user_name == null){

            user_icon.setVisibility(View.INVISIBLE);
            dispUserName.setText("            ");
        }
        dispUserName.setText(Constants.user_name);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                startActivity(new Intent(getApplicationContext() ,HomePage.class));

            }
        });

//~~~~~~~~~~~~~~~~date dispaly~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        int yy = c.get(Calendar.YEAR);
//        int mm = c.get(Calendar.MONTH);
        String month_name = month_date.format(c.getTime());
        int dd = c.get(Calendar.DAY_OF_MONTH);

        display_Date.setText(new StringBuilder().append(dd).append("-").append(month_name).append("-").append(yy));
                // Month is 0 based, just add 1


//~~~~~~~~~~~~~~~~Database Activities ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //    database_checking();

//~~~~~~~~~~~~~~~~Database Activities ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(getApplicationContext()," OnConnected called",Toast.LENGTH_SHORT).show();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());

            Constants.latitude=lat;
            Constants.longitude =lon;

        }
        updateUI();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onLocationChanged(Location location) {
     //   Toast.makeText(getApplicationContext()," location changed",Toast.LENGTH_SHORT).show();
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        updateUI();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    void updateUI() {

        String api_url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lon+"&key=AIzaSyDtxiTFONhso3ptncV2DgMTdl5zHlcmf2g";
        new AddressFetch().execute(api_url);
    }


    class AddressFetch extends AsyncTask<String, Void, Boolean> {
        String area;

        @Override
        protected void onPreExecute() {

            //  mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                HttpGet httpget = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpget);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsonObj = new JSONObject(data);
                    JSONArray results = jsonObj.getJSONArray("results");
                    JSONObject obj = results.getJSONObject(0);

                    String fullAddress = obj.getString("formatted_address");
                    List<String> splitedAddress = Arrays.asList(fullAddress.split(","));
                    area = splitedAddress.get(splitedAddress.size()-4);

                    Constants.areaName = area;

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
            //    Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
                area_name.setText("Bangalore");
            }else if(result == true){
                if(Constants.areaName.length()>15){
                    areaLocation = Constants.user_name.substring(0,15);
                    area_name.setTypeface(face);
                    area_name.setText(areaLocation);
                 }else {
                    area_name.setTypeface(face);
                    area_name.setText(Constants.areaName);
                 }
//                area_name.setText(Constants.areaName);
            }
        }
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}

