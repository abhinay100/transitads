package com.transit_ads.tabs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transit_ads.tabs.utlities.AlarmReceiver;

import java.io.File;
import java.util.Calendar;

public class PoweredByPage extends AppCompatActivity {

    RelativeLayout backgroundLayout;
    ImageView digiTransitLogo;

    private AlarmManager manager;
    PendingIntent pendingIntent;

    //location name fetching
    ProgressBar mProgressBar;
    TextView progress_txt;
    Thread timer;

    Typeface face;
    TextView powered_by,contact_us,company_email,company_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_poweredby_page);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        //~~~~~~~~~~~~~~~~~~~~~~~setting the custom font for our texts "myriad pro"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        face= Typeface.createFromAsset(this.getAssets(), "fonts/myriad_pro.ttf");

        powered_by = (TextView)findViewById(R.id.campaign);
        contact_us = (TextView)findViewById(R.id.contact_us);
        company_phone = (TextView)findViewById(R.id.tvPhone);
        company_email = (TextView)findViewById(R.id.company_email);
        progress_txt = (TextView)findViewById(R.id.progress_txt);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        backgroundLayout = (RelativeLayout)findViewById(R.id.backgroundLayout);
        digiTransitLogo = (ImageView)findViewById(R.id.digiTransitLogo);


        powered_by.setTypeface(face);
        contact_us.setTypeface(face);
        company_phone.setTypeface(face);
        company_email.setTypeface(face);
        progress_txt.setTypeface(face);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Image setting ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String path = Environment.getExternalStorageDirectory()+ "/TrasnsitLogos/";

        File imgFile = new File(path+"company_logo.jpg");
        if(imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //  ImageView imageView=(ImageView)findViewById(R.id.imageView);
            digiTransitLogo.setImageBitmap(myBitmap);
        }
        File backFile = new File(path+"city_bg.jpg");
        if(backFile.exists()){
            Bitmap bmImg = BitmapFactory.decodeFile(backFile.getAbsolutePath());
            BitmapDrawable background = new BitmapDrawable(bmImg);
            backgroundLayout.setBackgroundDrawable(background);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~For every Night running 9 PM service~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(PoweredByPage.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21); // For 9 PM
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if(calendar.before(Calendar.getInstance())){ // if it's in the past, increment
            calendar.add(Calendar.DATE, 1);
        }

//        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~For every Night running 9 PM service ends ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~For every Morning 9 AM running service~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarm_Intent = new Intent(PoweredByPage.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarm_Intent, 0);
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 9); // For 9 AM
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        if(calendar.before(Calendar.getInstance())){ // if it's in the past, increment
            calendar.add(Calendar.DATE, 1);
        }

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~For every Morning running 9 AM service ends ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


        timer= new Thread()
        {
            public void run()
            {
                try
                {
                    // Display for 3 seconds
                    sleep(2000);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally
                {
                    // Goes to Activity  StartingPoint.java(STARTINGPOINT)
                    startActivity(new Intent(getApplicationContext() , HomePage.class));
                    finish();
                }
            }
        };
        timer.start();
    }

}
