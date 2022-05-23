package com.alvarado.chadecat_app.ui.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alvarado.chadecat_app.MapsActivity1;
import com.alvarado.chadecat_app.R;
import com.alvarado.chadecat_app.databinding.ActivityMaps1Binding;
import com.alvarado.chadecat_app.databinding.FragmentHomeBinding;
import com.alvarado.chadecat_app.infoWindow.MyInfoWindowAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps1, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapa);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {


                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(41.721772, 1.818171);
                //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Joviat"));
                googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_icon_gps)).anchor(0.0f, 1.0f).position(sydney).title("Joviat"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 30));

                LatLng punt1 = new LatLng(41.722407, 1.818083);
                googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_icon_recharge)).anchor(0.0f, 1.0f).position(punt1).title("EdR CONSUM Manresa").snippet("CONSUM - Carrer de Sant Joan d'en Coll, Manresa\n ACCÉS: targeta.\n Mennekes sense cable MNK (20kW)"));
            }
        });

        return  view;
    }

}
