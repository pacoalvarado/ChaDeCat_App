package com.alvarado.chadecat_app.infoWindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.alvarado.chadecat_app.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnMyLocationButtonClickListener{

    Context context;
    int a = 0;

    public MyInfoWindowAdapter(Context context) {
        this.context = context;

    }


    /**
     * Metode que es crida quan el demanem des de el MapsFragment (quan cliquem en un market)
     * @param marker passa el market el qual em clicat
     * @return retorna la vista amb l'informacio d'aquell market.
     */
    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        View infoView = LayoutInflater.from(context).inflate(R.layout.custom_info, null);
        TextView title = infoView.findViewById(R.id.title);
        TextView snipp = infoView.findViewById(R.id.snipp);


        title.setText(marker.getTitle());
        snipp.setText(marker.getSnippet());

        return infoView;
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