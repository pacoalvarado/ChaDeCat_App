package com.alvarado.chadecat_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alvarado.chadecat_app.infoWindow.MyInfoWindowAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pop extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btn_afegir;
    FirebaseUser fUser;
    String nameMarker;

    RecyclerView rvMissatge;
    ArrayList<Missatges> msgArrayList;
    MissatgeAdapterFirestore myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop);

        btn_afegir = findViewById(R.id.brn_afegir_msg);

        nameMarker = getIntent().getExtras().getString("markerTitol");

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int)(height*.8));

        // Obtenci?? de les refer??ncies als components de GUI.
        rvMissatge = findViewById(R.id.rvMissatges);

        // Posar aquesta propietat a true incrementa el rendiment del RecyclerView
        // si sabem que la seva mida no canviar?? malgrat que hi pugui haver canvis
        // en les dades que mostra.
        rvMissatge.setHasFixedSize(true);

        // Si volem llista, activem el new LinearLayout...(); si volem grid, activem
        // el new GridLayout...(), indicant el nombre de columnes.
        rvMissatge.setLayoutManager(new LinearLayoutManager(this));

        // Creem el model de dades que mostrarem al RecyclerView. Executem una consulta sobre
        // la base de dades i guardem els resultats en un ArrayList.
        msgArrayList = new ArrayList<Missatges>();

        // Creem l'Adapter que associarem al RecyclerView a partir de l'ArrayList amb
        // les dades.
        myAdapter = new MissatgeAdapterFirestore(Pop.this, msgArrayList);

        // Associem l'Adapter al RecyclerView.
        rvMissatge.setAdapter(myAdapter);

        EventChangeListener();

        btn_afegir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Pop.this, PopAfegir.class);
                i.putExtra("markerTitol", nameMarker);
                startActivity(i);
                finish();
            }
        });
    }

    private void EventChangeListener(){

        db.collection("missatges").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        if(dc.getDocument().get("punt").equals(nameMarker)){
                            msgArrayList.add(dc.getDocument().toObject(Missatges.class));
                        }

                    }

                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
