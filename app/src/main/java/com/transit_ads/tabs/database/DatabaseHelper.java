package com.transit_ads.tabs.database;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.transit_ads.tabs.model.Advertisements;
import com.transit_ads.tabs.model.Categories;
import com.transit_ads.tabs.model.Feedback;
import com.transit_ads.tabs.model.Passenger;
import com.transit_ads.tabs.model.PassengerHasFeedback;
import com.transit_ads.tabs.model.UserDetails;

/**
 * Created by admin on 7/15/2016.
 */

//db as designed using singleton pattern
public class DatabaseHelper extends SQLiteOpenHelper {
    //singleton pattern
    private static DatabaseHelper sInstance;


    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "transitads";

    // Table Names
    private static final String TABLE_ADVERTISEMENT = "advertisements";
    private static final String TABLE_FEEDBACK = "passenger_feedback";
    private static final String TABLE_PASSENGER = "passenger";
    private static final String TABLE_PASSENGER_HAS_FEEDBACK = "pass_has_feedback";
    private static final String TABLE_USER_DETAILS = "user_details";
    private static final String TABLE_CATEGORIES = "categories";


    // Columns names of Advertisement-Table
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_COMPANY_ID = "company_id";
    private static final String COL_GROUP_ID = "group_id";
    private static final String COL_TYPE = "type";
    private static final String COL_CONTENT = "content";
    private static final String COL_CATEGORY = "categories";
    private static final String COL_THUMBNAIL = "thumbnail";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";
    private static final String COL_DELETED_AT = "deleted_at";

    // Columns names of passenger_feedback-Table
    private static final String KEY_ID = "id";
    private static final String KEY_TABLET_ID = "tablet_id";
    private static final String KEY_COMPANY_ID = "company_id";
    private static final String KEY_AD_ID = "ad_id";
    private static final String KEY_ACTION = "action";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";
    private static final String KEY_DELETED_AT = "deleted_at";

    // Columns names of passenger-Table
    private static final String KEY_COL_ID = "id";
    private static final String KEY_COL_NAME = "name";
    private static final String KEY_COL_PHONE = "phone_num";
    private static final String KEY_COL_WIFI_USED = "wifi_used";
    private static final String KEY_COL_CREATED_AT = "created_at";
    private static final String KEY_COL_UPDATED_AT = "updated_at";
    private static final String KEY_COL_DELETED_AT = "deleted_at";

    // Columns names of pass_has_feedback-Table
    public static final String COL_PASSENGER_ID="pass_id";
    public static final String COL_FEEDBACK_ID="feedback_id";

    // Columns names of User_details
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NAME= "name";
    public static final String COL_USER_PHONE = "phone";
    public static final String COL_USER_LIKE = "like";
    public static final String COL_USER_LIKE_ADID = "like_ad_id";

    // Columns names of Categories
    public static final String COL_CATEGORY_ID = "id";
    public static final String COL_CATEGORY_NAME= "name";
    public static final String COL_CATEGORY_ICON = "icon";
    public static final String COL_CATEGORY_COLOR = "color";



    // Table Create Statements for Table TABLE_ADVERTISEMENT
    // Todo table create statement
    private static final String CREATE_TABLE_ADVERTISEMENTS = "CREATE TABLE " + TABLE_ADVERTISEMENT + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME+ " TEXT,"
            + COL_COMPANY_ID + " INTEGER NOT NULL,"
            + COL_GROUP_ID + " INTEGER,"
            + COL_TYPE + " TEXT,"
            + COL_CONTENT + " TEXT,"
            + COL_CATEGORY + " TEXT,"
            + COL_THUMBNAIL + " TEXT,"
            + COL_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COL_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COL_DELETED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

    // Tag table create statement for Table TABLE_FEEDBACK
    private static final String CREATE_TABLE_FEEDBACK = "CREATE TABLE " + TABLE_FEEDBACK + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TABLET_ID + " INTEGER NOT NULL,"
            + KEY_COMPANY_ID + " INTEGER NOT NULL,"
            + KEY_AD_ID + " INTEGER NOT NULL,"
            + KEY_ACTION + " TEXT,"
            + COL_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + KEY_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + KEY_DELETED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

    // todo_tag table create statement for Table TABLE_PASSENGER
    private static final String CREATE_TABLE_PASSENGER = "CREATE TABLE "+ TABLE_PASSENGER + "("
            + KEY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_COL_NAME + " TEXT NOT NULL,"
            + KEY_COL_PHONE + " TEXT NOT NULL,"
            + KEY_COL_WIFI_USED + " INTEGER,"
            + KEY_COL_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + KEY_COL_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + KEY_COL_DELETED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

    // Tag table create statement for Table TABLE_PASSENGER_HAS_FEEDBACK
    private static final String CREATE_TABLE_PASSENGER_HAS_FEEDBACK = "CREATE TABLE " + TABLE_PASSENGER_HAS_FEEDBACK + "("
            + COL_PASSENGER_ID + " TEXT,"
            + COL_FEEDBACK_ID + " TEXT" + ")";

    // Table Create Statements for Table UserDetails
    private static final String CREATE_TABLE_USER_DETAILS = "CREATE TABLE " + TABLE_USER_DETAILS + "("
            + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_USER_NAME+ " TEXT,"
            + COL_USER_PHONE + " TEXT NOT NULL,"
            + COL_USER_LIKE + " TEXT ,"
            + COL_USER_LIKE_ADID + " TEXT,"
            + COL_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COL_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COL_DELETED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

    // Table Create Statements for Table Categories
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_CATEGORY_NAME+ " TEXT,"
            + COL_CATEGORY_ICON + " TEXT ,"
            + COL_CATEGORY_COLOR + " TEXT ,"
            + COL_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COL_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COL_DELETED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

//    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_ADVERTISEMENTS);
        db.execSQL(CREATE_TABLE_FEEDBACK);
        db.execSQL(CREATE_TABLE_PASSENGER);
        db.execSQL(CREATE_TABLE_PASSENGER_HAS_FEEDBACK);
        db.execSQL(CREATE_TABLE_USER_DETAILS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVERTISEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSENGER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSENGER_HAS_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

        // create new tables
        onCreate(db);
    }

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"ADVERTISEMENTS" table CRUD operation methods starts~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    /**
     * Creating a Advertisement
     */
    public long createAdvertisement(Advertisements ads) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, ads.getName());
        values.put(COL_COMPANY_ID, ads.getCompany_id());
        values.put(COL_GROUP_ID, ads.getGroup_id());
        values.put(COL_TYPE, ads.getType());
        values.put(COL_CONTENT, ads.getContent());
        values.put(COL_CATEGORY, ads.getCategories());
        values.put(COL_THUMBNAIL, ads.getThumbnail());
        values.put(COL_CREATED_AT, getDateTime());
        values.put(COL_CREATED_AT, getDateTime());
        values.put(COL_CREATED_AT, getDateTime());

        // insert row
        long ad_id = db.insert(TABLE_ADVERTISEMENT, null, values);

        return ad_id;
    }

    /**
     * get single Advertisement
     */
    public Advertisements getSingleAdvertisements(long todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ADVERTISEMENT + " WHERE "
                + COL_ID + " = " + todo_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Advertisements td = new Advertisements();
        td.setId(c.getInt((c.getColumnIndex(COL_ID))));
        td.setName((c.getString(c.getColumnIndex(COL_NAME))));
        td.setCompany_id(c.getInt(c.getColumnIndex(COL_COMPANY_ID)));
        td.setGroup_id(c.getInt(c.getColumnIndex(COL_GROUP_ID)));
        td.setType((c.getString(c.getColumnIndex(COL_TYPE))));
        td.setContent((c.getString(c.getColumnIndex(COL_CONTENT))));
        td.setCategories((c.getString(c.getColumnIndex(COL_CATEGORY))));
        td.setThumbnail((c.getString(c.getColumnIndex(COL_THUMBNAIL))));
        td.setCreated_at((c.getString(c.getColumnIndex(COL_CREATED_AT))));
        td.setUpdated_at((c.getString(c.getColumnIndex(COL_UPDATED_AT))));
        td.setDeleted_at((c.getString(c.getColumnIndex(COL_DELETED_AT))));

        return td;
    }

    /***      getting all Advertisements   ***/
    public List<Advertisements> getAllAdvertisements() {
        List<Advertisements> todos = new ArrayList<Advertisements>();
        String selectQuery = "SELECT  * FROM " + TABLE_ADVERTISEMENT;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Advertisements td = new Advertisements();
                td.setId(c.getInt((c.getColumnIndex(COL_ID))));
                td.setName((c.getString(c.getColumnIndex(COL_NAME))));
                td.setCompany_id(c.getInt(c.getColumnIndex(COL_COMPANY_ID)));
                td.setGroup_id(c.getInt(c.getColumnIndex(COL_GROUP_ID)));
                td.setType((c.getString(c.getColumnIndex(COL_TYPE))));
                td.setContent((c.getString(c.getColumnIndex(COL_CONTENT))));
                td.setCategories((c.getString(c.getColumnIndex(COL_CATEGORY))));
                td.setThumbnail((c.getString(c.getColumnIndex(COL_THUMBNAIL))));
                td.setCreated_at((c.getString(c.getColumnIndex(COL_CREATED_AT))));
                td.setUpdated_at((c.getString(c.getColumnIndex(COL_UPDATED_AT))));
                td.setDeleted_at((c.getString(c.getColumnIndex(COL_DELETED_AT))));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    /**
     * getting Advertisements count
     */
    public int getAdvertisementsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ADVERTISEMENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Advertisements
     */
    public int updateAdvertisements(Advertisements ads) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, ads.getName());
        values.put(COL_COMPANY_ID, ads.getCompany_id());
        values.put(COL_GROUP_ID, ads.getGroup_id());
        values.put(COL_TYPE, ads.getType());
        values.put(COL_CONTENT, ads.getContent());
        values.put(COL_CATEGORY, ads.getCategories());
        values.put(COL_THUMBNAIL, ads.getThumbnail());
        values.put(COL_CREATED_AT, getDateTime());
        values.put(COL_UPDATED_AT, getDateTime());
        values.put(COL_DELETED_AT, getDateTime());


        // updating row
        return db.update(TABLE_ADVERTISEMENT, values, COL_ID + " = ?",
                new String[] { String.valueOf(ads.getId()) });
    }

    /**
     * Deleting a Advertisements
     */
    public void deleteAdvertisement(long ads_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADVERTISEMENT, KEY_ID + " = ?",
                new String[]{String.valueOf(ads_id)});
    }
    public void truncateAdsDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADVERTISEMENT, null, null);
        db.close();
    }
//------------------------------------------End of CRUD operation for Advertisements Table-------------------------------------------//

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"FEEDBACK" table CRUD operation methods starts ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    /**
     * Creating a Feedback
     */
    public long createFeedback(Feedback feedback) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TABLET_ID, feedback.getTablet_id());
        values.put(KEY_COMPANY_ID, feedback.getCompany_id());
        values.put(KEY_AD_ID, feedback.getAd_id());
        values.put(KEY_ACTION, feedback.getAction());
        values.put(COL_CREATED_AT, getDateTime());
        values.put(COL_CREATED_AT, getDateTime());
        values.put(COL_CREATED_AT, getDateTime());


        // insert row
        long feedback_id = db.insert(TABLE_FEEDBACK, null, values);

//        // insert tag_ids
//        for (long tag_id : tag_ids) {
//            createTodoTag(todo_id, tag_id);
//        }

        return feedback_id;
    }

    /**
     * get single Feedback
     */
    public Feedback getFeedback(long feedback_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FEEDBACK + " WHERE "
                + KEY_ID + " = " + feedback_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Feedback td = new Feedback();
        td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
        td.setTablet_id((c.getInt(c.getColumnIndex(KEY_TABLET_ID))));
        td.setCompany_id(c.getInt(c.getColumnIndex(KEY_COMPANY_ID)));
        td.setAd_id((c.getInt(c.getColumnIndex(KEY_AD_ID))));
        td.setAction((c.getString(c.getColumnIndex(KEY_ACTION))));
        td.setCreated_at((c.getString(c.getColumnIndex(COL_CREATED_AT))));
        td.setUpdated_at((c.getString(c.getColumnIndex(COL_UPDATED_AT))));
        td.setDeleted_at((c.getString(c.getColumnIndex(COL_DELETED_AT))));

        return td;
    }

    /***      getting all Feedback   * */
    public List<Feedback> getAllFeedback() {
        List<Feedback> feed_back = new ArrayList<Feedback>();
        String selectQuery = "SELECT  * FROM " + TABLE_FEEDBACK;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Feedback td = new Feedback();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setTablet_id((c.getInt(c.getColumnIndex(KEY_TABLET_ID))));
                td.setCompany_id(c.getInt(c.getColumnIndex(KEY_COMPANY_ID)));
                td.setAd_id((c.getInt(c.getColumnIndex(KEY_AD_ID))));
                td.setAction((c.getString(c.getColumnIndex(KEY_ACTION))));
                td.setCreated_at((c.getString(c.getColumnIndex(COL_CREATED_AT))));
                td.setUpdated_at((c.getString(c.getColumnIndex(COL_UPDATED_AT))));
                td.setDeleted_at((c.getString(c.getColumnIndex(COL_DELETED_AT))));

                // adding to todo list
                feed_back.add(td);
            } while (c.moveToNext());
        }

        return feed_back;
    }

    /**
     * getting Feedback count
     */
    public int getFeedbackCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FEEDBACK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Feedback
     */
    public int updateFeedback(Feedback feedback) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TABLET_ID, feedback.getTablet_id());
        values.put(KEY_COMPANY_ID, feedback.getCompany_id());
        values.put(KEY_AD_ID, feedback.getAd_id());
        values.put(KEY_ACTION, feedback.getAction());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_UPDATED_AT, getDateTime());
        values.put(KEY_DELETED_AT, getDateTime());


        // updating row
        return db.update(TABLE_FEEDBACK, values, KEY_ID + " = ?",
                new String[] { String.valueOf(feedback.getId())});
    }

    /**
     * Deleting a Feedback
     */
    public void deleteFeedback(long feedback_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FEEDBACK, KEY_ID + " = ?",
                new String[]{String.valueOf(feedback_id)});
    }
//------------------------------------------End of CRUD operation for FEEDBACK Table-------------------------------------------------//


// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"PASSENGERS" table CRUD operation methods starts~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    /**
     * Creating a Passenger
     */
    public long createPassenger(Passenger passenger) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COL_ID, passenger.getId());
        values.put(KEY_COL_NAME, passenger.getName());
        values.put(KEY_COL_PHONE, passenger.getPhone());
        values.put(KEY_COL_WIFI_USED, passenger.getWifi_used());
        values.put(COL_CREATED_AT, getDateTime());
        values.put(COL_CREATED_AT, getDateTime());
        values.put(COL_CREATED_AT, getDateTime());

        // insert row
        long passenger_id = db.insert(TABLE_PASSENGER, null, values);

//        // insert tag_ids
//        for (long tag_id : tag_ids) {
//            createTodoTag(todo_id, tag_id);
//        }

        return passenger_id;
    }

    /**
     * get single Passenger
     */
    public Passenger getPassenger(long passenger_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PASSENGER + " WHERE "
                + KEY_COL_ID + " = " + passenger_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Passenger td = new Passenger();
        td.setId(c.getInt((c.getColumnIndex(KEY_COL_ID))));
        td.setName((c.getString(c.getColumnIndex(KEY_COL_NAME))));
        td.setPhone(c.getString(c.getColumnIndex(KEY_COL_PHONE)));
        td.setWifi_used((c.getInt(c.getColumnIndex(KEY_COL_WIFI_USED))));
        td.setCreated_at((c.getString(c.getColumnIndex(KEY_COL_CREATED_AT))));
        td.setUpdated_at((c.getString(c.getColumnIndex(KEY_COL_UPDATED_AT))));
        td.setDeleted_at((c.getString(c.getColumnIndex(KEY_COL_DELETED_AT))));

        return td;
    }

    /***      getting all Passenger   * */
    public List<Passenger> getAllPassenger() {
        List<Passenger> passenger = new ArrayList<Passenger>();
        String selectQuery = "SELECT  * FROM " + TABLE_PASSENGER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Passenger td = new Passenger();
                td.setId(c.getInt((c.getColumnIndex(KEY_COL_ID))));
                td.setName((c.getString(c.getColumnIndex(KEY_COL_NAME))));
                td.setPhone(c.getString(c.getColumnIndex(KEY_COL_PHONE)));
                td.setWifi_used((c.getInt(c.getColumnIndex(KEY_COL_WIFI_USED))));
                td.setCreated_at((c.getString(c.getColumnIndex(COL_CREATED_AT))));
                td.setUpdated_at((c.getString(c.getColumnIndex(COL_UPDATED_AT))));
                td.setDeleted_at((c.getString(c.getColumnIndex(COL_DELETED_AT))));

                // adding to Passenger list
                passenger.add(td);
            } while (c.moveToNext());
        }

        return passenger;
    }

    /**
     * getting Passenger count
     */
    public int getPassengerCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PASSENGER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Passenger
     */
    public int updatePassenger(Passenger passenger) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COL_NAME, passenger.getName());
        values.put(KEY_COL_PHONE, passenger.getPhone());
        values.put(KEY_COL_WIFI_USED, passenger.getWifi_used());
        values.put(KEY_COL_CREATED_AT, getDateTime());
        values.put(KEY_COL_UPDATED_AT, getDateTime());
        values.put(KEY_COL_DELETED_AT, getDateTime());


        // updating row
        return db.update(TABLE_PASSENGER, values, KEY_COL_ID + " = ?",
                new String[] { String.valueOf(passenger.getId()) });
    }

    /**
     * Deleting a Passenger
     */
    public void deletePassenger(long passenger_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PASSENGER, KEY_COL_ID + " = ?",
                new String[]{String.valueOf(passenger_id)});
    }
//------------------------------------------End of CRUD operation for PASSENGER Table-------------------------------------------//


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"PASSENGER_HAS_FEEDBACK" table CRUD operation methods starts~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    /**
     * Creating a PassengerHasFeedback
     */
    public long createPassengerHasFeedback(PassengerHasFeedback pass_has_feedback) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PASSENGER_ID, pass_has_feedback.getPassenger_id());
        values.put(COL_FEEDBACK_ID, pass_has_feedback.getFeedback_id());

        // insert row
        long pass_feed_id = db.insert(TABLE_PASSENGER_HAS_FEEDBACK, null, values);

//        // insert tag_ids
//        for (long tag_id : tag_ids) {
//            createTodoTag(todo_id, tag_id);
//        }

        return pass_feed_id;
    }

    /**
     * get single PassengerHasFeedback
     */
    public PassengerHasFeedback getPassengerHasFeedback(String PassengerHasFeedback_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ADVERTISEMENT + " WHERE "
                + COL_PASSENGER_ID + " = " + PassengerHasFeedback_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        PassengerHasFeedback td = new PassengerHasFeedback();
        td.setPassenger_id(c.getString((c.getColumnIndex(COL_PASSENGER_ID))));
        td.setFeedback_id(c.getString((c.getColumnIndex(COL_FEEDBACK_ID))));

        return td;
    }

    /**      getting all PassengerHasFeedback   * */
    public List<PassengerHasFeedback> getAllPassengerHasFeedback() {
        List<PassengerHasFeedback> pass_has_feedback = new ArrayList<PassengerHasFeedback>();
        String selectQuery = "SELECT  * FROM " + TABLE_PASSENGER_HAS_FEEDBACK;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PassengerHasFeedback td = new PassengerHasFeedback();
                td.setPassenger_id(c.getString((c.getColumnIndex(COL_PASSENGER_ID))));
                td.setFeedback_id(c.getString((c.getColumnIndex(COL_FEEDBACK_ID))));

                // adding to PassengerHasFeedback list
                pass_has_feedback.add(td);
            } while (c.moveToNext());
        }

        return pass_has_feedback;
    }

    /**
     * getting PassengerHasFeedback count
     */
    public int getPassengerHasFeedbackCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PASSENGER_HAS_FEEDBACK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a PassengerHasFeedback
     */
    public int updatePassengerHasFeedback(PassengerHasFeedback pass_feedback) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PASSENGER_ID, pass_feedback.getPassenger_id());
        values.put(COL_FEEDBACK_ID, pass_feedback.getFeedback_id());

        // updating row
        return db.update(TABLE_PASSENGER_HAS_FEEDBACK, values, COL_PASSENGER_ID + " = ?",
                new String[] { String.valueOf(pass_feedback.getPassenger_id()) });
    }

    /**
     * Deleting a PassengerHasFeedback
     */
    public void deletePassengerHasfeedack(String pass_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PASSENGER_HAS_FEEDBACK, COL_PASSENGER_ID + " = ?",
                new String[]{pass_id});
    }
//--------------------------------------End of CRUD operation for PASSENGER_HAS_FEEDBACK Table-------------------------------------------//

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"UserDetails" table CRUD operation methods starts~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    /**
     * Creating a UserDetails
     */
    public long createUserDetails(UserDetails ads) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, ads.getName());
        values.put(COL_USER_PHONE, ads.getPhone());
        values.put(COL_USER_LIKE, ads.getLike());
        values.put(COL_USER_LIKE_ADID, ads.getLike_ad_id());
        values.put(COL_CREATED_AT, getDateTime());
        values.put(COL_CREATED_AT, getDateTime());
        values.put(COL_CREATED_AT, getDateTime());

        // insert row
        long ad_id = db.insert(TABLE_USER_DETAILS, null, values);

        return ad_id;
    }

    /**
     * get single UserDetails
     */
    public UserDetails getSingleUserDetails(long todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_DETAILS + " WHERE "
                + COL_USER_ID + " = " + todo_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserDetails td = new UserDetails();
        td.setId(c.getInt((c.getColumnIndex(COL_USER_ID))));
        td.setName((c.getString(c.getColumnIndex(COL_USER_NAME))));
        td.setPhone(c.getString(c.getColumnIndex(COL_USER_PHONE)));
        td.setLike((c.getString(c.getColumnIndex(COL_USER_LIKE))));
        td.setLike_ad_id((c.getString(c.getColumnIndex(COL_USER_LIKE_ADID))));
        td.setCreated_at((c.getString(c.getColumnIndex(COL_CREATED_AT))));
        td.setUpdated_at((c.getString(c.getColumnIndex(COL_UPDATED_AT))));
        td.setDeleted_at((c.getString(c.getColumnIndex(COL_DELETED_AT))));

        return td;
    }
    /**
     * get single UserDetails
     */
    public UserDetails getSingleUserLastRecord() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM "+ TABLE_USER_DETAILS +" ORDER BY "+ COL_USER_ID +" DESC LIMIT 1";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserDetails td = new UserDetails();
        td.setId(c.getInt((c.getColumnIndex(COL_USER_ID))));
        td.setName((c.getString(c.getColumnIndex(COL_USER_NAME))));
        td.setPhone(c.getString(c.getColumnIndex(COL_USER_PHONE)));
        td.setLike((c.getString(c.getColumnIndex(COL_USER_LIKE))));
        td.setLike_ad_id((c.getString(c.getColumnIndex(COL_USER_LIKE_ADID))));
        td.setCreated_at((c.getString(c.getColumnIndex(COL_CREATED_AT))));
        td.setUpdated_at((c.getString(c.getColumnIndex(COL_UPDATED_AT))));
        td.setDeleted_at((c.getString(c.getColumnIndex(COL_DELETED_AT))));

        return td;
    }
    /***      getting all Users & details   ***/
    public List<UserDetails> getAllUserDetails() {
        List<UserDetails> todos = new ArrayList<UserDetails>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_DETAILS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserDetails td = new UserDetails();
                td.setId(c.getInt((c.getColumnIndex(COL_USER_ID))));
                td.setName((c.getString(c.getColumnIndex(COL_USER_NAME))));
                td.setPhone(c.getString(c.getColumnIndex(COL_USER_PHONE)));
                td.setLike((c.getString(c.getColumnIndex(COL_USER_LIKE))));
                td.setLike_ad_id((c.getString(c.getColumnIndex(COL_USER_LIKE_ADID))));
                td.setCreated_at((c.getString(c.getColumnIndex(COL_CREATED_AT))));
                td.setUpdated_at((c.getString(c.getColumnIndex(COL_UPDATED_AT))));
                td.setDeleted_at((c.getString(c.getColumnIndex(COL_DELETED_AT))));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    /**
     * getting UserDetails count
     */
    public int getUserDetailsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER_DETAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a UserDetails
     */
    public int updateUserDetails(UserDetails ads) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, ads.getId());
        values.put(COL_USER_PHONE, ads.getPhone());
        values.put(COL_USER_LIKE, ads.getLike());
        values.put(COL_USER_LIKE_ADID, ads.getLike_ad_id());
        values.put(COL_UPDATED_AT, getDateTime());

        // updating row
        return db.update(TABLE_USER_DETAILS, values, COL_USER_ID + " = ?",
                new String[] { String.valueOf(ads.getId()) });
    }

    /**
     * Deleting a UserDetails
     */
    public void deleteUserDetails(long ads_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_DETAILS, COL_USER_ID + " = ?",
                new String[]{String.valueOf(ads_id)});
    }
    public void truncateUserDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_DETAILS,null,null);
        db.close();
    }
//------------------------------------------End of CRUD operation for UserDetails Table-------------------------------------------//

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"CATEGORIES" table CRUD operation methods starts~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    /**
     * Creating a UserDetails
     */
    public long createCategories(Categories ads) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_CATEGORY_ID, ads.getId());
        values.put(COL_CATEGORY_NAME, ads.getName());
        values.put(COL_CATEGORY_ICON, ads.getIcon());
        values.put(COL_CATEGORY_COLOR, ads.getColor());
        values.put(COL_CREATED_AT, getDateTime());
        values.put(COL_CREATED_AT, getDateTime());
        values.put(COL_CREATED_AT, getDateTime());

        // insert row
        long ad_id = db.insert(TABLE_CATEGORIES, null, values);

        return ad_id;
    }

    /**
     * get single Categories
     */
    public Categories getSingleCategories(long todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE "
                + COL_CATEGORY_ID + " = " + todo_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Categories td = new Categories();
        td.setId(c.getInt((c.getColumnIndex(COL_CATEGORY_ID ))));
        td.setName((c.getString(c.getColumnIndex(COL_CATEGORY_NAME))));
        td.setIcon(c.getString(c.getColumnIndex(COL_CATEGORY_ICON)));
        td.setColor((c.getString(c.getColumnIndex(COL_CATEGORY_COLOR))));
        td.setCreated_at((c.getString(c.getColumnIndex(COL_CREATED_AT))));
        td.setUpdated_at((c.getString(c.getColumnIndex(COL_UPDATED_AT))));
        td.setDeleted_at((c.getString(c.getColumnIndex(COL_DELETED_AT))));

        return td;
    }

    /***      getting all Categories   ***/
    public List<Categories> getAllCategories() {
        List<Categories> todos = new ArrayList<Categories>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Categories td = new Categories();
                td.setId(c.getInt((c.getColumnIndex(COL_CATEGORY_ID))));
                td.setName((c.getString(c.getColumnIndex(COL_CATEGORY_NAME))));
                td.setIcon(c.getString(c.getColumnIndex(COL_CATEGORY_ICON)));
                td.setColor((c.getString(c.getColumnIndex(COL_CATEGORY_COLOR))));
                td.setCreated_at((c.getString(c.getColumnIndex(COL_CREATED_AT))));
                td.setUpdated_at((c.getString(c.getColumnIndex(COL_UPDATED_AT))));
                td.setDeleted_at((c.getString(c.getColumnIndex(COL_DELETED_AT))));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    /**
     * getting Categories count
     */
    public int getCategoriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Categories
     */
    public int updateCategories(Categories ads) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_CATEGORY_NAME, ads.getId());
        values.put(COL_CATEGORY_ICON, ads.getIcon());
        values.put(COL_CATEGORY_COLOR, ads.getColor());
        values.put(COL_UPDATED_AT, getDateTime());

        // updating row
        return db.update(TABLE_CATEGORIES, values, COL_CATEGORY_ID + " = ?",
                new String[] { String.valueOf(ads.getId()) });
    }

    /**
     * Deleting a Categories
     */
    public void deleteCategories(long ads_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, COL_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(ads_id)});
    }
    /**
     * Truncating the  a Categories
     */
    public void truncateCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES,null,null);
        db.close();
    }
//------------------------------------------End of CRUD operation for CATEGORIES Table-------------------------------------------//

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~get a DATE & TIME FOR created_at,updated_at,deleted_at~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

//----------------------------------------------End of Date & Time method-----------------------------------------------------------//
}
