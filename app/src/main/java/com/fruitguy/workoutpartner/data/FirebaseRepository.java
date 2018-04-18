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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.*;

/**
 * Created by heliao on 2/28/18.
 */

public class FirebaseRepository {

    private static final String TAG = FirebaseRepository.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mCurrentUserDataBase;
    private DatabaseReference mFriendRequestDataBase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;
    private StorageReference mImageStorage;
    private FirebaseUser mUser;
    private ValueEventListener mRequestStateListener;
    private ValueEventListener mFriendStateListener;
    private ValueEventListener mUserInfoListener;
    private String mFriendUserId;

    FirebaseRepository(FirebaseAuth firebaseAuth
            , DatabaseReference database
            , StorageReference storage
    ) {
        mFirebaseAuth = firebaseAuth;
        mDatabase = database;
        mImageStorage = storage;
    }

    public void init() {
        mUser = getCurrentUser();
        mCurrentUserDataBase = getUserDatabaseById(mUser.getUid());
        mImageStorage = getImageStorage();
        mFriendRequestDataBase = getFriendRequestDatabase();
        mFriendDatabase = getFriendsDatabase();
        mNotificationDatabase = getNotificationDatabase();
    }

    public void shutdown() {
        String currentUserId = mUser.getUid();
        if (mRequestStateListener != null) {
            mFriendRequestDataBase.child(currentUserId).removeEventListener(mRequestStateListener);
        }

        if (mFriendStateListener != null) {
            mFriendDatabase.child(currentUserId).removeEventListener(mFriendStateListener);
        }

        if (mUserInfoListener != null) {
            mCurrentUserDataBase.removeEventListener(mUserInfoListener);
            getUserDatabaseById(mFriendUserId).removeEventListener(mUserInfoListener);
            if (mFriendUserId != null) {
                mDatabase.child(mFriendUserId).removeEventListener(mUserInfoListener);
            }
        }

        mRequestStateListener = null;
        mFriendStateListener = null;
        mUserInfoListener = null;
    }

    public ValueEventListener getUserInfoListener(DataChangeCallBack<User> callback) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = generateUserInfo(dataSnapshot);
                callback.onDataChange(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        };
    }

    public FirebaseUser getCurrentUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    public DatabaseReference getUserDatabaseById(String id) {
        DatabaseReference database = mDatabase.child(USER_DATABASE).child(id);
        database.keepSynced(true);
        return database;
    }

    public void getCurrentUserInfo(DataChangeCallBack callback) {
        String userId = mUser.getUid();
        getSelectedUserInfoById(userId, callback);
    }

    public void getSelectedUserInfoById(String id, DataChangeCallBack callBack) {
        DatabaseReference userDatabase = getUserDatabaseById(id);
        mUserInfoListener = getUserInfoListener(callBack);
        userDatabase.addValueEventListener(mUserInfoListener);
    }

    private User generateUserInfo(DataSnapshot dataSnapshot) {
        return new User.UserBuilder()
                .setUserName(dataSnapshot.child(USER_NAME).getValue().toString())
                .setAge((long) dataSnapshot.child(USER_AGE).getValue())
                .setWeight((long) dataSnapshot.child(USER_WEIGHT).getValue())
                .setGender(dataSnapshot.child(USER_GENDER).getValue().toString())
                .setStatus(dataSnapshot.child(USER_STATUS).getValue().toString())
                .setImage(dataSnapshot.child(USER_IMAGE).getValue().toString())
                .setThumbNail(dataSnapshot.child(USER_THUMB_NAIL).getValue().toString())
                .create();
    }

    public StorageReference getImageStorage() {
        return mImageStorage.child(PROFILE_IMAGE_STORAGE);
    }

    public DatabaseReference getFriendRequestDatabase() {
        return mDatabase.child(FRIEND_REQUEST);
    }

    public DatabaseReference getNotificationDatabase() {
        return mDatabase.child(NOTIFICATION);
    }

    public DatabaseReference getFriendsDatabase() {
        return mDatabase.child(FRIENDS);
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
                updateImageUrl(imageUrl, USER_IMAGE);
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
                    updateImageUrl(downloadUrl.toString(), USER_IMAGE);
                    callBack.onSuccess();
                });
    }

    public void uploadThumbNail(byte[] data, UploadCallBack callBack) {
        UploadTask uploadTask = mImageStorage.child("thumb").child(mUser.getUid() + ".png").putBytes(data);
        uploadTask.addOnFailureListener((exception) -> Log.e(TAG, exception.getMessage()))
                .addOnSuccessListener((taskSnapshot) -> {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    updateImageUrl(downloadUrl.toString(), USER_THUMB_NAIL);
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
        updateUserProfileInfo(USER_NAME, user.getName());
        updateUserProfileInfo(USER_AGE, user.getAge());
        updateUserProfileInfo(USER_WEIGHT, user.getWeight());
        updateUserProfileInfo(USER_GENDER, user.getGender());
        updateUserProfileInfo(USER_STATUS, user.getStatus());
    }

    public void updateUserProfileInfo(String child, String value) {
        mCurrentUserDataBase.child(child).setValue(value);
    }

    public void updateUserProfileInfo(String child, long value) {
        mCurrentUserDataBase.child(child).setValue(value);
    }

    public void addFriend(String friendUserId, UploadCallBack callback) {
        String currentUserId = mUser.getUid();
        mFriendUserId = friendUserId;
        String currentDate = DateFormat.getDateTimeInstance().format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put(currentUserId + "/" + friendUserId +"/"+ DATE, currentDate);
        map.put(friendUserId + "/" + currentUserId +"/"+ DATE, currentDate);
        mFriendDatabase.updateChildren(map, (databaseError, databaseReference) -> {
            if(databaseError == null) {
                callback.onSuccess();
            } else {
                callback.onFailure();
                Log.e(TAG, "addFriend: " + databaseError.getMessage() );
            }
        });
    }

    public void unfriend(String friendUserId, UploadCallBack callback) {
        String currentUserId = mUser.getUid();
        mFriendUserId = friendUserId;
        Map<String, Object> map = new HashMap<>();
        map.put(currentUserId + "/" + friendUserId, null);
        map.put(friendUserId + "/" + currentUserId, null);
        mFriendDatabase.updateChildren(map, (databaseError, databaseReference) -> {
            if(databaseError == null) {
                callback.onSuccess();
            } else {
                callback.onFailure();
                Log.e(TAG, "addFriend: " + databaseError.getMessage() );
            }
        });
    }

    public void retrieveRequestState(String friendUserId, FriendRequestCallback callback) {
        String currentUserId = mUser.getUid();
        mFriendUserId = friendUserId;
        mRequestStateListener = getRequestStateListener(friendUserId, callback);
        mFriendStateListener = getFriendStateListener(friendUserId, callback);
        mFriendRequestDataBase.child(currentUserId).addListenerForSingleValueEvent(mRequestStateListener);
        mFriendDatabase.child(currentUserId).addListenerForSingleValueEvent(mFriendStateListener);
    }

    private ValueEventListener getRequestStateListener(String friendUserId, FriendRequestCallback callback) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(friendUserId)) {
                    String requestType = dataSnapshot.child(friendUserId).child(REQUEST_TYPE).getValue().toString();
                    if (requestType.equals(RECEIVED)) {
                        callback.onReceived();
                    } else if (requestType.equals(SENT)) {
                        callback.onSent();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        };
    }

    private ValueEventListener getFriendStateListener(String friendUserId, FriendRequestCallback callback) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(friendUserId)) {
                    callback.onFriend();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        };
    }

    public void sentFriendRequest(String friendUserId, UploadCallBack callBack) {
        String currentUserId = mUser.getUid();
        mFriendUserId = friendUserId;
        Map<String, Object> map = new HashMap<>();
        map.put(currentUserId
                    +"/"+ friendUserId
                    +"/"+ REQUEST_TYPE
                , SENT);

        map.put(friendUserId
                        +"/"+ currentUserId
                        +"/"+ REQUEST_TYPE
                , RECEIVED);

        mFriendRequestDataBase.updateChildren(map,(databaseError, databaseReference)  -> {
            if(databaseError != null) {
                Log.e(TAG, "sentFriendRequest: " + databaseError.getMessage());
                callBack.onFailure();
                return;
            }

            sendNotification(friendUserId);
            callBack.onSuccess();

        });
    }

    public void removeFriendRequest(String friendUserId, UploadCallBack callBack) {

        String currentUserId = mUser.getUid();
        mFriendUserId = friendUserId;
        Map<String, Object> map = new HashMap<>();
        map.put(currentUserId
                        +"/"+ friendUserId
                        +"/"+ REQUEST_TYPE
                , null);

        map.put(friendUserId
                        +"/"+ currentUserId
                        +"/"+ REQUEST_TYPE
                , null);

        mFriendRequestDataBase.updateChildren(map,(databaseError, databaseReference)  -> {
            if(databaseError != null) {
                Log.e(TAG, "sentFriendRequest: " + databaseError.getMessage());
                callBack.onFailure();
                return;
            }
            callBack.onSuccess();

        });
    }

    public void sendNotification(String friendUserId) {
        Map<String, String> map = new HashMap<>();
        map.put("from", getCurrentUser().getUid());
        map.put("type", NOTIFICATION_TYPE);

        mNotificationDatabase.child(friendUserId).push().setValue(map);
    }

    public interface UploadCallBack {
        void onSuccess();

        void onFailure();
    }

    public interface DataChangeCallBack<T> {
        void onDataChange(T data);
    }

    public interface FriendRequestCallback {
        void onReceived();

        void onSent();

        void onFriend();
    }
}
