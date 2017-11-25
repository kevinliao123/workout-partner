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
import com.fruitguy.workoutpartner.util.BitmapUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by heliao on 10/17/17.
 */

public class UserDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WorkoutPartnerUser";
    static final String TABLE_USER = "user";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({USER_NAME, USER_GENDER, USER_AGE, USER_COMMENT, USER_PORTRAIT})
    public @interface UserData {
    }

    static final String USER_NAME = "username";
    static final String USER_GENDER = "gender";
    static final String USER_AGE = "age";
    static final String USER_COMMENT = "comment";
    static final String USER_PORTRAIT = "portrait";

    public static final int DATABASE_VERSION = 1;

    private static UserDatabaseHelper mInstance;
    Handler mHander;

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
        mHander = new UserDatabaseHandler(ht.getLooper());

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " ("
                + USER_NAME + "TEXT, "
                + USER_GENDER + "TEXT, "
                + USER_AGE + "TEXT, "
                + USER_COMMENT + "TEXT, "
                + USER_PORTRAIT + "BLOB" + ")";

        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUser(User user) {
        mHander.obtainMessage(UserHandlerMessage.ADD_USER.ordinal(), user).sendToTarget();
    }

    public void updateUser(User user) {
        mHander.obtainMessage(UserHandlerMessage.UPDATE_USER.ordinal(), user).sendToTarget();
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
                .setComment(cursor.getString(cursor.getColumnIndex(USER_COMMENT)))
                .setPortrait(BitmapUtils.getImage(cursor.getBlob(cursor.getColumnIndex(USER_PORTRAIT))))
                .create();
    }

    private ContentValues getUserContentValues(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, user.getUserName());
        contentValues.put(USER_GENDER, user.getGender());
        contentValues.put(USER_AGE, user.getAge());
        contentValues.put(USER_COMMENT, user.getComment());
        contentValues.put(USER_PORTRAIT, BitmapUtils.getImageBytes(user.getPortrait()));

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
