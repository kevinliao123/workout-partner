package com.fruitguy.workoutpartner.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.chat.ChatActivity;
import com.fruitguy.workoutpartner.constant.FirebaseConstant;
import com.fruitguy.workoutpartner.data.Conversation;
import com.fruitguy.workoutpartner.search.UserViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_IMAGE;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_NAME;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_USER_ID;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.MESSAGES_NODE;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.MESSAGE_KEY;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.USER_DATABASE;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.USER_NAME;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.USER_THUMB_NAIL;

/**
 * Created by heliao on 1/15/18.
 */

public class ConvFragment extends Fragment implements ConvContract.View {

    @Inject
    ConvContract.Presenter mPresenter;

    DatabaseReference mUserDatabase;
    DatabaseReference mMessageDatabse;
    FirebaseRecyclerAdapter<Conversation, UserViewHolder> mFirebaseRecyclerAdapter;

    @BindView(R.id.conv_list)
    RecyclerView mConversationList;

    String mCurrentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_conv, container, false);
        ButterKnife.bind(this, root);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child(USER_DATABASE);
        mMessageDatabse = FirebaseDatabase.getInstance().getReference().child(MESSAGES_NODE);
        mUserDatabase.keepSynced(true);
        setupRecyclerView();

        return root;
    }

    @Override
    public void setPresenter(ConvContract.Presenter presenter) {

    }

    private void setupRecyclerView() {
        //conversation
        mConversationList.setLayoutManager(new LinearLayoutManager(getContext()));
        mConversationList.setHasFixedSize(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mCurrentUserId = user.getUid();
        DatabaseReference chatList = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstant.CHAT)
                .child(mCurrentUserId);
        chatList.keepSynced(true);
        FirebaseRecyclerOptions options = constructOption(chatList);
        attachRecyclerAdapter(options);

        // last chat message
        mMessageDatabse = mMessageDatabse.child(mCurrentUserId);
    }

    private FirebaseRecyclerOptions constructOption(DatabaseReference databaseReference) {
        return new FirebaseRecyclerOptions.Builder<Conversation>()
                .setQuery(databaseReference, Conversation.class)
                .build();
    }

    private void attachRecyclerAdapter(FirebaseRecyclerOptions options) {
        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Conversation, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Conversation model) {
                String key = getRef(position).getKey();
                mUserDatabase.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child(USER_NAME).getValue().toString();
                        String thumb = dataSnapshot.child(USER_THUMB_NAIL).getValue().toString();
                        holder.setImage(getContext(), thumb);
                        holder.setUserName(name);
                        holder.mView.setOnClickListener(view -> {
                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                            chatIntent.putExtra(FRIEND_USER_ID, key);
                            chatIntent.putExtra(FRIEND_NAME, name);
                            chatIntent.putExtra(FRIEND_IMAGE, thumb);
                            startActivity(chatIntent);

                        });

                        retrieveLastMessage(key, holder);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
                return new UserViewHolder(view);
            }
        };

        mFirebaseRecyclerAdapter.startListening();
        mConversationList.setAdapter(mFirebaseRecyclerAdapter);
    }

    private void retrieveLastMessage(String userId, UserViewHolder holder) {
        mMessageDatabse.child(userId).limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String message = dataSnapshot.child(MESSAGE_KEY).getValue().toString();
                holder.setUserStatus(message);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
