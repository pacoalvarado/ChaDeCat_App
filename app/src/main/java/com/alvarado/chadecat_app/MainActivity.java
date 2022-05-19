package com.alvarado.chadecat_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.alvarado.chadecat_app.databinding.ActivityPerfil2Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPerfil2Binding binding;
    TextView tvemail, tvname;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String comEmail;
    Button btn_logout;

    private DatabaseReference mDatabase;

    FirebaseUser fUser;

    private MapsActivity mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPerfil2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String user = fUser.getEmail();



        comEmail = user;

        Fragment fragment = new MapsActivity();

        getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment).commit();


        //Toast.makeText(getApplicationContext(), user, Toast.LENGTH_LONG).show();

        setSupportActionBar(binding.appBarPerfil2.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favorite)
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

    public void ReadUser(){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                tvname = findViewById(R.id.username_id);
                                tvemail = findViewById(R.id.email_id);

                                btn_logout = findViewById(R.id.logout);

                                btn_logout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(i);
                                    }
                                });


                                if(document.get("email").equals(comEmail)){
                                    String nameFinal = document.get("name").toString();
                                    String emailFinal = document.get("email").toString();

                                    tvname.setText(nameFinal);
                                    tvemail.setText(emailFinal);
                                }

                                //Log.e("A",document.getId() + " => " + document.getData());
                                //idUser = document.getId();
                            }
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
