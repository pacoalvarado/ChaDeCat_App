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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment  implements OnMapReadyCallback {
    private GoogleMap mMap;
    //private ActivityMaps1Binding binding;

    Context ctx;
    View root;
    ViewGroup contenidor;

    private @NonNull ActivityMaps1Binding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MapsViewModel mapsViewModel =
                new ViewModelProvider(this).get(MapsViewModel.class);

        binding = ActivityMaps1Binding.inflate(inflater, container, false);

        root = binding.getRoot();
        ctx = root.getContext();
        contenidor = container;







        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        ViewGroup content = contenidor;
        //Fragment p = contenidor.findViewById(R.id.nav_host_fragment_content_perfil2);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getParentFragmentManager()
                        .findFragmentById(R.id.mapa);
        Log.i("MAPS FRAGMENT", "**"+mapFragment);
        //mapFragment.getMapAsync(this);

        getLocalizacion();
    }

    @Override
    public void onResume() {
        SupportMapFragment mapFragment;

        super.onResume();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        do {
            mapFragment =
                    (SupportMapFragment) getParentFragmentManager()
                            .findFragmentById(R.id.nav_home);
        } while(mapFragment == null);
        Log.i("MAPS FRAGMENT", "**"+mapFragment);
    }


    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocalizacion();

        Log.d("ON CREATE", "");
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Map","hola");
        mMap = googleMap;
        LatLng sydney = new LatLng(16.823809, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").snippet("CONSUM - Carrer de Sant Joan d'en Coll, Manresa\\n ACCÉS: targeta.\\n Mennekes sense cable MNK (20kW)"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(ctx));


        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(miUbicacion).title("ubicacion actual"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(miUbicacion)
                        .zoom(14)
                        .bearing(90)
                        .tilt(45)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

}
