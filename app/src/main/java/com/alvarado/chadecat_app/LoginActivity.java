package com.alvarado.chadecat_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class LoginActivity extends AppCompatActivity {
    TextView email_log, password_log, tvname, tvemail;
    Button btnLogin, btnRegister;
    RadioButton rbSesion;
    String userId;

    private static final String STRING_PREFERENCES = "alvarado.chadecat_app";
    private static final String BUTTON_PREFERENCES = "state.button.sesion";

    boolean isActivateRadioButton;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseFirestore fStore;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        if(stateButton()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            String email = fUser.getEmail();
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        }

            email_log = findViewById(R.id.email_login);
            password_log = findViewById(R.id.password_login);
            btnLogin = findViewById(R.id.login);
            btnRegister = findViewById(R.id.button_register);
            rbSesion = findViewById(R.id.RBSesion);

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();


            isActivateRadioButton = rbSesion.isChecked();

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = email_log.getText().toString().trim();
                    String password = password_log.getText().toString().trim();

                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            saveStateButton();
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Done Login", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("email", email);

                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
            });

            rbSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isActivateRadioButton) {
                        rbSesion.setChecked(false);
                    }
                    isActivateRadioButton = rbSesion.isChecked();
                }
            });

    }

    public void saveStateButton(){
        SharedPreferences sharedPreferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(BUTTON_PREFERENCES, rbSesion.isChecked()).apply();
    }
    public boolean stateButton(){
        SharedPreferences sharedPreferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return sharedPreferences.getBoolean(BUTTON_PREFERENCES, false);
    }


}