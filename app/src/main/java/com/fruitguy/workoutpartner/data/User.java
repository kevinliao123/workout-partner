package com.fruitguy.workoutpartner.data;

import android.graphics.Bitmap;

/**
 * Created by heliao on 10/31/17.
 */

public class User {

    String mUserName;
    String mGender;
    String mAge;
    String mComment;
    Bitmap mPortrait;

    public String getUserName() {
        return mUserName;
    }

    public String getAge() {
        return mAge;
    }


    public String getComment() {
        return mComment;
    }


    public Bitmap getPortrait() {
        return mPortrait;
    }


    public String getGender() {
        return mGender;
    }

    public User(String userName,
                String gender,
                String age,
                String comment,
                Bitmap portrait) {
        mUserName = userName;
        mGender = gender;
        mAge = age;
        mComment = comment;
        mPortrait = portrait;
    }



    public static class UserBuilder {
        String mUserName;
        String mGender;
        String mAge;
        String mComment;
        Bitmap mPortrait;

        public UserBuilder setUserName(String userName) {
            mUserName = userName;
            return this;
        }

        public UserBuilder setGender(String gender) {
            mGender = gender;
            return this;
        }

        public UserBuilder setAge(String age) {
            mAge = age;
            return this;
        }

        public UserBuilder setComment(String comment) {
            mComment = comment;
            return this;
        }

        public UserBuilder setPortrait(Bitmap portrait) {
            mPortrait = portrait;
            return this;
        }

        public User create() {
            return new User(mUserName, mGender, mAge, mComment, mPortrait);
        }
    }
}
