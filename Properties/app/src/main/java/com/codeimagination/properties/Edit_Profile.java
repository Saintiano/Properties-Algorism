package com.codeimagination.properties;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codeimagination.properties.utils.CurvedBottomNavigationView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Edit_Profile extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private CardView cardTop,contraceptives,cardLeft,maternity_fitness, schedule_contraceptive;

    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    //DECLARE THE FIELDS

    private FirebaseUser mCurrentUser;

    //FIREBASE AUTHENTICATION FIELDS
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    //FIREBASE DATABASE FIELDS
    DatabaseReference mUserDatabse;
    StorageReference mStorageRef;

    //IMAGE HOLD URI
    Uri imageHoldUri = null;

    //PROGRESS DIALOG
    ProgressDialog mProgress;


    private static final int CHOOSE_IMAGE = 1;


    private CurvedBottomNavigationView mView;
    private VectorMasterView heartVector;
    private VectorMasterView heartVector1;
    private VectorMasterView heartVector2;
    private float mYVal;
    private RelativeLayout mlinId;
    PathModel outline;

    private ImageView settings, sideProfile, previousReminder, sideDashboard, notifications, sideHome;

    ImageView open_gallary, profile_image, background_image;

    Button button_location;

    private TextView textview, textview2, profile_user_name_tv;

    private Button btn_update_profile;

    private Spinner editProfile_sex, editProfile_maritalStatus, genoType, bloodGroup;

    private EditText userName, full_name_editProfile, user_name_editProfile, profession_occupation_editProfile,
            state_editProfile, house_address_editProfile, email_editProfile, mobile_number_editProfile, date_of_birth_editProfile;

    //for opening gallery
    static int PReqCode = 2;
    private int REQUESTCODE = 1;
    private Uri pickedImageUri;
    private Uri pickedImageBackgroundUri;

    //update user profile details
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseRef;
    private String userId;
    private FirebaseStorage firebaseStorage;
    private final int PICK_IMAGE = 123;
    private StorageReference storageReference;
    private StorageReference user_profile_image;
    private StorageReference profile_background;
    Uri profileImageDownloadUrl;
    Uri ProfileBackgroundImageDownloadUrl;
    Uri pickedImage;

    private ProgressBar progressBar;
    private TextView pleaseWait;

    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);




        mView = findViewById(R.id.customBottomBar);
        heartVector = findViewById(R.id.fab);
        heartVector1 = findViewById(R.id.fab1);
        heartVector2 = findViewById(R.id.fab2);

        mlinId = findViewById(R.id.lin_id);
        mView.inflateMenu(R.menu.bottom_menu);
        mView.setSelectedItemId(R.id.action_profile);

        mView.setOnNavigationItemSelectedListener(Edit_Profile.this);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        //ASSIGN INSTANCE TO FIREBASE AUTH
        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User_Profiles_Model");

        //FIREBASE DATABASE INSTANCE
        mUserDatabse = FirebaseDatabase.getInstance().getReference().child("User_Profiles_Model").child(mAuth.getCurrentUser().getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //image init
        open_gallary = findViewById(R.id.profile_add_image);
        profile_image = findViewById(R.id.profile_image);
        background_image = findViewById(R.id.profile_background);

        pleaseWait = (TextView) findViewById(R.id.please_wait_profile);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_profile);
        mProgress = new ProgressDialog(this);

        //Spinner init
        editProfile_sex = findViewById(R.id.editProfile_sex);

        profile_user_name_tv = findViewById(R.id.profile_user_name_tv);

        //EditText
        full_name_editProfile = findViewById(R.id.full_name_editProfile);
        profession_occupation_editProfile = findViewById(R.id.profession_occupation_editProfile);
        state_editProfile = findViewById(R.id.state_editProfile);
        house_address_editProfile = findViewById(R.id.house_address_editProfile);
        email_editProfile = findViewById(R.id.email_editProfile);
        mobile_number_editProfile = findViewById(R.id.mobile_number_editProfile);
        user_name_editProfile = findViewById(R.id.user_name_editProfile);

        //button init
        btn_update_profile = findViewById(R.id.btn_update_profile);

        //hiding progress bar and please wait text field at start
        progressBar.setVisibility(View.GONE);
        pleaseWait.setVisibility(View.GONE);



        //ONCLICK SAVE PROFILE BTN
        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //LOGIC FOR SAVING USER PROFILE
                saveUserProfile();

            }
        });

        //USER IMAGEVIEW ONCLICK LISTENER
        open_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermission();

                }else{

                    profilePicSelection();

                }



            }
        });


        //getting user information using user email by Querying database
        Query query = databaseReference.orderByChild("email_address").equalTo(user.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //checking for user data
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //get data
                    String fullName = ""+ds.child("fullname").getValue();
                    String houseAddress = ""+ds.child("office_address").getValue();
                    String phoneNumber = ""+ds.child("phone_number").getValue();
                    String state = ""+ds.child("state").getValue();
                    String sex = ""+ds.child("sex").getValue();
                    String profession = ""+ds.child("occupation").getValue();
                    String userId = ""+ds.child("user_unique_id").getValue();

                    String email = ""+ds.child("email_address").getValue();
                    String username = ""+ds.child("username").getValue();
                    String profileImageUrl = ""+ds.child("profileImageUrl").getValue();
                    String coverImageUrl = ""+ds.child("coverImageUrl").getValue();

                    //setting data to edittext views
                    //init textview
                    full_name_editProfile.setText(fullName);
                    profession_occupation_editProfile.setText(profession);
                    email_editProfile.setText(email);
                    mobile_number_editProfile.setText(phoneNumber);
                    state_editProfile.setText(state);
                    house_address_editProfile.setText(houseAddress);
                    user_name_editProfile.setText(username);

                    profile_user_name_tv.setText(fullName);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void profilePicSelection() {


        //DISPLAY DIALOG TO CHOOSE CAMERA OR GALLERY

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Profile.this);
        builder.setTitle("Add Photo!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void cameraIntent() {

        //CHOOSE CAMERA
        Log.d("gola", "entered here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        //CHOOSE IMAGE FROM GALLERY
        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //SAVE URI FROM GALLERY
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            //SAVE URI FROM CAMERA

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }


        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                profile_image.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


    private void saveUserProfile() {

        final String username = user_name_editProfile.getText().toString();
        final String fullName = full_name_editProfile.getText().toString();
        final String sex = editProfile_sex.getSelectedItem().toString();
        final String profession = profession_occupation_editProfile.getText().toString();
        final String state = state_editProfile.getText().toString();
        final String email = user.getEmail();
        final String phoneNumber = mobile_number_editProfile.getText().toString();
        final String houseAddress = house_address_editProfile.getText().toString();




        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(fullName)) {

            if (imageHoldUri != null) {

                mProgress.setTitle("Saveing Profile");
                mProgress.setMessage("Please wait....");
                mProgress.show();

                String current_userUd = mCurrentUser.getUid();
                String profilePicUrl = imageHoldUri.getLastPathSegment();

                final StorageReference mChildStorage = mStorageRef.child("User_Profiles_Model").child(mAuth.getCurrentUser().getUid());

                uploadTask = mChildStorage.putFile(imageHoldUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return mChildStorage.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            mUserDatabse.child("username").setValue(username);
                            mUserDatabse.child("office_address").setValue(houseAddress);
                            mUserDatabse.child("fullname").setValue(fullName);
                            mUserDatabse.child("occupation").setValue(profession);
                            mUserDatabse.child("email_address").setValue(user.getEmail());
                            mUserDatabse.child("phone_number").setValue(phoneNumber);
                            mUserDatabse.child("sex").setValue(sex);
                            mUserDatabse.child("state").setValue(state);

                            mUserDatabse.child("user_unique_id").setValue(mAuth.getCurrentUser().getUid());
                            mUserDatabse.child("profileImageUrl").setValue(downloadUri.toString());

                            mProgress.dismiss();

                            finish();
                            Intent moveToHome = new Intent(Edit_Profile.this, Profile.class);
                            moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(moveToHome);


                        } else {
                            // Handle failures
                            // ...

                        }
                    }
                });








            } else {

                Toast.makeText(Edit_Profile.this, "Please select a profile picture", Toast.LENGTH_LONG).show();

            }

        } else {

            Toast.makeText(Edit_Profile.this, "Please enter Username and Full Name", Toast.LENGTH_LONG).show();

        }

    }


    //grant permission to pick image
    private void checkAndRequestForPermission() {

        if (!(ContextCompat.checkSelfPermission(Edit_Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Edit_Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(Edit_Profile.this, "Please accept to grant storage permission", Toast.LENGTH_SHORT).show();

            }else{

                ActivityCompat.requestPermissions(Edit_Profile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);

            }

        }else{

            profilePicSelection();

        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                tet(6);
                // find the correct path using name
                mlinId.setX(mView.mFirstCurveControlPoint1.x );
                heartVector.setVisibility(View.VISIBLE);
                heartVector1.setVisibility(View.GONE);
                heartVector2.setVisibility(View.GONE);
                selectAnimation(heartVector);

                Intent intent_1 = new Intent(Edit_Profile.this,Home.class);
                startActivity(intent_1);
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_dashboard:
                tet(2);
                mlinId.setX(mView.mFirstCurveControlPoint1.x );
                heartVector.setVisibility(View.GONE);
                heartVector1.setVisibility(View.VISIBLE);
                heartVector2.setVisibility(View.GONE);

                selectAnimation(heartVector1);
                Intent intent_2 = new Intent(Edit_Profile.this,Dashboard.class);
                startActivity(intent_2);
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_profile:
                tet();
                mlinId.setX(mView.mFirstCurveControlPoint1.x ) ;
                heartVector.setVisibility(View.GONE);
                heartVector1.setVisibility(View.GONE);
                heartVector2.setVisibility(View.VISIBLE);

                selectAnimation(heartVector2);


                Intent intent_3 = new Intent(Edit_Profile.this,Profile.class);
                startActivity(intent_3);
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    private void selectAnimation(final VectorMasterView heartVector) {

        outline = heartVector.getPathModelByName("outline");
        outline.setStrokeColor(Color.parseColor("#ffffff"));
        outline.setTrimPathEnd(0.0f);
        // initialise valueAnimator and pass start and end float values
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // set trim end value and update view
                outline.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                heartVector.update();
            }
        });
        valueAnimator.start();
    }

    private void tet(int i) {

        // get width and height of navigation bar
        // Navigation bar bounds (width & height)
        //mNavigationBarHeight = getHeight();
        //mNavigationBarWidth = getWidth();
        // the coordinates (x,y) of the start point before curve
        mView.mFirstCurveStartPoint.set((mView.mNavigationBarWidth / i) - (mView.CURVE_CIRCLE_RADIUS * 2) - (mView.CURVE_CIRCLE_RADIUS / 3), 0);
        // the coordinates (x,y) of the end point after curve
        mView.mFirstCurveEndPoint.set(mView.mNavigationBarWidth / i, mView.CURVE_CIRCLE_RADIUS + (mView.CURVE_CIRCLE_RADIUS / 4));
        // same thing for the second curve
        mView.mSecondCurveStartPoint = mView.mFirstCurveEndPoint;
        mView.mSecondCurveEndPoint.set((mView.mNavigationBarWidth / i) + (mView.CURVE_CIRCLE_RADIUS * 2) + (mView.CURVE_CIRCLE_RADIUS / 3), 0);

        // the coordinates (x,y)  of the 1st control point on a cubic curve
        mView.mFirstCurveControlPoint1.set(mView.mFirstCurveStartPoint.x + mView.CURVE_CIRCLE_RADIUS + (mView.CURVE_CIRCLE_RADIUS / 4), mView.mFirstCurveStartPoint.y);
        // the coordinates (x,y)  of the 2nd control point on a cubic curve
        mView.mFirstCurveControlPoint2.set(mView.mFirstCurveEndPoint.x - (mView.CURVE_CIRCLE_RADIUS * 2) + mView.CURVE_CIRCLE_RADIUS, mView.mFirstCurveEndPoint.y);

        mView.mSecondCurveControlPoint1.set(mView.mSecondCurveStartPoint.x + (mView.CURVE_CIRCLE_RADIUS * 2) - mView.CURVE_CIRCLE_RADIUS, mView.mSecondCurveStartPoint.y);
        mView.mSecondCurveControlPoint2.set(mView.mSecondCurveEndPoint.x - (mView.CURVE_CIRCLE_RADIUS + (mView.CURVE_CIRCLE_RADIUS / 4)), mView.mSecondCurveEndPoint.y);



    }

    private void tet() {

        // get width and height of navigation bar
        // Navigation bar bounds (width & height)
        //mNavigationBarHeight = getHeight();
        //mNavigationBarWidth = getWidth();
        // the coordinates (x,y) of the start point before curve
        mView.mFirstCurveStartPoint.set((mView.mNavigationBarWidth * 10/12) - (mView.CURVE_CIRCLE_RADIUS * 2) - (mView.CURVE_CIRCLE_RADIUS / 3), 0);
        // the coordinates (x,y) of the end point after curve
        mView.mFirstCurveEndPoint.set(mView.mNavigationBarWidth  * 10/12, mView.CURVE_CIRCLE_RADIUS + (mView.CURVE_CIRCLE_RADIUS / 4));
        // same thing for the second curve
        mView.mSecondCurveStartPoint = mView.mFirstCurveEndPoint;
        mView.mSecondCurveEndPoint.set((mView.mNavigationBarWidth  * 10/12) + (mView.CURVE_CIRCLE_RADIUS * 2) + (mView.CURVE_CIRCLE_RADIUS / 3), 0);

        // the coordinates (x,y)  of the 1st control point on a cubic curve
        mView.mFirstCurveControlPoint1.set(mView.mFirstCurveStartPoint.x + mView.CURVE_CIRCLE_RADIUS + (mView.CURVE_CIRCLE_RADIUS / 4), mView.mFirstCurveStartPoint.y);
        // the coordinates (x,y)  of the 2nd control point on a cubic curve
        mView.mFirstCurveControlPoint2.set(mView.mFirstCurveEndPoint.x - (mView.CURVE_CIRCLE_RADIUS * 2) + mView.CURVE_CIRCLE_RADIUS, mView.mFirstCurveEndPoint.y);

        mView.mSecondCurveControlPoint1.set(mView.mSecondCurveStartPoint.x + (mView.CURVE_CIRCLE_RADIUS * 2) - mView.CURVE_CIRCLE_RADIUS, mView.mSecondCurveStartPoint.y);
        mView.mSecondCurveControlPoint2.set(mView.mSecondCurveEndPoint.x - (mView.CURVE_CIRCLE_RADIUS + (mView.CURVE_CIRCLE_RADIUS / 4)), mView.mSecondCurveEndPoint.y);
    }


}