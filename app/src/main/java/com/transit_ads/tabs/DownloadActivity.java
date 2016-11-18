package com.transit_ads.tabs;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.transit_ads.tabs.database.DatabaseHelper;
import com.transit_ads.tabs.database.SharedPreference;
import com.transit_ads.tabs.model.Categories;
import com.transit_ads.tabs.utlities.Constants;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DownloadActivity extends AppCompatActivity {

    TelephonyManager tel;
    String resultant_data;
    String[] scroollText;
    ProgressDialog pd;

    DownloadManager downloadManager;
    ArrayList<String> downloaded_add_title;
    int presence_download = 0;
    IntentFilter filter;
    DatabaseHelper data_base;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_download);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        sharedPreference = new SharedPreference();
//        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        Constants.tablet_imie = tel.getDeviceId().toString();

        Constants.arrayList_all_ads = new ArrayList<JSONObject>();
        downloaded_add_title = new ArrayList<String>();
        data_base = DatabaseHelper.getInstance(this);

        //set filter to only when download is complete and register broadcast receiver
        filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        downloading_Logos("http://www.transit-ads.com/api/v1/getUIElements?element=main_logo.jpg","main_logo.jpg");
        downloading_Logos("http://www.transit-ads.com/api/v1/getUIElements?element=city_bg.jpg","city_bg.jpg");
        downloading_Logos("http://www.transit-ads.com/api/v1/getUIElements?element=company_logo.jpg","company_logo.jpg");

        new GetCategory().execute("http://www.transit-ads.com/api/v1/categories");

        new GetMethodDemo().execute("http://www.transit-ads.com/api/v1/broadcast/"+sharedPreference.getValue(this,"IMIE"));


    }


    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE) ){
                //Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.download_finished, "Here should be the name", Toast.LENGTH_SHORT).show();
                Bundle extras = intent.getExtras();
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
                Cursor c = downloadManager.query(q);

                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // process download
                        downloaded_add_title.add(c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE)));
                        // get other required data by changing the constant passed to getColumnIndex
                    }
                }
                c.close();
            }
            if(Constants.video_List.size()+Constants.images_List.size() == downloaded_add_title.size()
                && downloaded_add_title.size()+scroollText.length ==Constants.arrayList_all_ads.size() ){

                    startActivity(new Intent(getApplicationContext(), DetaillsFetch.class));
                    finish();

            }else {
                int length=0;
                ArrayList<String> video_file_names = new ArrayList<String>();
                ArrayList<String> image_file_names = new ArrayList<String>();

                for(int i=0;i<Constants.arrayList_all_ads.size();i++){
                    try {
                        if(Constants.arrayList_all_ads.get(i).getString("type") == "video"){
                            length = length+1;
                            video_file_names.add(Constants.arrayList_all_ads.get(i).getString("content"));
                        }else if(Constants.arrayList_all_ads.get(i).getString("type") == "image"){
                            length = length+1;
                            image_file_names.add(Constants.arrayList_all_ads.get(i).getString("content"));
                        }else  if(Constants.arrayList_all_ads.get(i).getString("type") == "scroll"){
                            length = length+1;
                        }else  if(Constants.arrayList_all_ads.get(i).getString("type") == "sticker"){
                            length = length+1;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                String path = Environment.getExternalStorageDirectory()+"/TransitVideo/";
                File f = new File(path);
                File file[] = f.listFiles();
                if(file != null){
                    for (int i=0; i < file.length; i++)
                    {
                        for(int j=0;j<video_file_names.size();j++){
                            if(video_file_names.get(j).equals(file[i].getName())) {
                            }else {
                                file[i].delete();
                            }
                        }

                    }
                }else {

                }

                String image_path = Environment.getExternalStorageDirectory()+"/TransitImage/";
                File imagefiles = new File(image_path);
                File files[] = imagefiles.listFiles();
                if(files != null){
                    for (int i=0; i < files.length; i++)
                    {
                        for(int j=0;j<image_file_names.size();j++){
                            if(image_file_names.get(j).equals(files[i].getName())) {
                            }else {
                                files[i].delete();
                            }
                        }

                    }
                }else {

                }


                if(length == Constants.arrayList_all_ads.size()){
                    startActivity(new Intent(getApplicationContext(), DetaillsFetch.class));
                    finish();
                }
            }

        }
    };

    public class GetMethodDemo extends AsyncTask<String , Void ,String> {
        String server_response;
        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(DownloadActivity.this);
            pd.setMessage("loading");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }
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
                resultant_data = readStream(input_stream);
                JSONObject jsonObj = new JSONObject(resultant_data);
                if(!jsonObj.equals(null)){
                    JSONArray array = jsonObj.getJSONArray("result");
                    for ( int i=0 ; i<array.length() ;i++){
                        Constants.arrayList_all_ads.add(array.getJSONObject(i));
                    }
                }
                return resultant_data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
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
   //             pd.dismiss();
                Log.e("Response", "" + server_response);
            }else{

//                pd.dismiss();
                Log.e("Response", "" + server_response);
                Constants.scrolling_txts = new ArrayList<String>(Constants.arrayList_all_ads.size());
                Constants.video_List = new ArrayList<JSONObject>(Constants.arrayList_all_ads.size());
                Constants.images_List = new ArrayList<JSONObject>(Constants.arrayList_all_ads.size());
                for(int i=0;i< Constants.arrayList_all_ads.size();i++){
                    JSONObject obj = Constants.arrayList_all_ads.get(i);
                    try {
                        if(obj.get("type").equals("video")){
                            Constants.video_List.add(obj);
                        }else if(obj.get("type").equals("image")){
                            Constants.images_List.add(obj);
                        }else if(obj.get("type").equals("scroll")){
                            Constants.scrolling_txts.add(obj.getString("content"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(getApplicationContext(),""+Constants.arrayList_all_ads.size()+
                        "Vid="+Constants.video_List.size()+"img="+Constants.images_List.size()+"txt="
                        +Constants.scrolling_txts.size(),Toast.LENGTH_LONG).show();

                if(!(Constants.video_List.isEmpty())){
                    downloading_video(Constants.video_List);
                }
                if(!(Constants.images_List.isEmpty())){
                    downloading_image(Constants.images_List);

                }if(!(Constants.scrolling_txts.isEmpty())){
                    scroollText=new String[Constants.scrolling_txts.size()];

                    for(int k=0;k<Constants.scrolling_txts.size();k++){
                        scroollText[k] = Constants.scrolling_txts.get(k).toString();
                    }
                }
            }

        }
    }

    public void downloading_video(ArrayList<JSONObject> video_aaraylist){

        for(int i=0;i<video_aaraylist.size();i++){
            JSONObject video = Constants.video_List.get(i);
            try {
                File file = new File(Environment.getExternalStorageDirectory()+"/TransitVideo/"+video.getString("content"));
                if(file.exists()){
                    presence_download=presence_download+1;
                    continue;
                }else {
                    String url = "http://www.transit-ads.com/api/v1/getDownload?id=" + video.getString("id")
                            + "&name=" + video.getString("content");
                    File direct = new File(Environment.getExternalStorageDirectory() + "/TransitVideo/");
                    if (!direct.exists()) {
                        direct.mkdirs();
                    }
                    downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(
                            downloadUri);

                    request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle(video.getString("content"))
                            .setDescription("downloading")
                            .setDestinationInExternalPublicDir("/TransitVideo", video.getString("content"));

                    downloadManager.enqueue(request);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(presence_download == video_aaraylist.size()+Constants.images_List.size()){
            startActivity(new Intent(getApplicationContext(),DetaillsFetch.class));
            finish();
        }
    }
    public void downloading_image(ArrayList<JSONObject> image_aaraylist){

        for(int i=0;i<image_aaraylist.size();i++){
            JSONObject image = image_aaraylist.get(i);
            try {
                File file = new File(Environment.getExternalStorageDirectory() + "/TransitImage/" +image.getString("content"));
                if(file.exists()){
                    presence_download=presence_download+1;
                    continue;
                }else {
                    String url = "http://www.transit-ads.com/api/v1/getDownload?id=" + image.getString("id")
                            + "&name=" + image.getString("content");
                    File direct = new File(Environment.getExternalStorageDirectory() + "TransitImage");

                    if (!direct.exists()) {
                        direct.mkdirs();
                    }
                    downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);

                    Uri downloadUri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(
                            downloadUri);

                    request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI
                                    | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle(image.getString("content"))
                            .setDescription("downloading.")
                            .setDestinationInExternalPublicDir("/TransitImage", image.getString("content"));

                    downloadManager.enqueue(request);
                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        if(presence_download == image_aaraylist.size()+Constants.video_List.size()){
            startActivity(new Intent(getApplicationContext(),DetaillsFetch.class));
            finish();
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
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(downloadReceiver, filter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(downloadReceiver);
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

    public void downloading_Logos(String logo_url,String filename){

                File file = new File(logo_url);
                if(file.exists()){
                    file.delete();
                }else {

                    File direct = new File(Environment.getExternalStorageDirectory() + "/TrasnsitLogos/");
                    if (!direct.exists()) {
                        direct.mkdirs();
                    }else {
//                        try {
//                            FileUtils.cleanDirectory(direct);
//                        } catch (IOException e) {
//                            e.printStackTrace();

                        File image = new File(Environment.getExternalStorageDirectory() + "/TrasnsitLogos/" + filename);
                        if(image.exists()) image.delete();
                    }

                    downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse(logo_url);
                    DownloadManager.Request request = new DownloadManager.Request(
                            downloadUri);

                    request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle(filename)
                            .setDescription("downloading")
                            .setDestinationInExternalPublicDir("/TrasnsitLogos",filename);

                    downloadManager.enqueue(request);
                }
    }
}
