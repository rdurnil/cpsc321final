package com.durnil.rie.cpsc321final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WorkoutOpenHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "workoutDatabase.db";
    static final int DATABASE_VERSION = 1;

    static final String WORKOUT_TABLE = "workoutTable";
    static final String ID = "_id";
    static final String NAME = "name";
    static final String TYPE = "type";
    static final String DATE = "date";
    static final String TIME = "time";
    static final String DISTANCE = "distance";

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
                NAME + " TEXT, " + TYPE + " TEXT, " + DATE + " TEXT, "
                + TIME + " INTEGER, " + DISTANCE + " DOUBLE)";
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
                new String[]{ID, NAME, TYPE, DATE, TIME, DISTANCE},
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
            int time = cursor.getInt(4);
            double distance = cursor.getDouble(5);
            Workout workout = new Workout(id, name, type, date, time, distance);
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
                new String[]{ID, NAME, TYPE, DATE, TIME, DISTANCE},
                ID + "=?", new String[]{"" + id},
                null, null, null);
        Workout workout = null;
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            String type = cursor.getString(2);
            String date = cursor.getString(3);
            int time = cursor.getInt(4);
            double distance = cursor.getDouble(5);
            workout = new Workout(id, name, type, date, time, distance);
        }
        return workout;
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
         updateWorkoutIds();
    }
}
