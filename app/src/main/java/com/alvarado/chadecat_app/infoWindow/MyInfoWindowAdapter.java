package com.alvarado.chadecat_app.infoWindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.alvarado.chadecat_app.MapsActivity1;
import com.alvarado.chadecat_app.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;
    public MyInfoWindowAdapter(Context context) {

        this.context = context;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        View infoView = LayoutInflater.from(context).inflate(R.layout.custom_info, null);
        ImageView imageView = infoView.findViewById(R.id.imageView6);
        TextView title = infoView.findViewById(R.id.title);
        TextView snipp = infoView.findViewById(R.id.snipp);
        imageView.setImageDrawable(imageView.getResources().getDrawable(R.drawable.ic_launcher_foreground));
        title.setText(marker.getTitle());
        snipp.setText(marker.getSnippet());
        return infoView;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {

        return null;
    }


}

