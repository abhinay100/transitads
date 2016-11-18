package com.transit_ads.tabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.transit_ads.tabs.database.DatabaseHelper;
import com.transit_ads.tabs.database.SharedPreference;
import com.transit_ads.tabs.model.Advertisements;
import com.transit_ads.tabs.model.Categories;
import com.transit_ads.tabs.model.Passenger;
import com.transit_ads.tabs.model.UserDetails;
import com.transit_ads.tabs.utlities.Constants;
import com.transit_ads.tabs.utlities.ScrollTextView;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class HomePage extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    Button select_catogary, topClickHere;
    View decorView;
    Typeface face;
    TextClock displayTime;
    Button reset;

    FrameLayout framevideo;
    RelativeLayout header_layout,bottom_layout;
    static FragmentManager fm;
    static FragmentTransaction ft;
    static CategoryFragment f;

    DatabaseHelper data_base;
    List<Advertisements> allToDos;
    SharedPreference sharedPreference;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    TextView marquee_add;
    String areaLocation;
    ImageView side_add;
    ArrayList<String> image_path;
    ArrayList<String> text_add;
    ArrayList<String> video_center_txt;
    ImageButton volume_btn;

    TextView userName;
    ScrollTextView marqueTxtAdd;
    TextView displayDate , area_name;
    ImageView transitLogo,digi_transit;
    SeekBar sb_Volume;
    AudioManager audioManager;

    boolean wasAPEnabled = false;
    static WifiAP wifiAp;
    private WifiManager wifi;

    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;

    public static VideoView vd;
    public static int l;
    public static ArrayList<String> video_path;
    public static ImageButton like_button;
    List<Categories>  all_category_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // getSupportActionBar().hide();
        setContentView(R.layout.activity_home_page);

        decorView = getWindow().getDecorView();
     //   int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        final int mUIFlag =
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(mUIFlag);

        //       transitLogo.setImageDrawable(Constants.transitLogo);

 //!!!!!!!!!!!!!!!!!!~~~~~ALL Declarartion and Initialization part~~~~~~~~~~~~~!!!!!!!!!!!!!!!!!!
//~~~~~~~~~~~~~~~~~~~~~~~setting the custom font for our texts "myriad pro"~~~~~~~~~~~~~~~~~~
        face= Typeface.createFromAsset(this.getAssets(), "fonts/myriad_pro.ttf");

        displayTime = (TextClock)findViewById(R.id.displayTime);
        transitLogo = (ImageView)findViewById(R.id.transitLogo);
        userName =(TextView)findViewById(R.id.userName);
        topClickHere = (Button) findViewById(R.id.top_click);
        select_catogary = (Button) findViewById(R.id.select_catogary);
        reset = (Button) findViewById(R.id.reset);
        displayDate = (TextView)findViewById(R.id.displayDate);
        vd =(VideoView) findViewById(R.id.videoView);
        side_add =(ImageView)findViewById(R.id.addDisplay);
        marquee_add = (TextView)findViewById(R.id.marqueAdd);
        like_button =(ImageButton)findViewById(R.id.like_btn);
        area_name = (TextView)findViewById(R.id.area_name);
        framevideo = (FrameLayout)findViewById(R.id.framevideo);
        header_layout =(RelativeLayout)findViewById(R.id.header_layout);
        bottom_layout =(RelativeLayout)findViewById(R.id.bottom_layout);
        volume_btn = (ImageButton)findViewById(R.id.volume_icon);
        sb_Volume = (SeekBar)findViewById(R.id.seekBar1);
        sharedPreference =  new SharedPreference();
        digi_transit = (ImageView)findViewById(R.id.digi_transit);

        displayTime.setTypeface(face);
        userName.setTypeface(face);
        topClickHere.setTypeface(face);
        select_catogary.setTypeface(face);
        displayDate.setTypeface(face);
        marquee_add.setTypeface(face);
        area_name.setTypeface(face);

        reset.setText("RESET");
        select_catogary.setText("MENU");

        fm = getFragmentManager();
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Logos Image setting ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String transitPath = Environment.getExternalStorageDirectory()+ "/TrasnsitLogos/";

        File imgFile = new File(transitPath+"main_logo.jpg");
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            transitLogo.setImageBitmap(myBitmap);
        }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Logos Image setting ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        volume_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                sb_Volume.setMax(10);
                if (currentVolume == 0) {
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    int seventyVolume = (int) (maxVolume*0.5f);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seventyVolume, 0);

                    sb_Volume.setProgress(10);
                    volume_btn.setImageDrawable(getResources().getDrawable(R.drawable.volume));

                } else {
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    sb_Volume.setProgress(0);
                    volume_btn.setImageDrawable(getResources().getDrawable(R.drawable.mute_volume));
                }
            }
        });
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~Location name display~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        buildGoogleApiClient();
//~~~~~~~~~~~~~~~~~~~~~~~setting the custom font for our texts "myriad pro" ends~~~~~~~~~~~~~~~~~~

//~~~~~~~~~~~~~~~~WIFI~~~~~~~~~~~~~~
        wifiAp = new WifiAP();
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//~~~~~~~~~~~~~~~~~~displaying user name~~~~~~~~~~~~~~~~~~~~
        if(DetaillsFetch.submitted == false || Constants.user_name == null){
            userName.setVisibility(View.GONE);
            topClickHere.setVisibility(View.VISIBLE);
        }else if(Constants.user_name != null || DetaillsFetch.submitted == true){
            userName.setTypeface(face);
            userName.setText(Constants.user_name);
            userName.setVisibility(View.VISIBLE);
            topClickHere.setVisibility(View.GONE);
        }

///~~~~~~~~~~~~~~~~user name~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//~~~~~~~~~~~~~~~~~~displaying date~~~~~~~~~~~~~~~~~~~~~~~~~
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        int yy = c.get(Calendar.YEAR);
        String month_name = month_date.format(c.getTime());
        int dd = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview ,  Month is 0 based, just add 1
        displayDate.setTypeface(face);
        displayDate.setText(new StringBuilder().append(dd).append("-").append(month_name).append("-").append(yy));

        image_path = new  ArrayList<String>();
        video_path = new  ArrayList<String>();
        text_add = new  ArrayList<String>();
        video_center_txt = new  ArrayList<String>();
//-------------------------database works starts---------------------------------

        Constants.download_video_status = new ArrayList<String>();
        Constants.download_image_status = new ArrayList<String>();

        data_base = DatabaseHelper.getInstance(this);

//~~~~~~~~~~~~~~~~adding ends here categories manually~~~~~~~~~~~~~~~~~~~~~~~~~~

        all_category_data = data_base.getAllCategories();

        if(all_category_data.size() == 0) {
            new GetCategory().execute("http://www.transit-ads.com/api/v1/categories");
        }


//~~~~~~~~~~~~~~~~adding ends here categories manually~~~~~~~~~~~~~~~~~~~~~~~~~~

try {
    if (Constants.arrayList_all_ads.size() == 0) {

        startActivity(new Intent(getApplicationContext(), DownloadActivity.class));
        finish();
    } else {
        data_base.truncateAdsDatabase();

        for (int i = 0; i < Constants.arrayList_all_ads.size(); i++) {

            JSONObject obj = Constants.arrayList_all_ads.get(i);
            try {
                if (obj.get("type").equals("video")) {
                    String path = Environment.getExternalStorageDirectory() + "/TransitVideo/" + obj.getString("content");
                    File f = new File(path);
                    if (f.exists()) {
                        Advertisements ads = new Advertisements(obj.getInt("id"), obj.getString("name"), obj.getInt("company_id"),obj.getInt("group_id"), obj.getString("type"), "TransitVideo/" + obj.getString("content"), obj.getString("categories"), null);
                        long ads_insert = data_base.createAdvertisement(ads);
                    }

                } else if (obj.get("type").equals("image")) {
                    String path = Environment.getExternalStorageDirectory() + "/TransitImage/" + obj.getString("content");
                    File f = new File(path);
                    if (f.exists()) {
                        Advertisements ads = new Advertisements(obj.getInt("id"), obj.getString("name"), obj.getInt("company_id"),obj.getInt("group_id"), obj.getString("type"), "TransitImage/" + obj.getString("content"), obj.getString("categories"), null);
                        long ads_insert = data_base.createAdvertisement(ads);
                    }
                } else if (obj.get("type").equals("scroll")) {
                    Advertisements ads = new Advertisements(obj.getInt("id"), obj.getString("name"), obj.getInt("company_id"),obj.getInt("group_id"), obj.getString("type"), obj.getString("content"), obj.getString("categories"), null);
                    long ads_insert = data_base.createAdvertisement(ads);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}catch (NullPointerException e){
    e.printStackTrace();
}
        // Getting all Todos
        Log.d("Get Todos", "Getting All ToDos");

        allToDos = data_base.getAllAdvertisements();
        for (Advertisements todo : allToDos) {

            if(todo.getType().equals("image")){
                image_path.add(todo.getContent());
            }else if(todo.getType().equals("video")){
                video_path.add(todo.getContent());
            }else if(todo.getType().equals("scroll")){
                text_add.add(todo.getContent());
            }
        }

        if(video_path != null){
            Constants.video_path_holder = new ArrayList<String>();
            Constants.video_path_holder.addAll(video_path);

        }
//-------------------------database works ends---------------------------------
        select_catogary.setOnClickListener(this);
        topClickHere.setOnClickListener(this);
        like_button.setOnClickListener(this);

        // below text will move like marquee
        marquee_add.setSelected(true);
 // --------------Reset onclick and long press listeners-----------------------

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reset.getText().toString().trim() == "RESET"){
                    Intent intent = new Intent(getApplicationContext(), DetaillsFetch.class);
                    startActivity(intent);

                    Constants.user_id = 0;
                    Constants.user_name= null;
                    Constants.user_phone = null;

                }else if(reset.getText().toString().trim() == "Bengaluru"){
                    Intent intent = new Intent(getApplicationContext(), AboutBengaluru.class);
                    startActivity(intent);
                }
            }
        });

        reset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext() , "Log Presses" , Toast.LENGTH_SHORT).show();
                return false;
            }
        });

//---------------------- side image change 2 every  second------------------------

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int j = 0;

            public void run() {
                if (j > image_path.size() - 1) {
                    j = 0;
                }
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + image_path.get(j).toString());
                side_add.setImageURI(uri);

                j++;

                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 2000);

//----------------------------image code ends-------------------------------------

//----------------------------------video sequence -------------------------------

      play_videos();

//----------------------------------video sequence ends---------------------------------


//--------------------------------moving text add code start -----------------------------
        marqueTxtAdd =(ScrollTextView)findViewById(R.id.marqueAdd);
        marqueTxtAdd.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                marqueTxtAdd.setTypeface(face);
                marqueTxtAdd.setText(text_add.get(i).toString());
                marqueTxtAdd.setTextColor(Color.parseColor("#F7921E"));
                marqueTxtAdd.startScroll();
                i++;
                if (i == text_add.size())
                    i = 0;
                marqueTxtAdd.postDelayed(this, 20000);
            }
        });
//--------------------------------moving text add code ends-----------------------------~~~~~~~~~~~~

//~~~~~~~~~~~~~~~~~~~~~~Fragment creation~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        framevideo.setOnClickListener(this);
        header_layout.setOnClickListener(this);
        bottom_layout.setOnClickListener(this);

//--------------------------------volume controler for videoview start ---------------------------~~

        //sb_Volume.getProgressDrawable().setColorFilter(getResources().getColor(R.color.red_volume_color), PorterDuff.Mode.SRC_ATOP);
        sb_Volume.getThumb().setColorFilter(getResources().getColor(R.color.red_volume_color), PorterDuff.Mode.SRC_ATOP);
//        sb_Volume.setMax(100);

        if(sb_Volume!=null){

            sb_Volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            sb_Volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            int check_mute = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if(check_mute < 1){
                volume_btn.setImageDrawable(getResources().getDrawable(R.drawable.mute_volume));
            }else{
                volume_btn.setImageDrawable(getResources().getDrawable(R.drawable.volume));
            }

            sb_Volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {

                    // TODO Auto-generated method stub
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    volume_btn.setImageDrawable(getResources().getDrawable(R.drawable.volume));
                    if(progress < 1){
                        volume_btn.setImageDrawable(getResources().getDrawable(R.drawable.mute_volume));
                    }
                }
            });
        }

//--------------------------------volume controler for videoview end -----------------------------

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~playing video~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static void play_videos() {

        l = 0;
    //    Constants.video_path_holder.clear();

        Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + video_path.get(l).toString());
        if (uri!=null){
            vd.setVideoURI(uri);
        }
        vd.start();
        l = 1;

        vd.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.setVolume(5, 5);
            }
        });

        vd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if(l == video_path.size()) {
                    video_path.clear();
//                    ft = fm.beginTransaction();
//                    f = (CategoryFragment) fm.findFragmentByTag("tag");
//                    ft.remove(f);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
//                    ft.commit();
                    video_path.addAll(Constants.video_path_holder);
                    l = 0;
                }
                vd.setVideoPath(Environment.getExternalStorageDirectory() + "/" + video_path.get(l));
                l++;
                like_button.setBackgroundResource(R.drawable.heart_empty);
                vd.start();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.select_catogary:

                fm = getFragmentManager();
                ft = fm.beginTransaction();
                f = (CategoryFragment) fm.findFragmentByTag("tag");

                if(f == null) {  // not added
                    f = new CategoryFragment();
                    ft.add(R.id.category_fragment, f, "tag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    select_catogary.setText("HOME");
                    reset.setText("Bengaluru");

                } else {  // already added
                    ft.remove(f);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    select_catogary.setText("MENU");
                    reset.setText("RESET");
                }

                ft.commit();
                break;

            case R.id.like_btn:

                like_button.setBackgroundResource(R.drawable.heart_clicked);
                String str_check = Constants.user_name;
                    if (str_check == null) {
                        Toast.makeText(this, "Please click FREE WIFI button ", Toast.LENGTH_LONG).show();
                    } else {
                        for (Advertisements todo : allToDos) {
                                if (todo.getContent().equals(video_path.get(l).toString())) {
                                    UserDetails liked_user = data_base.getSingleUserDetails(Constants.user_id);
                                    String path = "http://www.transit-ads.com/api/v1/passengers/likeAdvertisement?imei_no=" +
                                            sharedPreference.getValue(this,"IMIE")+ "&passenger_name=" + liked_user.getName() + "&passenger_mobile_number=" +
                                            liked_user.getPhone() + "&ad_id=" + todo.getId() + "&group_id=" + todo.getGroup_id();
                                    new PassengerLikeAPI().execute(path);

                                    break;
                                }
                            }
                    }

                break;

            case R.id.top_click:

                startActivity(new Intent(getApplicationContext(), DetaillsFetch.class));
                finish();

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "HomePage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.transit_ads.tabs/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "HomePage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.transit_ads.tabs/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (wasAPEnabled) {
            if (wifiAp.getWifiAPState()!=wifiAp.WIFI_AP_STATE_ENABLED && wifiAp.getWifiAPState()!=wifiAp.WIFI_AP_STATE_ENABLING){
                wifiAp.toggleWiFiAP(wifi, HomePage.this);
            }
        }
        updateStatusDisplay();


        decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onPause() {
        super.onPause();
        boolean wifiApIsOn = wifiAp.getWifiAPState()==wifiAp.WIFI_AP_STATE_ENABLED || wifiAp.getWifiAPState()==wifiAp.WIFI_AP_STATE_ENABLING;
        if (wifiApIsOn) {
            wasAPEnabled = true;
            wifiAp.toggleWiFiAP(wifi, HomePage.this);
        } else {
            wasAPEnabled = false;
        }
        updateStatusDisplay();
    }

    public static void updateStatusDisplay() {
        if (wifiAp.getWifiAPState()==wifiAp.WIFI_AP_STATE_ENABLED || wifiAp.getWifiAPState()==wifiAp.WIFI_AP_STATE_ENABLING) {
    //        btnWifiToggle.setText("Turn off");
        } else {
    //        btnWifiToggle.setText("Turn on");
        }
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
                //   Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
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
            }
        }
    }

    public class GetCategory extends AsyncTask<String , Void ,String> {
        String server_response;
        @Override
        protected String doInBackground(String... strings) {

            InputStream input_stream = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int response = conn.getResponseCode();
                input_stream = conn.getInputStream();
                server_response = readStream(input_stream);
                return server_response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }  finally {
                if (input_stream != null) {
                    try {
                        input_stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s == null){
                Log.e("Response", "" + server_response);
            }else{
                try {
                    JSONObject jsonObj = new JSONObject(server_response);
                    JSONArray array = jsonObj.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        try {
                            Categories cat1 = new Categories(obj.getInt("id"), obj.getString("name"), obj.getString("icon"), obj.getString("color"));
                            long cat1_id = data_base.createCategories(cat1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }

        }
    }
    // Converting InputStream to String
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public class PassengerLikeAPI extends AsyncTask<String , Void ,String> {
        String server_response;
        @Override
        protected String doInBackground(String... strings) {

            InputStream input_stream = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int response = conn.getResponseCode();
//                if(response == 200){
//                    return server_response="200";
//                }
                input_stream = conn.getInputStream();
                server_response = readStream(input_stream);
                return server_response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }  finally {
                if (input_stream != null) {
                    try {
                        input_stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s == null){
                Log.e("Response", "" + server_response);
               // Toast.makeText(getApplicationContext(),"unable to update to server",Toast.LENGTH_LONG).show();
            }else{

              //  Toast.makeText(getApplicationContext(),"Successfully sent",Toast.LENGTH_LONG).show();
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray j_array = jsonObject.getJSONArray("data");
                    JSONObject jsonObj = new JSONObject(j_array.getString(0));
                    if(jsonObj.getString("result").equals("Acknowledged")){
                        Toast.makeText(getApplicationContext(),"Successfully sent",Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }
    }

}