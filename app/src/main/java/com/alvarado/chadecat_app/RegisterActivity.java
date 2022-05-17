package com.alvarado.chadecat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class RegisterActivity extends AppCompatActivity {
    TextView tvName, tvEmail, tvPassword, tvConfirmPassword;
    Button btnRegister;
    FirebaseAuth fAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        tvName = findViewById(R.id.email_register);
        tvEmail = findViewById(R.id.email);
        tvPassword = findViewById(R.id.password_register);
        tvConfirmPassword = findViewById(R.id.confirm_password);
        btnRegister = findViewById(R.id.register);

        fAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tvName.getText().toString().trim();
                String email = tvEmail.getText().toString().trim();
                String password = tvPassword.getText().toString().trim();

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User Create", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                            AddUser("1", name, email);

                        }else{
                            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    public void AddUser(String id, String name, String email){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("name", name);
        user.put("email", email);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}