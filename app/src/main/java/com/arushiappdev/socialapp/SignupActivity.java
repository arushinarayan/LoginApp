package com.arushiappdev.socialapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText emailid, pwd, name, age, ph;
    Button btnsignup;
    TextView logintext;
    ProgressBar progb;
    FirebaseAuth mFireAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailid= findViewById(R.id.signupemailtext);
        pwd= findViewById(R.id.signuppasswordtext);
        name= findViewById(R.id.signupnametext);
        age= findViewById(R.id.signupagetext);
        ph= findViewById(R.id.signupnumtext);
        btnsignup= findViewById(R.id.signupbtn);
        logintext= findViewById(R.id.signuplogintext);
        progb= findViewById(R.id.progbar);
        mFireAuth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();



        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String email= emailid.getText().toString();
                String pswd= pwd.getText().toString().trim();
                final String nm= name.getText().toString();
                final String ag= age.getText().toString();
                final String phonenum= ph.getText().toString();

                if (TextUtils.isEmpty(pswd)){
                    pwd.setError("Password is required");
                    return;
                }

                if (pswd.length() <6){
                    pwd.setError("Password should be 6 or more characters");
                    return;
                }
                if (TextUtils.isEmpty(nm)){
                    name.setError("Name is required");
                    return;
                }
                if (TextUtils.isEmpty(ag)){
                    age.setError("Age is required");
                    return;
                }
                if (TextUtils.isEmpty(phonenum)){
                    ph.setError("Phone number is required");
                    return;
                }
                if (phonenum.length() != 10){
                    ph.setError("Phone number is invalid");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    emailid.setError("Email is required");
                    return;
                }

                progb.setVisibility(View.VISIBLE);

                //Register user Firebase
                mFireAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                        Toast.makeText(SignupActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();

                        userID=mFireAuth.getCurrentUser().getUid();
                        DocumentReference documentReference= fstore.collection("users").document(userID);
                        Map<String,Object> user= new HashMap<>();
                        user.put("Name", nm);
                        user.put("Email", email);
                        user.put("Phone Number", phonenum);
                        user.put("Age", ag);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "User profile is created for "+ userID);
                            }
                        });

                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }else {
                        Toast.makeText(SignupActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progb.setVisibility(View.GONE);
                    }
                    }
                });



            }
        });

        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}




