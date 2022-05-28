package com.alvarado.chadecat_app.infoWindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.alvarado.chadecat_app.HomeActivity;
import com.alvarado.chadecat_app.LoginActivity;
import com.alvarado.chadecat_app.R;
import com.alvarado.chadecat_app.RegisterActivity;
import com.alvarado.chadecat_app.ui.maps.MapsFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnMyLocationButtonClickListener{

    Context context;
    int a = 0;

    public MyInfoWindowAdapter(Context context) {
        this.context = context;
    }




    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        View infoView = LayoutInflater.from(context).inflate(R.layout.custom_info, null);
        TextView title = infoView.findViewById(R.id.title);
        TextView snipp = infoView.findViewById(R.id.snipp);


        title.setText(marker.getTitle());
        snipp.setText(marker.getSnippet());

        Log.e("**A", "true");


        if(a == 0){
            a = 1;
            return infoView;
        }else {

            marker.hideInfoWindow();
            a = 0;
            return null;
        }
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {

        return null;
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}