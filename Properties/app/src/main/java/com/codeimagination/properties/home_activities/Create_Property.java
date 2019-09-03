package com.codeimagination.properties.home_activities;

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

import com.codeimagination.properties.Edit_Profile;
import com.codeimagination.properties.utils.CurvedBottomNavigationView;
import com.codeimagination.properties.Dashboard;
import com.codeimagination.properties.Home;
import com.codeimagination.properties.Profile;
import com.codeimagination.properties.R;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Create_Property extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


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

    private Button create_property;

    private Spinner bed_count, property_state, parking_count, bath_count_property;

    private EditText property_description, userName, user_name_property, property_owner, name_property_create, date_of_create_editProfile,
            price_property_create, house_address_editProfile, email_editProfile, mobile_number_editProfile, property_make;

    //for opening gallery
    static int PReqCode = 2;
    private int REQUESTCODE = 1;
    private Uri pickedImageUri;
    private Uri pickedImageBackgroundUri;


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

    //update user profile details
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseRef;
    private String userId;
    private DatabaseReference dbRef;
    private DatabaseReference dbRefProperties;

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

    Uri downloadUri;

    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__property);

        timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        mProgress = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        //ASSIGN INSTANCE TO FIREBASE AUTH
        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        userId = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();



        mStorageRef = FirebaseStorage.getInstance().getReference();

        databaseReference = firebaseDatabase.getReference("User_Profiles_Model");

        dbRefProperties = FirebaseDatabase.getInstance().getReference().child("Selling_Created_Property").push();

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Selling_Created_Property");

        mView = findViewById(R.id.customBottomBar);
        heartVector = findViewById(R.id.fab);
        heartVector1 = findViewById(R.id.fab1);
        heartVector2 = findViewById(R.id.fab2);

        mlinId = findViewById(R.id.lin_id);
        mView.inflateMenu(R.menu.bottom_menu);
        mView.setSelectedItemId(R.id.action_home);

        mView.setOnNavigationItemSelectedListener(Create_Property.this);

        //image init
        open_gallary = findViewById(R.id.profile_add_image);
        profile_image = findViewById(R.id.property_image);
        background_image = findViewById(R.id.profile_background);


        //Spinner init
        property_state = findViewById(R.id.property_state);
        bed_count = findViewById(R.id.property_bed_count);
        parking_count = findViewById(R.id.parking_count);
        bath_count_property = findViewById(R.id.bath_count_property);

        profile_user_name_tv = findViewById(R.id.number_of_added_property);

        //EditText
        user_name_property = findViewById(R.id.user_name_property);
        name_property_create = findViewById(R.id.name_property_create);
        price_property_create = findViewById(R.id.price_property_create);
        house_address_editProfile = findViewById(R.id.office_address_property);
        email_editProfile = findViewById(R.id.email_property);
        mobile_number_editProfile = findViewById(R.id.mobile_number_property);
        date_of_create_editProfile = findViewById(R.id.date_of_birth_editProfile);
        property_make = findViewById(R.id.property_make);
        property_owner = findViewById(R.id.property_owner);

        property_description = findViewById(R.id.property_description);

        //button init
        create_property = findViewById(R.id.create_property);

        create_property.setOnClickListener(new View.OnClickListener() {
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

                    //setting data to views
                    //init textview
                    property_owner.setText(fullName);
                    user_name_property.setText(username);
                    email_editProfile.setText(email);
                    mobile_number_editProfile.setText(phoneNumber);
                    house_address_editProfile.setText(state);
                    date_of_create_editProfile.setText(timeStamp);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(Create_Property.this);
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


        final String username = user_name_property.getText().toString();
        final String propertytName = name_property_create.getText().toString();
        final String property_condition = property_state.getSelectedItem().toString();

        final String bed = bed_count.getSelectedItem().toString();
        final String bath_count = bath_count_property.getSelectedItem().toString();
        final String parking = parking_count.getSelectedItem().toString();
        final String propertDescription = property_description.getText().toString();
        final String propertyOwner = property_owner.getText().toString();
        final String propertyMake = property_make.getText().toString();
        final String dateCreated = date_of_create_editProfile.getText().toString();
        final String price = price_property_create.getText().toString();
        final String officeAddress = house_address_editProfile.getText().toString();
        final String email = email_editProfile.getText().toString();
        final String phoneNumber = mobile_number_editProfile.getText().toString();
        final String houseAddress = house_address_editProfile.getText().toString();
        final String coverImageUrl = "";




        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(propertytName)) {

            if (imageHoldUri != null) {

                final String databaseRefer = FirebaseDatabase.getInstance().getReference().child("Created_Properties").child(userId).getKey();

                final String uniqueKeyGenerated = dbRefProperties.push().getKey();



                dbRef = FirebaseDatabase.getInstance().getReference().child("Admin_Created_Property").child(userId);



                //FIREBASE DATABASE INSTANCE
                mUserDatabse = FirebaseDatabase.getInstance().getReference().child("Created_Property_Model").child(userId).child(uniqueKeyGenerated);


                mProgress.setTitle("Creating Property");
                mProgress.setMessage("Please wait....");
                mProgress.show();

                String current_userUd = mCurrentUser.getUid();
                String profilePicUrl = imageHoldUri.getLastPathSegment();

                final StorageReference mChildStorage = mStorageRef.child("Created_Property_Model").child(userId).child(uniqueKeyGenerated);

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

                            downloadUri = task.getResult();

                            final String username = user_name_property.getText().toString();
                            final String propertytName = name_property_create.getText().toString();
                            final String property_condition = property_state.getSelectedItem().toString();
                            final String bed = bed_count.getSelectedItem().toString();
                            final String bath_count = bath_count_property.getSelectedItem().toString();
                            final String parking = parking_count.getSelectedItem().toString();
                            final String propertyOwner = property_owner.getText().toString();
                            final String propertyMake = property_make.getText().toString();
                            final String dateCreated = date_of_create_editProfile.getText().toString();
                            final String price = price_property_create.getText().toString();


                            final String coverImageUrl = "";

                            mUserDatabse.child("property_username").setValue(username);
                            mUserDatabse.child("property_address").setValue(officeAddress);
                            mUserDatabse.child("property_name").setValue(propertytName);
                            mUserDatabse.child("property_make").setValue(propertyMake);
                            mUserDatabse.child("property_email").setValue(email);
                            mUserDatabse.child("property_phone_number").setValue(phoneNumber);
                            mUserDatabse.child("bath_count_property").setValue(bath_count);
                            mUserDatabse.child("parking_count_property").setValue(parking);
                            mUserDatabse.child("bed_count_property").setValue(bed);
                            mUserDatabse.child("property_state").setValue(property_condition);
                            mUserDatabse.child("property_description").setValue(propertDescription);
                            mUserDatabse.child("property_price").setValue(price);
                            mUserDatabse.child("property_owner").setValue(propertyOwner);
                            mUserDatabse.child("property_timesCreated").setValue(timeStamp);
                            mUserDatabse.child("property_uniqueKey").setValue(uniqueKeyGenerated);


                            mUserDatabse.child("coverImageUrl").setValue(coverImageUrl);
                            mUserDatabse.child("user_unique_id").setValue(mAuth.getCurrentUser().getUid());
                            mUserDatabse.child("profileImageUrl").setValue(downloadUri.toString());


                            //prepare to insert data into database
                            dbRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    dataSnapshot.getRef().child("property_username").setValue(user_name_property.getText().toString());
                                    dataSnapshot.getRef().child("property_name").setValue(name_property_create.getText().toString());
                                    dataSnapshot.getRef().child("username_uniqueKey").setValue(databaseRefer);
                                    dataSnapshot.getRef().child("property_uniqueKey").setValue(uniqueKeyGenerated);
                                    dataSnapshot.getRef().child("property_timesCreated").setValue(timeStamp);
                                    dataSnapshot.getRef().child("profileImageUrl").setValue(downloadUri.toString());

                                    finish();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                    Toast.makeText(Create_Property.this, " "+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                            dbRefProperties.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    dataSnapshot.getRef().child("property_username").setValue(user_name_property.getText().toString());
                                    dataSnapshot.getRef().child("property_name").setValue(name_property_create.getText().toString());
                                    dataSnapshot.getRef().child("username_uniqueKey").setValue(databaseRefer);
                                    dataSnapshot.getRef().child("property_uniqueKey").setValue(uniqueKeyGenerated);
                                    dataSnapshot.getRef().child("property_timesCreated").setValue(timeStamp);
                                    dataSnapshot.getRef().child("profileImageUrl").setValue(downloadUri.toString());



                                    dataSnapshot.getRef().child("property_make").setValue(propertyMake);
                                    dataSnapshot.getRef().child("property_email").setValue(email);
                                    dataSnapshot.getRef().child("property_phone_number").setValue(phoneNumber);
                                    dataSnapshot.getRef().child("bath_count_property").setValue(bath_count);
                                    dataSnapshot.getRef().child("parking_count_property").setValue(parking);
                                    dataSnapshot.getRef().child("bed_count_property").setValue(bed);
                                    dataSnapshot.getRef().child("property_state").setValue(property_condition);
                                    dataSnapshot.getRef().child("property_description").setValue(propertDescription);
                                    dataSnapshot.getRef().child("property_price").setValue(price);
                                    dataSnapshot.getRef().child("property_owner").setValue(propertyOwner);
                                    dataSnapshot.getRef().child("coverImageUrl").setValue(coverImageUrl);


                                    finish();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                    Toast.makeText(Create_Property.this, " "+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                            mProgress.dismiss();

                            Toast.makeText(Create_Property.this, "Property Created", Toast.LENGTH_SHORT).show();


                            Intent moveToHome = new Intent(Create_Property.this, View_Property.class);
                            moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(moveToHome);

                            finish();


                        } else {
                            // Handle failures
                            // ...

                        }
                    }
                });










            } else {

                Toast.makeText(Create_Property.this, "Please select a profile picture", Toast.LENGTH_LONG).show();

            }

        } else {

            Toast.makeText(Create_Property.this, "Please enter Username and Property Name", Toast.LENGTH_LONG).show();

        }

    }


    //grant permission to pick image
    private void checkAndRequestForPermission() {

        if (!(ContextCompat.checkSelfPermission(Create_Property.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Create_Property.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(Create_Property.this, "Please accept to grant storage permission", Toast.LENGTH_SHORT).show();

            }else{

                ActivityCompat.requestPermissions(Create_Property.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);

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

                Intent intent_1 = new Intent(Create_Property.this, Home.class);
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
                Intent intent_2 = new Intent(Create_Property.this, Dashboard.class);
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


                Intent intent_3 = new Intent(Create_Property.this, Profile.class);
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
