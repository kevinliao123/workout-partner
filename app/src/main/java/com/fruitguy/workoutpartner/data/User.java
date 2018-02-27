package com.fruitguy.workoutpartner.data;

import android.graphics.Bitmap;

/**
 * Created by heliao on 10/31/17.
 */

public class User {

    String mUserName;
    String mGender;
    String mAge;
    String mWeight;
    String mStatus;
    String mImage;
    String mThumbNail;

    public String getUserName() {
        return mUserName;
    }

    public String getAge() {
        return mAge;
    }

    public String getWeight() {
        return mWeight;
    }
    public String getStatus() {
        return mStatus;
    }


    public String getImage() {
        return mImage;
    }

    public String getThumbNail() {
        return mThumbNail;
    }


    public String getGender() {
        return mGender;
    }

    public User(String userName,
                String gender,
                String age,
                String weight,
                String status,
                String image,
                String thumbNail) {
        mUserName = userName;
        mGender = gender;
        mAge = age;
        mWeight = weight;
        mStatus = status;
        mImage = image;
        mThumbNail = thumbNail;
    }



    public static class UserBuilder {
        String mUserName;
        String mGender;
        String mAge;
        String mWeight;
        String mStatus;
        String mImage;
        String mThumbNail;

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

        public UserBuilder setWeight(String weight) {
            mWeight = weight;
            return this;
        }

        public UserBuilder setStatus(String status) {
            mStatus = status;
            return this;
        }

        public UserBuilder setImage(String image) {
            mImage = image;
            return this;
        }

        public UserBuilder setThumbNail(String thumbNail) {
            mThumbNail = thumbNail;
            return this;
        }

        public User create() {
            return new User(mUserName, mGender, mAge, mWeight,mStatus, mImage, mThumbNail);
        }
    }
}
