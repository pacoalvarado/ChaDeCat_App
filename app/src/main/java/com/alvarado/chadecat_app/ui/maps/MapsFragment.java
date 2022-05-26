package com.alvarado.chadecat_app.ui.maps;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

public class MapsFragment extends Fragment   {
    GoogleMap mMap;
    ViewGroup contenidor;
    Location actual;
    private FusedLocationProviderClient fusedLocationClient;
    LatLng miUbicacion, punt1, punt2, puntF;
    //private Polyline mPolyline;
    Button btn_geo;


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

                                miUbicacion = new LatLng(actual.getLatitude(), actual.getLongitude());
                                //mMap.addMarker(new MarkerOptions().position(miUbicacion).title("ubicacion actual"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 30));
                                //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                punt1 = new LatLng(41.723102, 1.818289);
                                Location uFinal = new Location("punt1");
                                uFinal.setLatitude(41.723102);
                                uFinal.setLongitude(1.818083);
                                punt2 = new LatLng(41.722456, 1.818102);
                                Location uFinal1 = new Location("punt2");
                                uFinal1.setLatitude(41.722456);
                                uFinal1.setLongitude(1.818102);
                                mMap.addMarker(new MarkerOptions().position(punt2).title("Punt2"));
                                mMap.addMarker(new MarkerOptions().position(punt1).title("EdR CONSUM Manresa").snippet("CONSUM - Carrer de Sant Joan d'en Coll, Manresa\n ACCÉS: targeta.\n Mennekes sense cable MNK (20kW)"));
                                mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(contenidor.getContext()));
                                //drawRoute();





                            }
                        });


            }
        });

        return view;
    }


    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(contenidor.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }




    /*private void drawRoute(){

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(miUbicacion, puntF);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }*/


    /*private String getDirectionsUrl(LatLng origin,LatLng dest){

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
    }*/

/** A method to download json data from url */
    /*private String downloadUrl(String strUrl) throws IOException {
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
    }*/

/** A class to download data from Google Directions URL */
    /*private class DownloadTask extends AsyncTask<String, Void, String> {

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
    }*/

/** A class to parse the Google Directions in JSON format */
    /*private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

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