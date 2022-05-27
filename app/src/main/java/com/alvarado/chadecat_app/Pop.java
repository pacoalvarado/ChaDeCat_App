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
    TextView nmsg, emsg, dmsg, mmsg;
    EditText etname, etemail, etmsg;
    Button btn_afegir;
    FirebaseUser fUser;
    String nameF;

    RecyclerView rvMissatge;
    ArrayList<Missatges> msgArrayList;
    MissatgeAdapterFirestore myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop);

        btn_afegir = findViewById(R.id.brn_afegir_msg);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int)(height*.8));

        rvMissatge = findViewById(R.id.rvMissatges);
        rvMissatge.setHasFixedSize(true);
        rvMissatge.setLayoutManager(new LinearLayoutManager(this));

        msgArrayList = new ArrayList<Missatges>();
        myAdapter = new MissatgeAdapterFirestore(Pop.this, msgArrayList);

        rvMissatge.setAdapter(myAdapter);

        EventChangeListener();

        btn_afegir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Pop.this, PopAfegir.class));
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
                        msgArrayList.add(dc.getDocument().toObject(Missatges.class));
                    }

                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
