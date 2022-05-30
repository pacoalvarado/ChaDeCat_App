package com.alvarado.chadecat_app;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.alvarado.chadecat_app.databinding.ActivityPerfil2Binding;
import com.alvarado.chadecat_app.ui.maps.MapsFragment;
import com.alvarado.chadecat_app.ui.perfil.PerfilFragment;
import com.alvarado.chadecat_app.ui.perfil.PerfilViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPerfil2Binding binding;
    TextView tvemail, tvname;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String comEmail;
    View v_logout, v_perfil;

    private DatabaseReference mDatabase;

    FirebaseUser fUser;

    FusedLocationProviderClient fusedLocationProviderClient;
    Fragment mapsFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityPerfil2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String user = fUser.getEmail();



        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        comEmail = user;

        Fragment fragment = new MapsFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.mapa, fragment).commit();


        //Toast.makeText(getApplicationContext(), user, Toast.LENGTH_LONG).show();

        setSupportActionBar(findViewById(R.id.toolbar));
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_perfil)
                .setOpenableLayout(drawer)
                .build();



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_perfil2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        ReadUser();

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_perfil2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void ReadUser() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                tvname = findViewById(R.id.username_id);
                                tvemail = findViewById(R.id.email_id);

                                v_logout = findViewById(R.id.nav_logout);
                                v_perfil = findViewById(R.id.nav_perfil);


                                v_logout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SharedPreferences preferences = getSharedPreferences("alvarado.chadecat_app", MODE_PRIVATE);
                                        preferences.edit().clear().commit();
                                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });


                                if (document.get("email").equals(comEmail)) {
                                    String nameFinal = document.get("name").toString();
                                    String emailFinal = document.get("email").toString();

                                    tvname.setText(nameFinal);
                                    tvemail.setText(emailFinal);
                                }


                            }
                        } else {
                        }
                    }
                });
    }
}
