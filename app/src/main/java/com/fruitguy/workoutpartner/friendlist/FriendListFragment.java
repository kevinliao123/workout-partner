package com.fruitguy.workoutpartner.friendlist;

import android.app.AlertDialog;
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
import com.fruitguy.workoutpartner.data.Friend;
import com.fruitguy.workoutpartner.search.UserViewHolder;
import com.fruitguy.workoutpartner.user.UserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_IMAGE;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_NAME;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_USER_ID;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.USER_DATABASE;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.USER_NAME;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.USER_STATUS;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.USER_THUMB_NAIL;

/**
 * Created by heliao on 2/6/18.
 */

public class FriendListFragment extends Fragment {
    @BindView(R.id.friend_list)
    RecyclerView mFriendList;

    FirebaseRecyclerAdapter<Friend, UserViewHolder> mFirebaseRecyclerAdapter;

    DatabaseReference mUserDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this, rootView);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child(USER_DATABASE);
        mUserDatabase.keepSynced(true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mFirebaseRecyclerAdapter.stopListening();
    }

    private void setupRecyclerView() {
        mFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFriendList.setHasFixedSize(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference friendList = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstant.FRIENDS)
                .child(user.getUid());
        friendList.keepSynced(true);
        FirebaseRecyclerOptions options = constructOption(friendList);
        attachRecyclerAdapter(options);

    }

    private FirebaseRecyclerOptions constructOption(DatabaseReference databaseReference) {
        return new FirebaseRecyclerOptions.Builder<Friend>()
                .setQuery(databaseReference, Friend.class)
                .build();
    }

    private void attachRecyclerAdapter(FirebaseRecyclerOptions options) {
        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friend, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Friend model) {
                String key = getRef(position).getKey();
                mUserDatabase.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child(USER_NAME).getValue().toString();
                        String thumb = dataSnapshot.child(USER_THUMB_NAIL).getValue().toString();
                        String status = dataSnapshot.child(USER_STATUS).getValue().toString();
                        holder.setImage(getContext(), thumb);
                        holder.setUserName(name);
                        holder.setUserStatus(status);
                        holder.mView.setOnClickListener(view -> {
                            CharSequence[] option = {"Open Profile", "Send Message"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                    .setTitle("Select Options")
                                    .setItems(option, (dialog, which) -> {
                                        if (which == 0) {
                                            Intent intent = new Intent(getContext(), UserActivity.class);
                                            intent.putExtra(FRIEND_USER_ID, key);
                                            startActivity(intent);
                                        } else {
                                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                            chatIntent.putExtra(FRIEND_USER_ID, key);
                                            chatIntent.putExtra(FRIEND_NAME, name);
                                            chatIntent.putExtra(FRIEND_IMAGE, thumb);
                                            startActivity(chatIntent);
                                        }
                                    });
                            builder.create().show();
                        });
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
        mFriendList.setAdapter(mFirebaseRecyclerAdapter);
    }
}
