package com.alvarado.chadecat_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class PopAfegir extends AppCompatActivity {
    Button btn_afegir_final, btn_exit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    EditText etmsg;
    String nameF;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_afegir);

        btn_afegir_final = findViewById(R.id.btn_add);
        btn_exit = findViewById(R.id.btn_exit);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int)(height*.8));

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PopAfegir.this, Pop.class));
                finish();
            }
        });

        btn_afegir_final.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                etmsg = findViewById(R.id.etmsg);

                String emailF = fUser.getEmail();
                String misstageF = etmsg.getText().toString().trim();

                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.get("email").equals(emailF)) {
                                            nameF = document.get("name").toString();
                                            AddMsg(nameF, misstageF);
                                        }
                                    }
                                } else {
                                }
                            }
                        });

                startActivity(new Intent(PopAfegir.this, Pop.class));
                finish();
            }
        });

    }

    public void AddMsg(String name, String msg){
        // Create a new user with a first and last name
        Map<String, Object> missatge = new HashMap<>();
        missatge.put("nom", name);
        missatge.put("missatge", msg);


// Add a new document with a generated ID
        db.collection("missatges")
                .add(missatge)
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
