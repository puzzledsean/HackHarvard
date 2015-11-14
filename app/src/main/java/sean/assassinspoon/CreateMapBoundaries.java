package sean.assassinspoon;

/**
 * Created by huyle on 11/14/2015.
 */
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateMapBoundaries extends FragmentActivity implements OnMapReadyCallback {
    private String name = null;
    private int numOfPlayers = 0;

    private GoogleMap map;
    private Marker[] markers = new Marker[4];
    private ParseGeoPoint[] boundaries = new ParseGeoPoint[4];
    private int count = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_map_boundaries);
        Intent myIntent = getIntent();
        name = myIntent.getStringExtra("name");
        numOfPlayers = Integer.parseInt(myIntent.getStringExtra("numOfPlayers"));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button createMap = (Button) findViewById(R.id.createMap);
        createMap.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateMapBoundaries();
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(CreateMapBoundaries.this, "Success! Created game: " + name,
                            Toast.LENGTH_LONG).show();
                }
            }
        );

        map = mapFragment.getMap();
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (count == 4){
                    map.clear();
                    Arrays.fill(markers, null);
                    Arrays.fill(boundaries, null);
                    count = 0;
                }
                Marker marker = map.addMarker(new MarkerOptions().position(point));
                markers[count] = marker;
                boundaries[count] = new ParseGeoPoint(point.latitude, point.longitude);
                if(count == 3){
                    map.addPolyline(new PolylineOptions()
                            .add(new LatLng(boundaries[0].getLatitude(), boundaries[0].getLongitude()), new LatLng(boundaries[1].getLatitude(), boundaries[1].getLongitude()))
                            .width(5)
                            .color(Color.RED));
                    map.addPolyline(new PolylineOptions()
                            .add(new LatLng(boundaries[1].getLatitude(), boundaries[1].getLongitude()), new LatLng(boundaries[2].getLatitude(), boundaries[2].getLongitude()))
                            .width(5)
                            .color(Color.RED));
                    map.addPolyline(new PolylineOptions()
                            .add(new LatLng(boundaries[2].getLatitude(), boundaries[2].getLongitude()), new LatLng(boundaries[3].getLatitude(), boundaries[3].getLongitude()))
                            .width(5)
                            .color(Color.RED));
                    map.addPolyline(new PolylineOptions()
                            .add(new LatLng(boundaries[3].getLatitude(), boundaries[3].getLongitude()), new LatLng(boundaries[0].getLatitude(), boundaries[0].getLongitude()))
                            .width(5)
                            .color(Color.RED));
                }
                count++;
            }
        });
    }

    public void updateMapBoundaries(){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");

        query.whereEqualTo("name", name);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    //do nothing
                } else {
                    for(ParseGeoPoint i : boundaries){
                        object.add("mapBoundaries", i);
                    }
                    object.saveInBackground();
                }
            }
        });
    }

    private boolean checkPermission(){
        int fineLocationResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (fineLocationResult == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker to Boston, and move the camera. Google Map's filler code.
        LatLng boston = new LatLng(42.3601, -71.0589);
        // map.addMarker(new MarkerOptions().position(boston).title("Marker in Boston"));
        map.moveCamera(CameraUpdateFactory.newLatLng(boston));
        /*
        if(checkPermission()){
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            Log.d("Longitude", String.valueOf(longitude));
            Log.d("Latitude", String.valueOf(latitude));
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
        */
    }
}
