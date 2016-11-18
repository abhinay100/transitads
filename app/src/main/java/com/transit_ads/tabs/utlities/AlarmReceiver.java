package com.transit_ads.tabs.utlities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.transit_ads.tabs.DownloadActivity;

import java.util.Calendar;

/**
 * Created by admin on 6/3/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();

        //~~~~~~~~~~~~~~~~~~~~~~~~~~un-comment it~~~~~~~~~~~~~~~~

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 13); // For 1 PM or 2 PM
//        calendar.set(Calendar.MINUTE, 00);
//        calendar.set(Calendar.SECOND, 0);

        Intent myIntent = new Intent(arg0 ,DownloadActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        arg0.startActivity(myIntent);

    }


    //    @Override
//    public void onReceive(Context context, Intent intent) {
//        Intent service1 = new Intent(context, MainActivity.class);
//        service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
//        context.startService(service1);
//        Log.d("TIME FOR RETRIVING", "RETRIVED");
//    }
}