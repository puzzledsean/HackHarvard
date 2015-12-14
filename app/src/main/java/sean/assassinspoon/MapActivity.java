package sean.assassinspoon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.notifications.MessageFlags;
import com.microsoft.band.notifications.VibrationType;
import com.microsoft.band.tiles.BandTile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MapActivity extends FragmentActivity implements LocationProvider.LocationCallback,
        OnMapReadyCallback {
    private BandClient client = null;
    private Button btnStart;
    private Button btnToMenu;
    private Button btnToTarget;
    private TextView txtStatus;

    private long currentTime = System.currentTimeMillis();

    private UUID tileId = UUID.fromString("aa0D508F-70A3-47D4-BBA3-812BADB1F8Aa");

    public static final String TAG = MapActivity.class.getSimpleName(); // debug Tag
    private int count = 0;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationProvider mLocationProvider;

    private Marker userMarker, targetMarker, assassinMarker;
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

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnToMenu = (Button) findViewById((R.id.toMenu));
        btnToTarget = (Button) findViewById(R.id.toTarget);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("");
                new appTask("hello", "it's a test message").execute();
            }
        });
        btnToMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MapActivity.this, CreateOrJoinActivity.class);
                startActivity(intent);
            }
        });
        btnToTarget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MapActivity.this, TargetInfoPageActivity.class);
                startActivity(intent);
            }


        });

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
        targetPos = new LatLng(42.351220, -71.109819);
        assassinPos = new LatLng(42.351026, -71.107995);

        // Initialize the user and target player spots on the map
        userMarker = mMap.addMarker(new MarkerOptions().position(userPos).title("User"));
        setMarkerColor(userMarker, BitmapDescriptorFactory.HUE_RED);

        targetMarker = mMap.addMarker(new MarkerOptions().position(targetPos).title("Target"));
        setMarkerColor(targetMarker, BitmapDescriptorFactory.HUE_BLUE);

        assassinMarker = mMap.addMarker(new MarkerOptions().position(assassinPos).title("Assassin"));
        setMarkerColor(targetMarker, BitmapDescriptorFactory.HUE_YELLOW);

        targetMarker.setVisible(true); // target is invisible at the beginning of the game
        assassinMarker.setVisible(false); // target is invisible at the beginning of the game

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
        userMarker.setTitle(count + " latitude: " + userPos.latitude + " longitude: " + userPos.longitude);
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

        long thirtySeconds = System.currentTimeMillis();

        if (distanceT <= 800 && (thirtySeconds - currentTime > 20000)) {
            targetAlerted();
            // targetMarker.setVisible(true);
            Log.i(TAG, "latU: " + latU + "lngU: " + lngU);
            Log.i(TAG, "Within Range: " + distanceT);
        }
        else {
            // targetMarker.setVisible(false);
            Log.i(TAG, "Outside Range: " + distanceT);
        }

        if (distanceA <= 1000 && (thirtySeconds - currentTime > 15000)) {
            notifyUserOfDanger();
            assassinMarker.setVisible(true);
            Log.i(TAG, "Assassin in range: " + distanceA);
            currentTime = System.currentTimeMillis();
        }else{
            assassinMarker.setVisible(false);
            Log.i(TAG, "Assassin outside of range: " + distanceA);
        }
    }

    private void targetAlerted() {
        txtStatus.setText("");
        new appTask("Target", "Target is alerted!").execute();
        Log.i(TAG, "Target is alerted!");
    }

    private void notifyUserOfDanger() {
        // Add Microsoft Band notifications
        txtStatus.setText("");
        new appTask("Warning", "Your assassin is near!").execute();
        Log.i(TAG, "Your assassin is nearby!");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // default generated stub
    }

    public void sendToBand(String title, String message) {
        new appTask(title, message).execute();
    }


    private class appTask extends AsyncTask<Void, Void, Void> {
        private String message;
        private String title;

        public appTask(String title, String message) {
            this.title = title;
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (getConnectedBandClient()) {
                    if (doesTileExist(client.getTileManager().getTiles().await(), tileId)) {
                        if(message.equals("Your assassin is near!")) {
                            sendMessage(title, message);
                            sendAlarmVibration();
                        }else{
                            sendMessage(title, message);
                            sendNoticeVibration();
                        }
                    } else {
                        if (addTile()) {
                            sendMessage("hint", "Send message to new message tile");
                        }
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage = "";
                switch (e.getErrorType()) {
                    case DEVICE_ERROR:
                        exceptionMessage = "Please make sure bluetooth is on and the band is in range.";
                        break;
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.";
                        break;
                    case BAND_FULL_ERROR:
                        exceptionMessage = "Band is full. Please use Microsoft Health to remove a tile.";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage();
                        break;
                }
                appendToUI(exceptionMessage);

            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }

    private void appendToUI(final String string) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtStatus.append(string);
            }
        });
    }

    private boolean doesTileExist(List<BandTile> tiles, UUID tileId) {
        for (BandTile tile : tiles) {
            if (tile.getTileId().equals(tileId)) {
                return true;
            }
        }
        return false;
    }

    private boolean addTile() throws Exception {
        /* Set the options */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap tileIcon = BitmapFactory.decodeResource(getBaseContext().getResources(), R.raw.tile_icon_large, options);
        Bitmap badgeIcon = BitmapFactory.decodeResource(getBaseContext().getResources(), R.raw.tile_icon_small, options);

        BandTile tile = new BandTile.Builder(tileId, "MessageTile", tileIcon)
                .setTileSmallIcon(badgeIcon).build();
        appendToUI("Message Tile is adding ...\n");
        if (client.getTileManager().addTile(this, tile).await()) {
            appendToUI("Message Tile is added.\n");
            return true;
        } else {
            appendToUI("Unable to add message tile to the band.\n");
            return false;
        }
    }

    private void sendMessage(String title, String message) throws BandIOException {
        client.getNotificationManager().sendMessage(tileId, title, message, new Date(), MessageFlags.SHOW_DIALOG);
        appendToUI(message + "\n");
    }

    private void sendAlarmVibration() throws BandIOException{
        try {
            // send a vibration request of type alert alarm to the Band
            client.getNotificationManager().vibrate(VibrationType.NOTIFICATION_ALARM).await();
        } catch (InterruptedException e) {
            // handle InterruptedException
        } catch (BandException e) {
            // handle BandException
        }
    }

    private void sendNoticeVibration() throws BandIOException{
        try {
            // send a vibration request of type alert alarm to the Band
            client.getNotificationManager().vibrate(VibrationType.ONE_TONE_HIGH).await();
        } catch (InterruptedException e) {
            // handle InterruptedException
        } catch (BandException e) {
            // handle BandException
        }
    }

    private boolean getConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                appendToUI("Band isn't paired with your phone.\n");
                return false;
            }
            client = BandClientManager.getInstance().create(getBaseContext(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        // appendToUI("Band is connecting...\n");
        return ConnectionState.CONNECTED == client.connect().await();
    }
}
