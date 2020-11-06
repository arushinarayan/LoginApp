package com.arushiappdev.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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


public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button btnlogin;
    TextView signuptext, forgotpwd;
    FirebaseAuth mFireAuth;
    ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username= findViewById(R.id.loginusernametext);
        password= findViewById(R.id.loginpasswordtext);
        btnlogin= findViewById(R.id.loginbtn);
        signuptext= findViewById(R.id.loginsignuptext);
        forgotpwd= findViewById(R.id.forgotpasswordtext);
        progressbar= findViewById(R.id.progressBar);
        mFireAuth= FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernm= username.getText().toString().trim();
                String pwd= password.getText().toString().trim();

                if (TextUtils.isEmpty(pwd)){
                    password.setError("Password is required");
                    return;
                }
                if (TextUtils.isEmpty(usernm)){
                    username.setError("Email is required");
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);

                //authenticate user
                mFireAuth.signInWithEmailAndPassword(usernm, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "User Logged In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }else{
                            Toast.makeText(MainActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });

     signuptext.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             startActivity(new Intent(getApplicationContext(), SignupActivity.class));
         }
     });

     forgotpwd.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             final EditText resetMail = new EditText(v.getContext());
             AlertDialog.Builder pwdresetdialog = new AlertDialog.Builder(v.getContext());
             pwdresetdialog.setTitle("Reset Password?");
             pwdresetdialog.setMessage("Enter your email to receive reset link");
             pwdresetdialog.setView(resetMail);
             pwdresetdialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                    //extract email and send link

                     String mail = resetMail.getText().toString();
                     mFireAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {
                             Toast.makeText(MainActivity.this, "Reset Link Sent", Toast.LENGTH_SHORT).show();
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(MainActivity.this, "Error Occured!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                         }
                     });

                 }
             });
             pwdresetdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     //Close the dialog

                 }
             });

             pwdresetdialog.create().show();

         }
     });

    }
}








