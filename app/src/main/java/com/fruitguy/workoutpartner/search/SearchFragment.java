package com.fruitguy.workoutpartner.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.constant.FirebaseConstant;
import com.fruitguy.workoutpartner.data.User;
import com.fruitguy.workoutpartner.user.UserActivity;
import com.fruitguy.workoutpartner.util.ImageUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnEditorAction;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_USER_ID;

/**
 * Created by heliao on 2/6/18.
 */

public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();

    @BindView(R.id.user_list)
    RecyclerView mUserList;

    @BindView(R.id.age_edit_text)
    EditText mAgeEditText;

    @BindView(R.id.weight_edit_text)
    EditText mWeightEditText;

    @BindView(R.id.gender_switch)
    SwitchCompat mGenderSwitch;

    private DatabaseReference mUserDatabase;
    private FirebaseRecyclerAdapter<User, UserViewHolder> mFirebaseRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);
        setupRecyclerView();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFirebaseRecyclerAdapter.stopListening();
    }

    private void setupRecyclerView() {
        mUserList.setLayoutManager(new LinearLayoutManager(getContext()));
        mUserList.setHasFixedSize(false);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child(FirebaseConstant.USER_DATABASE);
        FirebaseRecyclerOptions options = constructOption(mUserDatabase);
        attachRecyclerAdapter(options);

    }

    @OnEditorAction(R.id.age_edit_text)
    public boolean onAgeEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            String age = v.getText().toString();
            if(!TextUtils.isEmpty(age)) {
                Query query = constructQuery(FirebaseConstant.USER_AGE, Integer.valueOf(age));
                FirebaseRecyclerOptions option = constructOption(query);
                attachRecyclerAdapter(option);
            }
        }
        return false;
    }

    @OnEditorAction(R.id.weight_edit_text)
    public boolean onWeightEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            String weight = v.getText().toString();
            if(!TextUtils.isEmpty(weight)) {
                Query query = constructQuery(FirebaseConstant.USER_WEIGHT, Integer.valueOf(weight));
                FirebaseRecyclerOptions option = constructOption(query);
                attachRecyclerAdapter(option);
            }
        }
        return false;
    }

    @OnCheckedChanged(R.id.gender_switch)
    public void onSubscribeSwitchCheckedChange(CompoundButton buttonView, boolean isChecked) {
        String gender = "male";
        if(isChecked) {
            gender = "female";
        }
        Query query = constructQuery(FirebaseConstant.USER_GENDER, gender);
        FirebaseRecyclerOptions option = constructOption(query);
        attachRecyclerAdapter(option);

    }

    private Query constructQuery(@FirebaseConstant.DatabaseField String child
    , int value) {
        Query query = FirebaseDatabase.getInstance().getReference().child(FirebaseConstant.USER_DATABASE);
            query = query
                    .orderByChild(child)
                    .endAt(value);
        return query;
    }

    private Query constructQuery(@FirebaseConstant.DatabaseField String child
            , String value) {
        Query query = FirebaseDatabase.getInstance().getReference().child(FirebaseConstant.USER_DATABASE);
        query = query
                .orderByChild(child)
                .equalTo(value);
        return query;
    }

    private FirebaseRecyclerOptions constructOption(Query query) {
        return new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
    }

    private void attachRecyclerAdapter(FirebaseRecyclerOptions options) {
        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
                holder.setImage(model.getThumbNail());
                holder.setUserName(model.getName());
                holder.setUserStatus(model.getStatus());

                String userId = getRef(position).getKey();
                holder.mView.setOnClickListener(v ->{
                    Intent intent = new Intent(getContext(), UserActivity.class);
                    intent.putExtra(FRIEND_USER_ID, userId);
                    startActivity(intent);
                });

            }

            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
                return new UserViewHolder(view);
            }
        };
        mFirebaseRecyclerAdapter.startListening();
        mUserList.setAdapter(mFirebaseRecyclerAdapter);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        @BindView(R.id.user_image)
        CircleImageView mUserImage;

        @BindView(R.id.user_name)
        TextView mUserName;

        @BindView(R.id.user_status)
        TextView mUserStatus;

        String userId;

        public UserViewHolder(View itemView) {
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
