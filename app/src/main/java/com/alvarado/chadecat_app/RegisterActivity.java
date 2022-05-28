package com.alvarado.chadecat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
    TextView tvName, tvEmail, tvPassword;
    Spinner spModel;
    Button btnRegister, btnExit;
    FirebaseAuth fAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        tvName = findViewById(R.id.email_register);
        tvEmail = findViewById(R.id.email);
        tvPassword = findViewById(R.id.password_register);
        btnRegister = findViewById(R.id.register3);
        btnExit = findViewById(R.id.exit);

        fAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tvName.getText().toString().trim();
                String email = tvEmail.getText().toString().trim();
                String password = tvPassword.getText().toString().trim();
                //String model = spModel.getSelectedItem().toString();

                if(name.length() == 0){
                    tvName.setError("This field can not be blank");
                }
                if(email.length() == 0){
                    tvEmail.setError("This field can not be blank");
                } else if(!email.contains("@")){
                    tvEmail.setError("The email is incorrect");
                }
                if(password.length() == 0){
                    tvPassword.setError("This field can not be blank");
                }else if(password.length() <= 5){
                    tvPassword.setError("The password must contain at least 6 characters");
                }

                if(email.length() != 0 && password.length() != 0 && name.length() != 0){
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                                AddUser(name, email);

                                finish();

                            } else {
                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(RegisterActivity.this);
                                dlgAlert.setMessage("The credentials are incorrect, please try again");
                                dlgAlert.setTitle("Error Message...");
                                dlgAlert.setPositiveButton("OK", null);
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which){

                                    }
                                });

                            }
                        }
                    });
                }

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void AddUser(String name, String email){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
       // user.put("model", model);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
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