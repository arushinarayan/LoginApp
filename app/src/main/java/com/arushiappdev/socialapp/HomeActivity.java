package com.arushiappdev.socialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class HomeActivity extends AppCompatActivity {
    TextView nametext, emailtext, phtext;
    FirebaseAuth mFireAuth;
    FirebaseFirestore fstore;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnsignout;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nametext=findViewById(R.id.namedisp);
        emailtext=findViewById(R.id.emaildisp);
        phtext=findViewById(R.id.phonedisp);
        mFireAuth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        userId= mFireAuth.getCurrentUser().getUid();
        DocumentReference documentReference= fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                nametext.setText(documentSnapshot.getString("Name"));
                emailtext.setText(documentSnapshot.getString("Email"));
                phtext.setText(documentSnapshot.getString("Phone Number"));

            }
        });


        btnsignout= findViewById(R.id.signoutbutton);
        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
}
