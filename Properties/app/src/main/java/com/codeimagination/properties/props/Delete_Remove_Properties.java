package com.codeimagination.properties.props;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeimagination.properties.Dashboard;
import com.codeimagination.properties.Home;
import com.codeimagination.properties.Profile;
import com.codeimagination.properties.R;
import com.codeimagination.properties.adapters.RecyclerViewAdapter_Create_Property;
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

public class Delete_Remove_Properties extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    private CurvedBottomNavigationView mView;
    private VectorMasterView heartVector;
    private VectorMasterView heartVector1;
    private VectorMasterView heartVector2;
    private float mYVal;
    private RelativeLayout mlinId;
    PathModel outline;

    Animation smallToBig, nothingtocome,from_right, button_animation;

    ImageView profile_Image, profile_Image_background, planBenefits, menu_retail_plan, back_retail_plan;

    TextView propertyName_tv , property_make_tv , property_state_tv , property_owner_tv , property_price_tv, property_description_tv ,bed_count_tv ,
            bath_count_tv, parking_count_tv, property_seller_username, property_date_added;

    private CardView buy;

    Button button_location, button_request, button_profile;

    private FirebaseUser user;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference, databaseSelling, databaseCreated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete__remove__properties);

        Intent intent = getIntent();

        final String userName = intent.getStringExtra(RecyclerViewAdapter_Create_Property.KEY_NAME);
        final String property_UniqueKey = intent.getStringExtra(RecyclerViewAdapter_Create_Property.UNIQUE_KEY);
        final String profileUrl = intent.getStringExtra(RecyclerViewAdapter_Create_Property.KEY_USERNAME);

        smallToBig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        nothingtocome = AnimationUtils.loadAnimation(this, R.anim.nothingtocome);
        button_animation = AnimationUtils.loadAnimation(this, R.anim.button_animation);
        from_right = AnimationUtils.loadAnimation(this, R.anim.nothingtocome_right);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        String user_unique = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Selling_Created_Property");

        databaseSelling = firebaseDatabase.getReference("Selling_Created_Property").child(property_UniqueKey);

        databaseCreated = FirebaseDatabase.getInstance().getReference().child("Created_Property_Model").child(user_unique).child(property_UniqueKey);

        //init textview


        propertyName_tv  = findViewById(R.id.propertyName_tv);
        property_make_tv = findViewById(R.id.property_make_tv);
        property_state_tv = findViewById(R.id.property_state_tv);
        property_owner_tv = findViewById(R.id.property_owner_tv);
        property_price_tv = findViewById(R.id.property_price_tv);
        property_description_tv = findViewById(R.id.property_description_tv);
        bed_count_tv = findViewById(R.id.bed_count_tv);
        bath_count_tv = findViewById(R.id.bath_count_tv);

        parking_count_tv = findViewById(R.id.parking_count_tv);
        property_seller_username = findViewById(R.id.property_seller_username);
        property_date_added = findViewById(R.id.property_date_added);

        buy = findViewById(R.id.buy);


        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseCreated.removeValue();

                databaseReference.removeValue();

                Intent intent1 = new Intent(Delete_Remove_Properties.this, Delete_Remove_Properties.class); //ACTIVITY_NUM = 0
                startActivity(intent1);

                Toast.makeText(Delete_Remove_Properties.this, "Property Removed", Toast.LENGTH_SHORT).show();

            }
        });

        //button_request = findViewById(R.id.request_drugs);
        //button_profile = findViewById(R.id.view_Subscription);
//        back_retail_plan = findViewById(R.id.profileback);
//        menu_retail_plan= findViewById(R.id.editProfile);
//
//        menu_retail_plan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //dialogBarrier();
//            }
//        });
//
//        back_retail_plan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Property_Details.this
//                        , Home.class);
//                intent.addFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
//            }
//        });



        mView = findViewById(R.id.customBottomBar);
        heartVector = findViewById(R.id.fab);
        heartVector1 = findViewById(R.id.fab1);
        heartVector2 = findViewById(R.id.fab2);

        mlinId = findViewById(R.id.lin_id);
        mView.inflateMenu(R.menu.bottom_menu);
        mView.setSelectedItemId(R.id.action_home);

        mView.setOnNavigationItemSelectedListener(Delete_Remove_Properties.this);

        profile_Image = findViewById(R.id.profileImageUrl);

        profile_Image_background = findViewById(R.id.coverImageUrl
        );

        profile_Image.startAnimation(smallToBig);

        //getting user information using user email by Querying database
        Query query = databaseReference.orderByChild("property_uniqueKey").equalTo(property_UniqueKey);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //checking for user data
                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    //get data
                    String property_username = ""+ds.child("property_username").getValue();
                    String property_name = ""+ds.child("property_name").getValue();
                    String username_uniqueKey = ""+ds.child("username_uniqueKey").getValue();
                    String property_uniqueKey = ""+ds.child("property_uniqueKey").getValue();
                    String property_timesCreated = ""+ds.child("property_timesCreated").getValue();
                    String profileImageUrl = ""+ds.child("profileImageUrl").getValue();
                    String property_make = ""+ds.child("property_make").getValue();

                    String property_email = ""+ds.child("property_email").getValue();
                    String property_phone_number = ""+ds.child("property_phone_number").getValue();
                    String bath_count_property = ""+ds.child("bath_count_property").getValue();
                    String parking_count_property = ""+ds.child("parking_count_property").getValue();

                    String bed_count_property = ""+ds.child("bed_count_property").getValue();
                    String property_state = ""+ds.child("property_state").getValue();
                    String property_description = ""+ds.child("property_description").getValue();
                    String property_price = ""+ds.child("property_price").getValue();
                    String property_owner = ""+ds.child("property_owner").getValue();
                    String coverImageUrl = ""+ds.child("coverImageUrl").getValue();

                    //setting data to views
                    //init textview


                    propertyName_tv.setText(property_name);
                    property_make_tv.setText(property_make);
                    property_state_tv.setText(property_state);

                    property_owner_tv.setText(property_owner);
                    property_price_tv.setText(property_price);
                    property_description_tv.setText(property_description);
                    bed_count_tv.setText(bed_count_property);
                    bath_count_tv.setText(bath_count_property);
                    parking_count_tv.setText(parking_count_property);
                    property_seller_username.setText(property_username);
                    property_date_added.setText(property_timesCreated);



                    try{

                        //check if there is image in url and set
                        Picasso.get().load(profileImageUrl).into(profile_Image);
//                        Picasso.get().load(coverImageUrl).into(profile_Image_background);



                    }catch (Exception e){

                        //if no image set default image
                        Picasso.get().load(R.drawable.profile_image).into(profile_Image);
//                        Picasso.get().load(R.drawable.profile_background).into(profile_Image_background);

                    }

                    try{

                        //check if there is image in url and set
//                        Picasso.get().load(profileImageUrl).into(profile_Image);
                        Picasso.get().load(coverImageUrl).into(profile_Image_background);



                    }catch (Exception e){

                        //if no image set default image
//                        Picasso.get().load(R.drawable.profile_image).into(profile_Image);
                        Picasso.get().load(R.drawable.profile_background).into(profile_Image_background);

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

                Intent intent_1 = new Intent(Delete_Remove_Properties.this, Home.class);
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
                Intent intent_2 = new Intent(Delete_Remove_Properties.this, Dashboard.class);
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


                Intent intent_3 = new Intent(Delete_Remove_Properties.this, Profile.class);
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
