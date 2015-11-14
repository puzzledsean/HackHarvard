package sean.assassinspoon;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements LocationProvider.LocationCallback,
        OnMapReadyCallback {
    public static final String TAG = MapActivity.class.getSimpleName(); // debug Tag
    private int count = 0;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationProvider mLocationProvider;

    private Marker userMarker, targetMarker;
    private LatLng userPos, targetPos, assassinPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        */
        mLocationProvider = new LocationProvider(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }
    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the userMarker to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A userMarker can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    // helper function to set marker color
    private void setMarkerColor(Marker marker, float color) {
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(color));
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Add a marker in current position, and move the camera.

        double userLat = 0, userLng = 0;
        LatLng userPos = new LatLng(userLat, userLng);

        mMap.setPadding(10, 10, 10, 10);

        // Place a static target near user
        targetPos = new LatLng(42.350292, -71.098425);
        assassinPos = new LatLng(42.350292, -71.098425);

        // Initialize the user and target player spots on the map
        userMarker = mMap.addMarker(new MarkerOptions().position(userPos).title("User"));
        setMarkerColor(userMarker, BitmapDescriptorFactory.HUE_RED);

        targetMarker = mMap.addMarker(new MarkerOptions().position(targetPos).title("Target"));
        setMarkerColor(targetMarker, BitmapDescriptorFactory.HUE_BLUE);

        targetMarker.setVisible(false); // target is invisible at the beginning of the game

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(userPos));
    }

    @Override
    public void handleNewLocation(Location userLoc) {
        userPos = new LatLng(userLoc.getLatitude(), userLoc.getLongitude());
        //LatLng userPos = new LatLng(0, 0); // If you want to test a static user location

        userMarker.setPosition(userPos); // Set new user location
        if(count == 0) {
            mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userPos));
        }
        Log.i(TAG, "latitude: " + userPos.latitude + "longitude: " + userPos.longitude);
        userMarker.setTitle(count + "latitude: " + userPos.latitude + "longitude: " + userPos.longitude);
        ++count;

        update(targetPos, assassinPos, userPos);
    }

    // convert miles to meters
    private static double toMeters(double miles) {
        return miles * 1609.34;
    }

    // helper function to calculate distance between two latlng points into meters
    private static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c; // in miles

        return toMeters(dist); // converted to meters
    }

    private void update(LatLng T, LatLng A, LatLng U) {
        // Somehow get long and lat of you, your targetMarker and your assassin
        // T stands for the latlng of target
        // A stands for the latlng of assassin
        // U stands for the latlng of the user
        double latU = U.latitude, lngU = U.longitude;
        double latA = A.latitude, lngA = A.longitude;
        double latT = T.latitude, lngT = T.longitude;

        double distanceT = distFrom(latU, lngU, latT, lngT);
        double distanceA = distFrom(latU, lngU, latA, lngA);

        if (distanceT <= 150) {
            targetMarker.setVisible(true);
            Log.i(TAG, "latU: " + latU + "lngU: " + lngU);
            Log.i(TAG, "Within Range: " + distanceT);
        }
        else {
            targetMarker.setVisible(false);
            Log.i(TAG, "Outside Range: " + distanceT);
        }

        if (distanceA <= 50) {
            notifyUserOfDanger();
        }
    }

    private void notifyUserOfDanger() {
        // Add Microsoft Band notifications
        Log.i(TAG, "Your assassin is nearby!");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // default generated stub
    }
}
