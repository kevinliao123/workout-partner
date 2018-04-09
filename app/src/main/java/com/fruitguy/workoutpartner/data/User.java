package com.fruitguy.workoutpartner.data;

/**
 * Created by heliao on 10/31/17.
 */

public class User {

    String name;
    String gender;
    long age;
    long weight;
    String status;
    String image;
    String thumbNail;

    public String getName() {
        return name;
    }

    public long getAge() {
        return age;
    }

    public long getWeight() {
        return weight;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public String getThumbNail() {
        return thumbNail;
    }


    public String getGender() {
        return gender;
    }

    public User(String userName,
                String gender,
                long age,
                long weight,
                String status,
                String image,
                String thumbNail) {
        name = userName;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.status = status;
        this.image = image;
        this.thumbNail = thumbNail;
    }

    public User() {

    }



    public static class UserBuilder {
        String mUserName;
        String mGender;
        long mAge;
        long mWeight;
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

        public UserBuilder setAge(long age) {
            mAge = age;
            return this;
        }

        public UserBuilder setWeight(long weight) {
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
