package com.codeimagination.properties;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeimagination.properties.utils.CurvedBottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private CurvedBottomNavigationView mView;
    private VectorMasterView heartVector;
    private VectorMasterView heartVector1;
    private VectorMasterView heartVector2;
    private float mYVal;
    private RelativeLayout mlinId;
    PathModel outline;

    Animation smallToBig, nothingtocome,from_right, button_animation;

    ImageView profile_Image, profile_Image_background, planBenefits, menu_retail_plan, back_retail_plan;

    TextView profile_fullname, profile_username, profile_tv_username, profile_sex, profile_marital_status, profile_occupation, profile_email, profile_mobile_phone, profile_state, profile_address, profile_date_of_birth;


    Button button_location, button_request, button_profile;

    private FirebaseUser user;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        mView = findViewById(R.id.customBottomBar);
        heartVector = findViewById(R.id.fab);
        heartVector1 = findViewById(R.id.fab1);
        heartVector2 = findViewById(R.id.fab2);

        mlinId = findViewById(R.id.lin_id);
        mView.inflateMenu(R.menu.bottom_menu);
        mView.setSelectedItemId(R.id.action_profile);

        mView.setOnNavigationItemSelectedListener(Profile.this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User_Profiles_Model");


        smallToBig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        nothingtocome = AnimationUtils.loadAnimation(this, R.anim.nothingtocome);
        button_animation = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        from_right = AnimationUtils.loadAnimation(this, R.anim.nothingtocome_right);

        //init textview
        profile_fullname  = findViewById(R.id.profile_tv_name);
        profile_tv_username = findViewById(R.id.profile_tv_username);
        profile_sex = findViewById(R.id.profile_sex);
        profile_occupation = findViewById(R.id.profile_work);
        profile_email = findViewById(R.id.profile_email);
        profile_mobile_phone = findViewById(R.id.profile_phone_number);
        profile_state = findViewById(R.id.profile_tv_state_address);
        profile_address = findViewById(R.id.profile_address);

        //button_request = findViewById(R.id.request_drugs);
        //button_profile = findViewById(R.id.view_Subscription);
        back_retail_plan = findViewById(R.id.profileback);
        menu_retail_plan= findViewById(R.id.editProfile);

        menu_retail_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogBarrier();
            }
        });

        back_retail_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this
                        , Home.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        profile_Image = findViewById(R.id.profile_image);
        profile_Image.startAnimation(smallToBig);

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
                    profile_fullname.setText(fullName);
                    profile_tv_username.setText(username);
                    profile_sex.setText(sex);

                    profile_occupation.setText(profession);
                    profile_email.setText(email);
                    profile_mobile_phone.setText(phoneNumber);
                    profile_state.setText(state);
                    profile_address.setText(houseAddress);




                    try{

                        //check if there is image in url and set
                        Picasso.get().load(profileImageUrl).into(profile_Image);
                        //Picasso.get().load(coverImageUrl).into(profile_Image_background);



                    }catch (Exception e){

                        //if no image set default image
                        Picasso.get().load(R.drawable.profile_image).into(profile_Image);
                        //Picasso.get().load(R.drawable.profile_background).into(profile_Image_background);

                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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

                Intent intent_1 = new Intent(Profile.this,Home.class);
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
                Intent intent_2 = new Intent(Profile.this,Dashboard.class);
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


                Intent intent_3 = new Intent(Profile.this,Profile.class);
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
