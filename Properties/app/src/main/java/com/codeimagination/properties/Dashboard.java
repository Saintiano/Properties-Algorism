package com.codeimagination.properties;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.codeimagination.properties.Interface.IFirestoreLoadDone;
import com.codeimagination.properties.adapters.Dashboard_Slider_Adapter;
import com.codeimagination.properties.adapters.RecyclerViewAdapter_Create_Property;
import com.codeimagination.properties.adapters.RecyclerViewAdapter_Create_Property_Horizontal;
import com.codeimagination.properties.home_activities.Create_Property;
import com.codeimagination.properties.home_activities.View_Deleted_Properties;
import com.codeimagination.properties.home_activities.View_Property;
import com.codeimagination.properties.models.CreateProperty_Model;
import com.codeimagination.properties.models.Dashboard_Slider_Items;
import com.codeimagination.properties.props.Update_Properties;
import com.codeimagination.properties.utils.CurvedBottomNavigationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karan.churi.PermissionManager.PermissionManager;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, IFirestoreLoadDone {

    private CardView cardTop,contraceptives,cardLeft,maternity_fitness, schedule_contraceptive;

    private ImageView settings, sideProfile, previousReminder, sideDashboard, notifications, sideHome;

    private CurvedBottomNavigationView mView;
    private VectorMasterView heartVector;
    private VectorMasterView heartVector1;
    private VectorMasterView heartVector2;
    private float mYVal;
    private RelativeLayout mlinId;
    PathModel outline;

    TextView removedProperty, updateProperty, createPropery;

    //for cover flow
    DatabaseReference dbRef;
    AlertDialog dialog;
    FeatureCoverFlow featureCoverFlow;
    TextSwitcher title;
    Dashboard_Slider_Adapter dashboardSliderAdapter;

    ViewPager viewPager;

    IFirestoreLoadDone iFirestoreLoadDone;

    CollectionReference dashboard_slider;

    PermissionManager permissionManager;

    //for alert to wait for the slider loading
    AlertDialog dialogSpot;

    ImageView back_arrow, menu_retail;

    DatabaseReference databaseReference;

    RecyclerView recyclerViewAppointment;

    ArrayList<CreateProperty_Model> createProperty_models;

    RecyclerViewAdapter_Create_Property adapterCreateProperty;

    RecyclerViewAdapter_Create_Property_Horizontal adapter_create_property_horizontal;

    RecyclerView seller_recyclerview_linearlayout,seller_recyclerview_horizontal;

    //update user profile details
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;

    private DatabaseReference databaseRef;
    private String userId;
    private FirebaseStorage firebaseStorage;
    private final int PICK_IMAGE = 123;
    private StorageReference storageReference;
    private StorageReference user_profile_image;
    private StorageReference profile_background;
    Uri profileImageDownloadUrl;
    Uri ProfileBackgroundImageDownloadUrl;


    private Handler handler;
    private int delay = 5000; //milliseconds

    private int page = 0;

    Runnable runnable = new Runnable() {
        public void run() {
            if (dashboardSliderAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        permissionManager = new PermissionManager(){};
        permissionManager.checkAndRequestPermissions(this);

        //init
        iFirestoreLoadDone = this;

        dashboard_slider = FirebaseFirestore.getInstance().collection("Dashboard_slider");

        dialogSpot = new SpotsDialog.Builder().setContext(this).build();

        //init viewpager
        viewPager = (ViewPager)findViewById(R.id.view_pager_dashboard);

//        getData();

        // card ini
//        schedule_contraceptive = findViewById(R.id.schedule_contraceptive);
//        cardTop = findViewById(R.id.cardTop);
//        contraceptives = findViewById(R.id.contraceptives);
//        maternity_fitness = findViewById(R.id.maternity_fitness) ;

        // ini Animations

        Animation animeBottomToTop = AnimationUtils.loadAnimation(this,R.anim.anime_bottom_to_top);
        Animation animeTopToBottom = AnimationUtils.loadAnimation(this,R.anim.anime_top_to_bottom);
        Animation animeRightToleft = AnimationUtils.loadAnimation(this,R.anim.anime_right_to_left);
        Animation animeLeftToRight = AnimationUtils.loadAnimation(this,R.anim.anime_left_to_right);


        // setup Animation :
//        maternity_fitness.setAnimation(animeBottomToTop);
//        cardTop.setAnimation(animeTopToBottom);
//        contraceptives.setAnimation(animeRightToleft);
//        schedule_contraceptive.setAnimation(animeLeftToRight);

        mView = findViewById(R.id.customBottomBar);
        heartVector = findViewById(R.id.fab);
        heartVector1 = findViewById(R.id.fab1);
        heartVector2 = findViewById(R.id.fab2);

        mlinId = findViewById(R.id.lin_id);
        mView.inflateMenu(R.menu.bottom_menu);
        mView.setSelectedItemId(R.id.action_dashboard);

//        // get our folding cell
//        final FoldingCell fc = (FoldingCell) findViewById(R.id.folding_cell);
//
//        // attach click listener to folding cell
//        fc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fc.toggle(false);
//            }
//        });

        //getting bottom navigation view and attaching the listener
        //BottomNavigationView navigation = findViewById(R.id.customBottomBar);
        mView.setOnNavigationItemSelectedListener(Dashboard.this);



        //side card view
        settings = findViewById(R.id.img_drugs);
        sideProfile = findViewById(R.id.img_profile);
        previousReminder = findViewById(R.id.img_drugHistory);

        updateProperty = findViewById(R.id.sideMenu);
        createPropery = findViewById(R.id.createPropery);
        removedProperty = findViewById(R.id.updateProperties);



        //side intent
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(Dashboard.this, Edit_Profile.class);
                startActivity(intent_1);
            }
        });


        sideProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(Dashboard.this, Profile.class);
                startActivity(intent_1);
            }
        });


        previousReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(Dashboard.this, View_Property.class);
                startActivity(intent_1);
            }
        });

        removedProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(Dashboard.this, View_Deleted_Properties.class);
                startActivity(intent_1);
            }
        });

        createPropery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(Dashboard.this, Create_Property.class);
                startActivity(intent_1);
            }
        });


        updateProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1 = new Intent(Dashboard.this, Update_Properties.class);
                startActivity(intent_1);
            }
        });


        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        userId = user.getUid();

        FirebaseApp.initializeApp(this);

        //getting data into recyclerview
        seller_recyclerview_linearlayout = findViewById(R.id.propertie_vertical_recyclerview);
        seller_recyclerview_linearlayout.setLayoutManager(new LinearLayoutManager(this));
        createProperty_models = new ArrayList<CreateProperty_Model>();

        //get data from firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Selling_Created_Property");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if data is gotten
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    CreateProperty_Model admin_changeProviders = dataSnapshot1.getValue(CreateProperty_Model.class);
                    createProperty_models.add(admin_changeProviders);

                }

                adapterCreateProperty = new RecyclerViewAdapter_Create_Property(Dashboard.this, createProperty_models);
                seller_recyclerview_linearlayout.setAdapter(adapterCreateProperty);
                adapterCreateProperty.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //if data is cancelled
                Toast.makeText(Dashboard.this, "No Data", Toast.LENGTH_SHORT).show();

            }
        });



        //getting data into recyclerview horizontal view
        seller_recyclerview_horizontal = findViewById(R.id.propertie_horizontal_recyclerview);
        seller_recyclerview_horizontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        createProperty_models = new ArrayList<CreateProperty_Model>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if data is gotten
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    CreateProperty_Model admin_changeProviders = dataSnapshot1.getValue(CreateProperty_Model.class);
                    createProperty_models.add(admin_changeProviders);

                }

                adapter_create_property_horizontal = new RecyclerViewAdapter_Create_Property_Horizontal(Dashboard.this, createProperty_models);
                seller_recyclerview_horizontal.setAdapter(adapter_create_property_horizontal);
                adapter_create_property_horizontal.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //if data is cancelled
                Toast.makeText(Dashboard.this, "No Data", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void getData() {

        dialogSpot.show();

        dashboard_slider.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iFirestoreLoadDone.onFireStoreLoadFailed(e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    //get data from our slider model
                    List<Dashboard_Slider_Items> dashboard_slider = new ArrayList<>();

                    for (QueryDocumentSnapshot dashboardSnapShot: task.getResult()){

                        Dashboard_Slider_Items dashboard_slider_items = dashboardSnapShot.toObject(Dashboard_Slider_Items.class);

                        dashboard_slider.add(dashboard_slider_items);

                    }

                    iFirestoreLoadDone.onFireStoreLoadSuccess(dashboard_slider);


                }



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

                Intent intent_1 = new Intent(Dashboard.this,Home.class);
                startActivity(intent_1);
                Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_dashboard:
                tet(2);
                mlinId.setX(mView.mFirstCurveControlPoint1.x );
                heartVector.setVisibility(View.GONE);
                heartVector1.setVisibility(View.GONE);
                heartVector2.setVisibility(View.GONE);

                selectAnimation(heartVector1);
                Intent intent_2 = new Intent(Dashboard.this,Dashboard.class);
                startActivity(intent_2);
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_profile:
                tet();
                mlinId.setX(mView.mFirstCurveControlPoint1.x ) ;
                heartVector.setVisibility(View.GONE);
                heartVector1.setVisibility(View.GONE);
                heartVector2.setVisibility(View.GONE);

                selectAnimation(heartVector2);


                Intent intent_3 = new Intent(Dashboard.this,Profile.class);
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


    @Override
    public void onFireStoreLoadSuccess(List<Dashboard_Slider_Items> dashboard_slider_items) {
        dashboardSliderAdapter = new Dashboard_Slider_Adapter(this, dashboard_slider_items);
        viewPager.setAdapter(dashboardSliderAdapter);

        dialogSpot.dismiss();
    }

    @Override
    public void onFireStoreLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        dialogSpot.dismiss();

    }



}