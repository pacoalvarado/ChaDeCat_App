package com.alvarado.chadecat_app.ui.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alvarado.chadecat_app.Pop;
import com.alvarado.chadecat_app.R;
import com.alvarado.chadecat_app.infoWindow.MyInfoWindowAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MapsFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {
    GoogleMap mMap;

    ViewGroup contenidor;
    Location actual;
    private FusedLocationProviderClient fusedLocationClient;
    LatLng miUbicacion, punt1, punt2, puntF, punt;
    private Polyline mPolyline;
    Button btn_min, btn_ruta, btn_msg, btn_reserva, btn_close;
    Location uFinal, uFinal1, locationF;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<Float, LatLng> mapDistance = new HashMap<>();
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    boolean show;
    Marker m;
    int a = 0;
    Fragment map;

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



                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                LocationManager locationManager = (LocationManager) container.getContext().getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
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


                fusedLocationClient = LocationServices.getFusedLocationProviderClient(container.getContext());
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                actual = location;

                                miUbicacion = new LatLng(actual.getLatitude(), actual.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 30));


                                db.collection("puntsrecarrega")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                       String nameFinal = document.get("nom").toString();
                                                       String carrerFinal = document.get("carrer").toString();
                                                       String latitude = document.get("latitude").toString();
                                                       String longitude = document.get("longitude").toString();
                                                       Boolean reservat = document.getBoolean("reservat");
                                                       String usuariReservat = document.get("usuariReservat").toString();

                                                        String emailUserRes = fUser.getEmail();
                                                        Log.d("EMAIL", emailUserRes);
                                                        Log.d("EMAILR", usuariReservat);

                                                        btn_msg = getActivity().findViewById(R.id.btn_msg);
                                                        btn_ruta = getActivity().findViewById(R.id.btn_ruta);
                                                        btn_reserva = getActivity().findViewById(R.id.btn_reservar);
                                                        btn_close = getActivity().findViewById(R.id.btn_close);



                                                       if(usuariReservat.equals(emailUserRes) || !reservat){

                                                           double latitudeFinal = Double.parseDouble(latitude);
                                                           double longitudeFinal = Double.parseDouble(longitude);

                                                           punt = new LatLng(latitudeFinal, longitudeFinal);

                                                           location.setLatitude(latitudeFinal);
                                                           location.setLongitude(longitudeFinal);



                                                           m = mMap.addMarker(new MarkerOptions().position(punt).title(nameFinal).snippet(carrerFinal));





                                                          mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                               @Override
                                                               public boolean onMarkerClick(@NonNull Marker marker) {
                                                                   if(a == 0){
                                                                       a = 1;
                                                                       mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(contenidor.getContext()));
                                                                       btn_ruta.setVisibility(View.VISIBLE);
                                                                       btn_reserva.setVisibility(View.VISIBLE);
                                                                       btn_msg.setVisibility(View.VISIBLE);
                                                                       marker.showInfoWindow();
                                                                   }else {

                                                                       btn_ruta.setVisibility(View.INVISIBLE);
                                                                       btn_reserva.setVisibility(View.INVISIBLE);
                                                                       btn_msg.setVisibility(View.INVISIBLE);
                                                                       marker.hideInfoWindow();
                                                                       a = 0;
                                                                   }

                                                                   return true;

                                                               }


                                                           });






                                                           float distance = actual.distanceTo(location) / 1000;

                                                           mapDistance.put(distance, punt);




                                                       }

                                                    }
                                                } else {
                                                }
                                            }
                                        });

                                btn_min = getActivity().findViewById(R.id.btn_buscar_min);
                                btn_min.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        float min = Collections.min(mapDistance.keySet());

                                        LatLng latLngMasCercano = mapDistance.get(min);

                                        mMap.clear();

                                        puntF = new LatLng(latLngMasCercano.latitude, latLngMasCercano.longitude);

                                        mMap.addMarker(new MarkerOptions().position(puntF).title("PuntFinal"));

                                        //drawRoute();

                                    }
                                });





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

    @Override
    public boolean onMyLocationButtonClick() {
        return true;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }





/*
    private void drawRoute(){

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(miUbicacion, puntF);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=" + getString(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        Log.e("URL", url);
        return url;
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionJSONParser parser = new DirectionJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getActivity(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }*/


}
