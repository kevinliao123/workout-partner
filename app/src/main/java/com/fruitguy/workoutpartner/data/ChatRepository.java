package com.fruitguy.workoutpartner.data;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.*;

public class ChatRepository extends FirebaseRepository {

    private static final String TAG = ChatRepository.class.getSimpleName();

    protected DatabaseReference mChatRef;
    protected DatabaseReference mMessageRef;
    private String mCurrentUserId;
    private ValueEventListener mChatEventListener;

    public ChatRepository(FirebaseAuth firebaseAuth
            , DatabaseReference database
            , StorageReference storage) {
        super(firebaseAuth, database, storage);
        mChatRef = database.child(CHAT);
        mMessageRef = database.child(MESSAGES_NODE);
        mCurrentUserId = mUser.getUid();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        if(mChatEventListener != null) {
            mChatRef.child(mCurrentUserId).removeEventListener(mChatEventListener);
        }
        mChatEventListener = null;
    }

    public void startChat(String friendUserId, UploadCallBack callback) {
        mChatEventListener = getChatEventListener(friendUserId);
        mChatRef.child(mCurrentUserId).addValueEventListener(mChatEventListener);
    }

    public ValueEventListener getChatEventListener(String friendUserId) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(friendUserId)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(mCurrentUserId + "/" + friendUserId + "/" + TIMESTAMP, ServerValue.TIMESTAMP);
                    map.put(friendUserId + "/" + mCurrentUserId + "/" + TIMESTAMP, ServerValue.TIMESTAMP);

                    mChatRef.updateChildren(map, (databaseError, databaseReference) -> {
                        if(databaseError != null) {
                            Log.e(TAG, "onDataChange: " + databaseError.getMessage() );
                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public void sendMessage(String message, String friendUserId, UploadCallBack callback) {
        Map messageMap = generateTextMessageMap(message);
        String pushId = getChatMessagePushId(friendUserId);
        uploadMessage(friendUserId, pushId, messageMap, callback);
    }

    public void sendMessage(Uri image, String friendUserId, UploadCallBack callback) {
        Map messageMap = generateImageMessageMap(image);
        String pushId = getChatMessagePushId(friendUserId);
        uploadMessage(friendUserId, pushId, messageMap, callback);
    }

    public void uploadMessage(String friendUserId, String pushId, Map data, UploadCallBack callback) {
        String currentUserId = mUser.getUid();
        String currentUserRef = currentUserId + "/"+ friendUserId;
        String friendUserRef =  friendUserId + "/"+ currentUserId;
        Map userMap = new HashMap();
        userMap.put(currentUserRef + "/" + pushId, data);
        userMap.put(friendUserRef + "/" + pushId, data);

        mMessageRef.updateChildren(userMap, (databaseError, databaseReference) ->{
            if(databaseError != null) {
                Log.e(TAG, "sendMessage: " + databaseError.getMessage());
                callback.onFailure();
            } else {
                callback.onSuccess();
            }
        });
    }

    private Map generateTextMessageMap(String message) {
        Map messageMap = new HashMap();
        messageMap.put(MESSAGE_KEY, message);
        messageMap.put(MESSAGE_TYPE, TEXT);
        messageMap.put(TIMESTAMP, ServerValue.TIMESTAMP);
        messageMap.put(FROM, mUser.getUid());

        return messageMap;
    }

    private Map generateImageMessageMap(Uri image) {
        Map messageMap = new HashMap();
        messageMap.put(MESSAGE_KEY, image.toString());
        messageMap.put(MESSAGE_TYPE, IMAGE);
        messageMap.put(TIMESTAMP, ServerValue.TIMESTAMP);
        messageMap.put(FROM, mUser.getUid());

        return messageMap;
    }

    public String getChatMessagePushId(String friendUserId) {
        String currentUserId = mUser.getUid();
        return mMessageRef.child(currentUserId).child(friendUserId).push().getKey();
    }


}
