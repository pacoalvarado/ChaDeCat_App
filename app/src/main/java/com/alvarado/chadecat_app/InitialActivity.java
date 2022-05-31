package com.alvarado.chadecat_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        new Handler().postDelayed(new Runnable() {
            /**
             * Metode que ens canvia d'activiti al cap de 2 segons de ser iniciada.
             */
            @Override
            public void run() {
                Intent intent = new Intent(InitialActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }
}
