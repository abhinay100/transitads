package com.transit_ads.tabs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.transit_ads.tabs.adapters.CategoryAdapter;
import com.transit_ads.tabs.database.DatabaseHelper;
import com.transit_ads.tabs.model.Advertisements;
import com.transit_ads.tabs.model.Categories;
import com.transit_ads.tabs.utlities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 25-03-2016.
 */

public class CategoryFragment extends Fragment {

    ImageView returnToAds, bengalore;
    private ListView lv;
    List<Categories> categoriesAds;
    DatabaseHelper db;
    ProgressDialog pd;
    ArrayList<JSONObject> all_category_json;
    ArrayList<String> temp_videoPath;
    ArrayList<String> thumb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.category_layout, null);
        db = DatabaseHelper.getInstance(getActivity());
        categoriesAds = db.getAllCategories();
        if(categoriesAds.size() == 0){
            new GetCategory().execute("http://www.transit-ads.com/api/v1/categories");
        }else {
            lv = (ListView) v.findViewById(R.id.list_view);
            lv.setAdapter(new CategoryAdapter(getActivity(), categoriesAds));
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    int poistion_id = categoriesAds.get(position).getId();
                    temp_videoPath = new ArrayList<String>();
                    List<Advertisements> ads = db.getAllAdvertisements();
                    for(Advertisements advertisements:ads ){
                        String str = advertisements.getCategories();
                        String real_str = str.substring(1, str.length() - 1);
                        String[] splitted = real_str.split(",");
                        for(int i=0;i<splitted.length;i++){
                            String value = splitted[i].substring(1, splitted[i].length() - 1);
                            if(value.equals(String.valueOf(poistion_id))){
                                if(advertisements.getType().equals("video"))
                                    temp_videoPath.add(advertisements.getContent().toString());
                            }
                        }
                    }

                    try {
                        if (temp_videoPath.size() != 0) {
                            HomePage.video_path.clear();
                            HomePage.video_path.addAll(temp_videoPath);
                            HomePage.play_videos();
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                getActivity().getFragmentManager().beginTransaction().remove(CategoryFragment.this).commit();
            }
        });
        return v;
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
                            long cat1_id = db.createCategories(cat1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callAdapter();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }
    }

    private void callAdapter() {
        categoriesAds = db.getAllCategories();
        if(!categoriesAds.equals(null)){
            lv.setAdapter(new CategoryAdapter(getActivity(),categoriesAds));
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

}
