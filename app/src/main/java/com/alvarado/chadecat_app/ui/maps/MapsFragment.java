package com.alvarado.chadecat_app.ui.maps;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
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

import com.alvarado.chadecat_app.R;
import com.alvarado.chadecat_app.infoWindow.MyInfoWindowAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsFragment extends Fragment {
    GoogleMap mMap;
    ViewGroup contenidor;
    Location actual;
    private FusedLocationProviderClient fusedLocationClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps1, container, false);
        this.contenidor = container;

        getLocalizacion();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapa);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(container.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(container.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);

                mMap.getUiSettings().setMyLocationButtonEnabled(false);

                LocationManager locationManager = (LocationManager) container.getContext().getSystemService(Context.LOCATION_SERVICE);
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
                        Log.e("CameraZoom", "Zoom");
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





                /*
                mMap = googleMap;

                // Add a marker in Sydney and move the camera

                LatLng sydney = new LatLng(41.721772, 1.818171);
                //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Joviat"));
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_icon_gps)).anchor(0.0f, 1.0f).position(sydney).title("Joviat"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 30));

                LatLng punt1 = new LatLng(41.722407, 1.818083);
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_icon_recharge)).anchor(0.0f, 1.0f).position(punt1).title("EdR CONSUM Manresa").snippet("CONSUM - Carrer de Sant Joan d'en Coll, Manresa\n ACCÉS: targeta.\n Mennekes sense cable MNK (20kW)"));
*/
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(container.getContext());
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                actual = location;
                                LatLng miUbicacion = new LatLng(actual.getLatitude(), actual.getLongitude());
                                //mMap.addMarker(new MarkerOptions().position(miUbicacion).title("ubicacion actual"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 30));
                                //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                LatLng punt1 = new LatLng(41.722407, 1.818083);
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_icon_recharge)).anchor(0.0f, 1.0f).position(punt1).title("EdR CONSUM Manresa").snippet("CONSUM - Carrer de Sant Joan d'en Coll, Manresa\n ACCÉS: targeta.\n Mennekes sense cable MNK (20kW)"));
                                mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(contenidor.getContext()));
                            }
                        });

            }
        });

        return  view;
    }

    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(contenidor.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

}
