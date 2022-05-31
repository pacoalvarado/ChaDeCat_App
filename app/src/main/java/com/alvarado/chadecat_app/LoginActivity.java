package com.alvarado.chadecat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    TextView email_log, password_log;
    Button btnLogin, btnRegister;
    RadioButton rbSesion;


    private static final String STRING_PREFERENCES = "alvarado.chadecat_app";
    private static final String BUTTON_PREFERENCES = "state.button.sesion";

    boolean isActivateRadioButton;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseFirestore fStore;

    /**
     * Al crear la vista, cridem el metode que primer ens comprova si tenim algu iniciat en la sessió (amb el sharedprefernce), si estem login entrem directa, si no haurem de fer el Login
     * @param savedInstanceState Si l'activitat s'està reinicialitzant després d'haver-se tancat prèviament, aquest paquet conté les dades que ha subministrat més recentment a onSaveInstanceState(Bundle).
     */
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

                        //Comprovem si el email esta buit.
                        if(email.length() == 0){
                            email_log.setError("This field can not be blank");
                        }
                        //Comprovem la contrassenya.
                        if(password.length() == 0){
                            password_log.setError("This field can not be blank");
                        }

                        if(email.length() != 0 && password.length() != 0){
                            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    saveStateButton();
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("email", email);

                                        startActivity(intent);
                                        finish();

                                    } else {
                                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LoginActivity.this);
                                        dlgAlert.setMessage("wrong password or email");
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


            //Anem a l'activity de Registre
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            });

            //Seleccionem el Logout
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


    //Guardem l'estat del boto, i guardem l'informacio dins del SharedPreference
    public void saveStateButton(){
        SharedPreferences sharedPreferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(BUTTON_PREFERENCES, rbSesion.isChecked()).apply();
    }
    public boolean stateButton(){
        SharedPreferences sharedPreferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return sharedPreferences.getBoolean(BUTTON_PREFERENCES, false);
    }


}