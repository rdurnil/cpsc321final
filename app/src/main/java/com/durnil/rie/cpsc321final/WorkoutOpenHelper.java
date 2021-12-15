package com.durnil.rie.cpsc321final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class WorkoutOpenHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "workoutDatabase.db";
    static final int DATABASE_VERSION = 1;

    static final String LATLNG_TABLE = "latLngTable";
    static final String LATITUDE = "latitude";
    static final String LONGITUDE = "longitude";
    static final String ORDER_NUM = "order_num";
    static final String WORKOUT_TABLE = "workoutTable";
    static final String ID = "_id";
    static final String NAME = "name";
    static final String TYPE = "type";
    static final String DATE = "date";
    static final String TIME = "time";
    static final String DISTANCE = "distance";
    static final String AVG_SPEED = "avgSpeed";

    /**
     * Constructor for WorkoutOpenHelper
     */
    public WorkoutOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate method, creates the table to be used for workouts.
     *
     * @param sqLiteDatabase the database to create the table
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreate = "CREATE TABLE " + WORKOUT_TABLE + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                TYPE + " TEXT, " +
                DATE + " TEXT, " +
                TIME + " TEXT, " +
                DISTANCE + " DOUBLE, " +
                AVG_SPEED + " DOUBLE);";

        sqLiteDatabase.execSQL(sqlCreate);

        sqlCreate = "CREATE TABLE " + LATLNG_TABLE + "(" +
                ID + " INTEGER, " +
                LATITUDE + " FLOAT, " +
                LONGITUDE + " FLOAT, " +
                ORDER_NUM + " INT);";
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Inserts a workout into the database.
     *
     * @param workout the workout to be inserted
     */
    public void insertWorkout(Workout workout) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, workout.getWorkoutName());
        contentValues.put(TYPE, workout.getType());
        contentValues.put(DATE, workout.getDate());
        contentValues.put(TIME, workout.getTime());
        contentValues.put(DISTANCE, workout.getDistance());
        contentValues.put(AVG_SPEED, workout.getAvgSpeed());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(WORKOUT_TABLE, null, contentValues);
        db.close();
    }

    /**
     * Gets a cursor to go through all entries in the table.
     *
     * @return the Cursor to go through the table
     */
    public Cursor getSelectAllCursor() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(WORKOUT_TABLE,
                new String[]{ID, NAME, TYPE, DATE, TIME, DISTANCE, AVG_SPEED},
                null, null, null, null,
                null);
        return cursor;
    }

    /**
     * Gets all the workouts in the database and returns them in a list of Workout object.
     *
     * @return the entire List of workouts from the table in the database
     */
    public List<Workout> getSelectAllWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        Cursor cursor = getSelectAllCursor();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String type = cursor.getString(2);
            String date = cursor.getString(3);
            String time = cursor.getString(4);
            double distance = cursor.getDouble(5);
            double avgSpeed = cursor.getDouble(6);
            Workout workout = new Workout(id, name, type, date, time, distance, avgSpeed);
            workouts.add(workout);
        }
        return workouts;
    }

    /**
     * Finds the Workout corresponding to an index in the table.
     * Note that indexes and ids should line up in this programming
     * using other methods defined below.
     *
     * @param id the id/index to be selected
     * @return the Workout object corresponding to the index in the database
     */
    public Workout getSelectWorkoutById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(WORKOUT_TABLE,
                new String[]{ID, NAME, TYPE, DATE, TIME, DISTANCE, AVG_SPEED},
                ID + "=?", new String[]{"" + id},
                null, null, null);
        Workout workout = null;
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            String type = cursor.getString(2);
            String date = cursor.getString(3);
            String time = cursor.getString(4);
            double distance = cursor.getDouble(5);
            double avgSpeed = cursor.getDouble(6);
            workout = new Workout(id, name, type, date, time, distance, avgSpeed);
        }
        return workout;
    }

    public int getSelectWorkoutIdByData(String name, String type, String date, String time, double distance, double avgSpeed) {
        int id = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(WORKOUT_TABLE,
                new String[]{ID},
                NAME + "=? AND " + TYPE + "=? AND " + DATE + "=? AND " + TIME
                        + "=? AND " + DISTANCE + "=? AND " + AVG_SPEED + "=?",
                new String[]{name, type, date, time, Double.toString(distance), Double.toString(avgSpeed)},
                null, null, null);
        // in case there are multiple with the same data, go to last one since this is called right
        // after an insert
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        return id;
    }

    /**
     * Method to keep ids and indexes lined up for the RecyclerView.
     */
    public void updateWorkoutIds() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = getSelectAllCursor();
        int i = 0;
        while (cursor.moveToNext()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, i);
            db.update(WORKOUT_TABLE, contentValues,
                    ID + "=?", new String[]{"" + cursor.getInt(0)});
            i++;
        }
        db.close();
    }

    /**
     * Deletes a video by id
     *
     * @param id the id to be deleted
     */
    public void deleteWorkoutById(int id) {
         SQLiteDatabase db = getWritableDatabase();
         db.delete(WORKOUT_TABLE, ID + "=?", new String[]{"" + id});
         db.close();
         deleteLatLangById(id);
         updateWorkoutIds();
    }

    /**
     * Inserts a new lat lng into LATLANG table using workout id
     */
    public void insertLatLng(int id, LatLng latLng, int orderNum) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(LATITUDE, latLng.latitude);
        contentValues.put(LONGITUDE, latLng.longitude);
        contentValues.put(ORDER_NUM, orderNum);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(LATLNG_TABLE, null, contentValues);
        db.close();
    }

    /**
     * Deletes all the entries of a specific workout
     */
    public void deleteLatLangById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(LATLNG_TABLE, ID + "=?", new String[]{"" + id});
        db.close();
    }

    /**
     * Returns all of the latlangs of workout by id
     */
    public List<LatLng> getLatLangsById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(LATLNG_TABLE,
                new String[]{LATITUDE, LONGITUDE},
                ID + "=?", new String[]{"" + id},
                null, null, null);
        List<LatLng> latLngs = new ArrayList<>();
        LatLng temp;
        while (cursor.moveToNext()) {
            Double latitude = cursor.getDouble(0);
            Double longitude = cursor.getDouble(1);
            temp = new LatLng(latitude, longitude);
            latLngs.add(temp);
        }
        return latLngs;
    }
}
