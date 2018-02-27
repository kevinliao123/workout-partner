package com.fruitguy.workoutpartner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.StringDef;

import com.fruitguy.workoutpartner.data.User;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by heliao on 10/17/17.
 */

public class UserDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WorkoutPartnerUser";
    static final String TABLE_USER = "user";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({USER_NAME, USER_GENDER, USER_AGE, USER_WEIGHT
            , USER_STATUS, USER_IMAGE, USER_THUMB_NAIL})
    public @interface UserData {
    }

    static final String USER_NAME = "name";
    static final String USER_GENDER = "gender";
    static final String USER_AGE = "weight";
    static final String USER_WEIGHT = "age";
    static final String USER_STATUS = "status";
    static final String USER_IMAGE = "image";
    static final String USER_THUMB_NAIL = "thumbNail";

    public static final int DATABASE_VERSION = 1;

    private static UserDatabaseHelper mInstance;
    Handler mHandler;

    public static UserDatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserDatabaseHelper(context);
        }
        return mInstance;
    }

    private UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        HandlerThread ht = new HandlerThread("UserDatabaseThread");
        ht.start();
        mHandler = new UserDatabaseHandler(ht.getLooper());

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " ("
                + USER_NAME + "TEXT, "
                + USER_GENDER + "TEXT, "
                + USER_AGE + "TEXT, "
                + USER_WEIGHT + "TEXT, "
                + USER_STATUS + "TEXT, "
                + USER_IMAGE + "TEXT,"
                + USER_THUMB_NAIL + "TEXT"
                + ")";

        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUser(User user) {
        mHandler.obtainMessage(UserHandlerMessage.ADD_USER.ordinal(), user).sendToTarget();
    }

    public void updateUser(User user) {
        mHandler.obtainMessage(UserHandlerMessage.UPDATE_USER.ordinal(), user).sendToTarget();
    }

    public User retrieveUser() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.isClosed() && cursor.moveToFirst()) {
            return getUserFromCursor(cursor);
        }
        return null;
    }

    private void internalAddUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = getUserContentValues(user);
        db.insert(TABLE_USER, null, contentValues);
        db.close();

    }

    private int internalUpdateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getUserContentValues(user);
         return db.update(TABLE_USER, contentValues, "id = ?", new String[]{String.valueOf(1)});
    }

    private User getUserFromCursor(Cursor cursor) {
        return new User.UserBuilder()
                .setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME)))
                .setGender(cursor.getString(cursor.getColumnIndex(USER_GENDER)))
                .setAge(cursor.getString(cursor.getColumnIndex(USER_AGE)))
                .setStatus(cursor.getString(cursor.getColumnIndex(USER_STATUS)))
                .setImage(cursor.getString(cursor.getColumnIndex(USER_IMAGE)))
                .setThumbNail(cursor.getString(cursor.getColumnIndex(USER_THUMB_NAIL)))
                .create();
    }

    private ContentValues getUserContentValues(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, user.getUserName());
        contentValues.put(USER_GENDER, user.getGender());
        contentValues.put(USER_AGE, user.getAge());
        contentValues.put(USER_WEIGHT, user.getWeight());
        contentValues.put(USER_STATUS, user.getStatus());
        contentValues.put(USER_IMAGE, user.getImage());
        contentValues.put(USER_THUMB_NAIL, user.getImage());

        return contentValues;
    }

    private enum UserHandlerMessage {
        ADD_USER,
        UPDATE_USER;

        private static UserHandlerMessage valueOf(int ordinal) {
            if (ordinal < 0 || ordinal >= UserHandlerMessage.values().length) {
                throw new IllegalArgumentException();
            }

            return UserHandlerMessage.values()[ordinal];
        }
    }

    private class UserDatabaseHandler extends android.os.Handler {

        UserDatabaseHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            UserHandlerMessage message = UserHandlerMessage.valueOf(msg.what);

            switch (message) {
                case ADD_USER:
                    internalAddUser((User) msg.obj);
                    break;
                case UPDATE_USER:
                    internalUpdateUser((User) msg.obj);
                    break;
            }
        }
    }


}
