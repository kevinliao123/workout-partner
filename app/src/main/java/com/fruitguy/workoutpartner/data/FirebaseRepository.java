package com.fruitguy.workoutpartner.data;

import android.net.Uri;
import android.util.Log;

import com.fruitguy.workoutpartner.constant.FirebaseConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.PROFILE_IMAGE_STORAGE;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.USER_DATABASE;

/**
 * Created by heliao on 2/28/18.
 */

public class FirebaseRepository {

    private static final String TAG = FirebaseRepository.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mCurrentUserDataBase;
    private StorageReference mImageStorage;
    private FirebaseUser mUser;
    private DataChangeCallBack mDataChangeCallback;

    FirebaseRepository(FirebaseAuth firebaseAuth
            , DatabaseReference database
            , StorageReference storage
    ) {
        mFirebaseAuth = firebaseAuth;
        mDatabase = database;
        mImageStorage = storage;
    }

    public void init(DataChangeCallBack callBack) {
        mUser = getCurrentUser();
        mCurrentUserDataBase = getUserDatabaseById(mUser.getUid());
        mCurrentUserDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = generateUserInfo(dataSnapshot);
                mDataChangeCallback.onDataChange(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
        mImageStorage = getImageStorage();
        mDataChangeCallback = callBack;
    }

    public FirebaseUser getCurrentUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    public DatabaseReference getUserDatabaseById(String id) {
        return mDatabase.child(USER_DATABASE).child(id);
    }

    public void getSelectedUserInfoById(String id, DataChangeCallBack callBack) {
        DatabaseReference userDatabase = getUserDatabaseById(id);
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = generateUserInfo(dataSnapshot);
                callBack.onDataChange(user);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage() );
            }
        });
    }

    private User generateUserInfo(DataSnapshot dataSnapshot) {
        return new User.UserBuilder()
                .setUserName(dataSnapshot.child(FirebaseConstant.USER_NAME).getValue().toString())
                .setAge((long) dataSnapshot.child(FirebaseConstant.USER_AGE).getValue())
                .setWeight((long) dataSnapshot.child(FirebaseConstant.USER_WEIGHT).getValue())
                .setGender(dataSnapshot.child(FirebaseConstant.USER_GENDER).getValue().toString())
                .setStatus(dataSnapshot.child(FirebaseConstant.USER_STATUS).getValue().toString())
                .setImage(dataSnapshot.child(FirebaseConstant.USER_IMAGE).getValue().toString())
                .setThumbNail(dataSnapshot.child(FirebaseConstant.USER_THUMB_NAIL).getValue().toString())
                .create();
    }

    public StorageReference getImageStorage() {
        return mImageStorage.child(PROFILE_IMAGE_STORAGE);
    }

    public void updateUserProfile(Map<String, String> userMap, UploadCallBack callBack) {
        mCurrentUserDataBase.setValue(userMap)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        callBack.onSuccess();
                    } else {
                        callBack.onFailure();
                    }
                });
    }

    public void uploadImage(Uri resultUri, UploadCallBack callBack) {
        StorageReference filePath = mImageStorage
                .child(mUser.getUid()+".png");
        filePath.putFile(resultUri).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.i(TAG, "upload image success");
                String imageUrl = task.getResult().getDownloadUrl().toString();
                updateImageUrl(imageUrl, FirebaseConstant.USER_IMAGE);
                callBack.onSuccess();
            } else {
                Log.i(TAG, "upload image fail");
            }
        });
    }

    public void uploadImage(byte[] data, UploadCallBack callBack) {
        UploadTask uploadTask = mImageStorage.child(mUser.getUid() + ".png").putBytes(data);
        uploadTask.addOnFailureListener((exception) -> Log.e(TAG, exception.getMessage()))
                .addOnSuccessListener((taskSnapshot) -> {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    updateImageUrl(downloadUrl.toString(), FirebaseConstant.USER_IMAGE);
                    callBack.onSuccess();
                });
    }

    public void uploadThumbNail(byte[] data, UploadCallBack callBack) {
        UploadTask uploadTask = mImageStorage.child("thumb").child(mUser.getUid() + ".png").putBytes(data);
        uploadTask.addOnFailureListener((exception) -> Log.e(TAG, exception.getMessage()))
                .addOnSuccessListener((taskSnapshot) -> {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    updateImageUrl(downloadUrl.toString(), FirebaseConstant.USER_THUMB_NAIL);
                    callBack.onSuccess();
                });
    }

    public void updateImageUrl(String imageUrl, @FirebaseConstant.DatabaseField String child) {
        mCurrentUserDataBase.child(child).setValue(imageUrl)
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()) {
                       Log.i(TAG, "upload image url success");
                   } else {
                       Log.i(TAG, "upload image url fail");
                   }
                });
    }

    public void updateUserProfileInfoToBackEnd(User user) {
        updateUserProfileInfo(FirebaseConstant.USER_NAME, user.getName());
        updateUserProfileInfo(FirebaseConstant.USER_AGE, user.getAge());
        updateUserProfileInfo(FirebaseConstant.USER_WEIGHT, user.getWeight());
        updateUserProfileInfo(FirebaseConstant.USER_GENDER, user.getGender());
        updateUserProfileInfo(FirebaseConstant.USER_STATUS, user.getStatus());
    }

    public void updateUserProfileInfo(String child, String value) {
        mCurrentUserDataBase.child(child).setValue(value);
    }

    public void updateUserProfileInfo(String child, long value) {
        mCurrentUserDataBase.child(child).setValue(value);
    }

    public interface UploadCallBack {
        void onSuccess();

        void onFailure();
    }

    public interface DataChangeCallBack<T> {
        void onDataChange(T data);
    }
}
