package com.alvarado.chadecat_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(41.721772, 1.818171);
                //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Joviat"));
                /*googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_icon_gps)).anchor(0.0f, 1.0f).position(sydney).title("Joviat"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 30));

                LatLng punt1 = new LatLng(41.722407, 1.818083);
                googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_icon_recharge)).anchor(0.0f, 1.0f).position(punt1).title("EdR CONSUM Manresa").snippet("CONSUM - Carrer de Sant Joan d'en Coll, Manresa\n ACCÉS: targeta.\n Mennekes sense cable MNK (20kW)"));*/

            }
        });
        return  view;
    }

}