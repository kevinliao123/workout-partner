package com.fruitguy.workoutpartner.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.data.User;
import com.fruitguy.workoutpartner.util.BitmapUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;


/**
 * Created by heliao on 11/6/17.
 */
@RunWith(AndroidJUnit4.class)
public class UserDatabaseHelperTest  {

    private static final String TAG = UserDatabaseHelper.class.getSimpleName();

    @Test
    public void AddUser() throws Exception {
        UserDatabaseHelper helper = UserDatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext());
        Bitmap image = BitmapFactory.decodeResource(InstrumentationRegistry.getTargetContext().getResources(), R.mipmap.ic_launcher);
        User user = new User.UserBuilder()
                .setUserName("test")
                .setAge("18")
                .setGender("Male")
                .setComment("Strong as a bull")
                .setPortrait(image)
                .create();
        assertNotNull(user);
        helper.addUser(user);
        User newUser = helper.retrieveUser();
        if(newUser != null) {
            Log.i(TAG, "new user is: " + newUser.toString());
        }
    }

//    @Test
//    public void updateUser() throws Exception {
//        helper.updateUser(createTestUser());
//    }
//
//    @Test
//    public void retrieveUser() throws Exception {
//    }

    private User createTestUser() {
        return new User.UserBuilder()
                .setUserName("test")
                .setAge("21")
                .setGender("Male")
                .setComment("Strong as a bull")
                .setPortrait(BitmapUtils.readBitmap(InstrumentationRegistry.getTargetContext(), R.mipmap.ic_launcher))
                .create();
    }

}