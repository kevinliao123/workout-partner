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

/**
 * Created by heliao on 2/28/18.
 */

public class FirebaseRepository implements ValueEventListener {

    private static final String TAG = FirebaseRepository.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUserDataBase;
    private StorageReference mImageStorage;
    private FirebaseUser mUser;
    private DataChangeCallBack mDataChangeCallback;

    FirebaseRepository(FirebaseAuth firebaseAuth
            , DatabaseReference database
            , StorageReference storage
    ) {
        mFirebaseAuth = firebaseAuth;
        mUserDataBase = database;
        mImageStorage = storage;
    }

    public void init(DataChangeCallBack callBack) {
        mUserDataBase = getUserDatabase();
        mImageStorage = getImageStorage();
        mUser = getCurrentUser();
        mUserDataBase.addValueEventListener(this);
        mDataChangeCallback = callBack;
    }

    public FirebaseUser getCurrentUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    public DatabaseReference getUserDatabase() {
        String userId = getCurrentUser().getUid();
        return mUserDataBase.child(FirebaseConstant.USER_DATABASE).child(userId);
    }

    public StorageReference getImageStorage() {
        return mImageStorage.child(PROFILE_IMAGE_STORAGE);
    }

    public void updateUserProfile(Map<String, String> userMap, UploadCallBack callBack) {
        mUserDataBase.setValue(userMap)
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
                updateImageUrl(imageUrl);
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
                    updateImageUrl(downloadUrl.toString());
                    callBack.onSuccess();
                });
    }

    public void updateImageUrl(String imageUrl) {
        mUserDataBase.child(FirebaseConstant.USER_IMAGE).setValue(imageUrl)
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
        mUserDataBase.child(child).setValue(value);
    }

    public void updateUserProfileInfo(String child, long value) {
        mUserDataBase.child(child).setValue(value);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.i(TAG, "onDataChange: ");
        User user = generateUserInfo(dataSnapshot);
        mDataChangeCallback.onDataChange(user);
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

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public interface UploadCallBack {
        void onSuccess();

        void onFailure();
    }

    public interface DataChangeCallBack<T> {
        void onDataChange(T data);
    }
}
