package com.fruitguy.workoutpartner.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.data.User;
import com.fruitguy.workoutpartner.util.PermissionUtil;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by heliao on 2/21/18.
 */

public class ProfileActivity extends DaggerAppCompatActivity implements ProfileContract.View {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int SELECT_IMAGE = 1000;
    public static final int CAMERA = 1001;

    @BindView(R.id.profile_activity_layout)
    RelativeLayout mRootView;

    @BindView(R.id.profile_image)
    CircleImageView mProfileImage;

    @BindView(R.id.user_name)
    TextView mUserName;

    @BindView(R.id.user_status)
    TextView mUserStatus;

    @BindView(R.id.user_age)
    EditText mUserAge;

    @BindView(R.id.user_weight)
    EditText mUserWeight;

    @BindView(R.id.gender)
    Spinner mUserGenderSpinner;


    @Inject
    ProfileContract.Presenter mProfilePresenter;
    private ArrayAdapter<CharSequence> mSpinnerAdapter;
    private ProgressDialog mProgessDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setupGenderSpinner();
        mProfilePresenter.takeView(this);
        mProfilePresenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProfilePresenter.takeView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mProfilePresenter.dropView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == SELECT_IMAGE) {
            Uri image = data.getData();
            UCrop.of(image, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cropped_image.png")))
                    .withAspectRatio(16, 16)
                    .withMaxResultSize(500, 500)
                    .start(this);
        }

        mProfilePresenter.handleCropImageResult(requestCode, resultCode, data);
        mProfilePresenter.handleCameraImageResult(requestCode, resultCode, data);
    }


    private void setupGenderSpinner() {
        mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserGenderSpinner.setAdapter(mSpinnerAdapter);
        mUserGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUserGenderSpinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {

    }

    @OnClick(R.id.update_profile_button)
    public void onUpdateProfileButtonClicked(View view) {
        mProfilePresenter.updateUserProfileInBackend(prepareUserData());
    }

    private User prepareUserData() {
        return new User.UserBuilder()
                .setUserName(mUserName.getText().toString())
                .setAge(mUserAge.getText().toString())
                .setWeight(mUserWeight.getText().toString())
                .setGender(mUserGenderSpinner.getSelectedItem().toString())
                .setStatus(mUserStatus.getText().toString())
                .create();
    }

    @OnClick(R.id.profile_image)
    public void onProfileImageClicked(View view) {
        showPictureDialog();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    List<String> permissionList = PermissionUtil.checkPermissions(ProfileActivity.this);
                    if (permissionList.size() > 0) {
                        PermissionUtil.requestPermissions(ProfileActivity.this, permissionList);
                    }
                    switch (which) {
                        case 0:
                            choosePhotoFromGallery();
                            break;
                        case 1:
                            takePhotoFromCamera();
                            break;
                    }
                }
        );
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, SELECT_IMAGE);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void updateProfileUi(User user) {
        Picasso.with(this).load(user.getImage()).into(mProfileImage);
        mUserName.setText(user.getUserName());
        mUserAge.setText(user.getAge());
        mUserWeight.setText(user.getWeight());
        mUserStatus.setText(user.getStatus());
        int position = mSpinnerAdapter.getPosition(user.getGender());
        mUserGenderSpinner.setSelection(position);
    }

    @Override
    public void showProgressDialog() {
        mProgessDialog = new ProgressDialog(this);
        mProgessDialog.setTitle("Uploading....");
        mProgessDialog.setMessage("Please wait while we upload");
        mProgessDialog.setCanceledOnTouchOutside(false);
        mProgessDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        mProgessDialog.dismiss();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mRootView, message, Snackbar.LENGTH_LONG).show();
    }
}
