package com.codeimagination.properties.signup_login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codeimagination.properties.Dashboard;
import com.codeimagination.properties.R;
import com.codeimagination.properties.models.User_Profiles_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;

    private Button btn_register;

    Button btn_create_account;

    private EditText userName, emailRegister, passwordRegister, confirmPasswordRegister;

    private ProgressDialog progressDialog;

    DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;

    private FirebaseUser user;

    private String user_unique_id,
            username,
            fullname,
            phone_number,
            email_address,
            office_address,
            state,
            occupation,
            sex,
            profileImageUrl,
            coverImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                fullname = "";
                phone_number = "";
                email_address= "";
                office_address= "";
                state = "";
                occupation= "";
                sex = "";
                profileImageUrl = "";
                coverImageUrl = "";


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User_Profiles_Model");




        //init views
        userName = (EditText) findViewById(R.id.signInUserName);
        emailRegister = (EditText) findViewById(R.id.signInEmail);
        passwordRegister = (EditText) findViewById(R.id.signInPassword);
        confirmPasswordRegister = (EditText) findViewById(R.id.signInConfirmPassword);

        //progress bar init
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");


        //init buttons
        btn_register = findViewById(R.id.btn_register);

        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);


//start account creation process
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                //getting filled data from edittext field
                final String email_address = emailRegister.getText().toString();
                final String username = userName.getText().toString();
                final String password = passwordRegister.getText().toString();
                final String confirmPassword = confirmPasswordRegister.getText().toString();


                //Validate inputs
                if (!Patterns.EMAIL_ADDRESS.matcher(email_address).matches()){
                    //set error message
                    emailRegister.setError("Invalid Email");
                    emailRegister.setFocusable(true);
                    progressDialog.dismiss();

                }else if (password.length() < 6){
                    //set error message and focus
                    passwordRegister.setError("Password must be more than six (6) characters");
                    passwordRegister.setFocusable(true);
                    progressDialog.dismiss();

                }else if (!password.equals(confirmPassword)){
                    //set error message and focus
                    confirmPasswordRegister.setError("Password does not match");
                    confirmPasswordRegister.setFocusable(true);
                    progressDialog.dismiss();

                }else {

                    firebaseAuth.createUserWithEmailAndPassword(email_address, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressDialog.show();

                            user = firebaseAuth.getCurrentUser();

                            user_unique_id = user.getUid();

                            if (task.isSuccessful()) {



                                //get data from model
                                User_Profiles_Model userInformationa = new User_Profiles_Model(

                                        user_unique_id,
                                        username,
                                        fullname,
                                        phone_number,
                                        email_address,
                                        office_address,
                                        state,
                                        occupation,
                                        sex,
                                        profileImageUrl,
                                        coverImageUrl
                                );

                                FirebaseDatabase.getInstance().getReference("User_Profiles_Model")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(userInformationa)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                //created user successfully
                                                Toast.makeText(Register_Activity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                                                //then update user info, add all custom parameters here
                                                Intent intent = new Intent(Register_Activity.this
                                                        , Dashboard.class);
                                                intent.addFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                startActivity(intent);
                                                progressDialog.dismiss();

                                            }
                                        });




                            }else{

                                //account failed to create
                                Toast.makeText(Register_Activity.this, "Failed to create account"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();

                            }



                        }
                    });
                }

            }
        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
