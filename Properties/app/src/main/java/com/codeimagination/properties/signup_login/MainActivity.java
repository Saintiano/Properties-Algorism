package com.codeimagination.properties.signup_login;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.codeimagination.properties.Dashboard;
import com.codeimagination.properties.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton btRegister;
    private TextView tvLogin;

    private Button btnLogin;


    ImageView imageView;

    TextView register, textview2, textview3;

    Button btn_sign_in;

    private EditText editEmail, editPassword, confirmPassword;

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.login);
        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);
        btRegister.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.etEmail);
        editPassword = findViewById(R.id.etPassword);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in User...");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get details from edit field
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                progressDialog.dismiss();

                //Validate inputs
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //set error message
                    editEmail.setError("Invalid Email");
                    editEmail.setFocusable(true);
                    progressDialog.dismiss();

                }else if (password.length() < 6){
                    //set error message and focus
                    editPassword.setError("Password must be more than six (6) characters");
                    editPassword.setFocusable(true);
                    progressDialog.dismiss();

                }else {

                    login(email, password);

                }



            }
        });





    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v==btRegister){
            Intent intent   = new Intent(MainActivity.this,Register_Activity.class);
            Pair[] pairs    = new Pair[1];
            pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
            startActivity(intent,activityOptions.toBundle());
        }
    }

    private void login(String email, String password) {

        //show progress bar
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Dismiss progress bar
                            progressDialog.dismiss();

                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getInstance().getCurrentUser();

                            if (user != null) {
                                // Name, email address, and profile photo Url
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                Uri photoUrl = user.getPhotoUrl();

                                // Check if user's email is verified
                                boolean emailVerified = user.isEmailVerified();

                                // The user's ID, unique to the Firebase project. Do NOT use this value to
                                // authenticate with your backend server, if you have one. Use
                                // FirebaseUser.getIdToken() instead.
                                String uid = user.getUid();
                            }

                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
                            startActivity(intent);

                            Animatoo.animateInAndOut(MainActivity.this);

                            finish();


                        } else {

                            //dismiss progress bar
                            progressDialog.show();

                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed."+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //dismiss progress bar
                progressDialog.show();

                //failed to login user
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }



}
