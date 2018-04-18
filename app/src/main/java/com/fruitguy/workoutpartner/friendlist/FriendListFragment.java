package com.fruitguy.workoutpartner.friendlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.constant.FirebaseConstant;
import com.fruitguy.workoutpartner.data.Friend;
import com.fruitguy.workoutpartner.data.User;
import com.fruitguy.workoutpartner.search.SearchFragment;
import com.fruitguy.workoutpartner.user.UserActivity;
import com.fruitguy.workoutpartner.util.ImageUtils;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

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

    FirebaseRecyclerAdapter<Friend, FriendViewHolder> mFirebaseRecyclerAdapter;

    DatabaseReference mUserDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this, rootView);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child(USER_DATABASE);
        mUserDatabase.keepSynced(true);
        setupRecyclerView();
        return rootView;
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
        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friend, FriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendViewHolder holder, int position, @NonNull Friend model) {
                String key = getRef(position).getKey();
                mUserDatabase.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child(USER_NAME).getValue().toString();
                        String thumb = dataSnapshot.child(USER_THUMB_NAIL).getValue().toString();
                        String status = dataSnapshot.child(USER_STATUS).getValue().toString();
                        holder.setImage(thumb);
                        holder.setUserName(name);
                        holder.setUserStatus(status);
                        holder.mView.setOnClickListener(view -> {
                            Intent intent = new Intent(getContext(), UserActivity.class);
                            intent.putExtra(FRIEND_USER_ID, key);
                            startActivity(intent);
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @NonNull
            @Override
            public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
                return new FriendViewHolder(view);
            }
        };

        mFirebaseRecyclerAdapter.startListening();
        mFriendList.setAdapter(mFirebaseRecyclerAdapter);
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        @BindView(R.id.user_image)
        CircleImageView mUserImage;

        @BindView(R.id.user_name)
        TextView mUserName;

        @BindView(R.id.user_status)
        TextView mUserStatus;

        String userId;

        public FriendViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, mView);
        }

        public void setImage(String url) {
            ImageUtils.loadImage(getActivity(), url, mUserImage);
        }

        public void setUserName(String name) {
            mUserName.setText(name);
        }

        public void setUserStatus(String status) {
            mUserStatus.setText(status);
        }
    }
}
