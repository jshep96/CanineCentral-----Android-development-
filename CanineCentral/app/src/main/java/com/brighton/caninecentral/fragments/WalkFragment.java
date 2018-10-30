package com.brighton.caninecentral.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.brighton.caninecentral.MainActivity;
import com.brighton.caninecentral.caninecentral.R;
import com.brighton.caninecentral.database.LocationPoint;
import com.brighton.caninecentral.database.PetRouteJoin;
import com.brighton.caninecentral.database.Routes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class WalkFragment extends Fragment {
    private static final String TAG = "WalkActivity";
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private boolean mapPermissionGranted = false;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient mFusedLocationProvider;

    private GoogleMap map;
    private MapView mapView;
    private ArrayList<LatLng> route;
    private Button startBtn;
    private TextView distanceText;
    private LocationCallback mLocationCallback;
    private float distance;
    private Location currLocation;
    private Chronometer timer;

    private List<String> petOnWalk = new ArrayList<String>();

    public WalkFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_walk, container, false);

        mapView = v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        distanceText = v.findViewById(R.id.distance);
        startBtn = v.findViewById(R.id.startBtn);
        timer = v.findViewById(R.id.timer);
        route = new ArrayList<>();
        mFusedLocationProvider = getFusedLocationProviderClient(getActivity());

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
                Log.d(TAG, "tracking......................");
            }
        };

        checkLocPermission();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                Log.d(TAG, "stating map............");
                map = mMap;
                startMap();
            }
        });


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = startBtn.getText().toString();

                if (btnText.equals("Start")) {

                    if(petsNotNull()) {
                        Toast.makeText(getActivity(), "Need to add a pet first!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        openDialog();
                        distance = 0;
                        timer.setBase(SystemClock.elapsedRealtime());
                        startLocationTracking();
                        timer.start();
                        startBtn.setText("Stop");
                    }
                } else {
                    stopLocationTracking();
                    saveToDatabase();
                    timer.stop();
                    route.clear();
                    petOnWalk.clear();
                    startBtn.setText("Start");
                }
            }
        });
        return v;
    }


    @SuppressLint("MissingPermission")
    private void startMap() {
        if (mapPermissionGranted) {
            Log.d(TAG, "map started");
            map.setMyLocationEnabled(true);
            map.setMinZoomPreference(15f);
            getLocation();
        }
    }


    private void openDialog() {
        final String[] pets;
        boolean[] checkedPets;
        final ArrayList<Integer> selectedItems = new ArrayList<>();

        List<String> petList = MainActivity.database.petDoa().getPets();
        pets = petList.toArray(new String[petList.size()]);
        checkedPets = new boolean[pets.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select pets going for a walk");

        builder.setMultiChoiceItems(pets, checkedPets, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if (isChecked) {
                    if (!selectedItems.contains(i)) {
                        selectedItems.add(i);
                    } else {
                        selectedItems.remove(i);
                    }
                }
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {

                for (int i = 0; i < selectedItems.size(); i++) {
                    petOnWalk.add(pets[selectedItems.get(i)]);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @SuppressLint("MissingPermission")
    public void getLocation() {
        if (mapPermissionGranted) {
            mFusedLocationProvider.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {

                                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting location");
                        }
                    });
        }
    }


    protected void startLocationTracking() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(4000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(15f);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> locationUpdates = client.checkLocationSettings(builder.build());
        Log.d(TAG, "location tracking started");


        locationUpdates.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (mapPermissionGranted) {
                    Log.d(TAG, "trying to locate");
                    mFusedLocationProvider.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
                }
            }
        });
        locationUpdates.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        Log.d(TAG, "failed");
                        ResolvableApiException resolve = (ResolvableApiException) e;
                        resolve.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.d(TAG, "error location update setting request");
                    }
                }
            }
        });
    }


    public void onLocationChanged(Location location) {
        if (currLocation != null) {
            distance += currLocation.distanceTo(location) / 1000;
        }

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        route.add(currentLatLng);
        distanceText.setText(String.format("%.2f Km", distance));
        currLocation = location;
        drawRoute();
        Log.d(TAG, "changed location" + currentLatLng);
    }


    private void drawRoute() {
        map.clear();
        PolylineOptions options = new PolylineOptions().width(15).color(getContext().getResources().getColor(R.color.colorAccent)).geodesic(true);

        for (int i = 0; i < route.size(); i++) {
            LatLng point = route.get(i);
            options.add(point);
        }
        map.addPolyline(options);
        Log.d(TAG, "drawing route");
    }


    private void stopLocationTracking() {
        mFusedLocationProvider.removeLocationUpdates(mLocationCallback);
        Log.d(TAG, "Stopped tracking......................");
    }


    private boolean petsNotNull() {
        if(MainActivity.database.petDoa().getPets().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    private void saveToDatabase() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(todayDate);

        Routes routeDb = new Routes();
        routeDb.distance = distance;
        routeDb.date = today;
        routeDb.time = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / (1000 * 60) % 60);
        long routeId = MainActivity.database.routeDoa().addRoute(routeDb);


        for(int i = 0; i< petOnWalk.size(); i++ ){
            PetRouteJoin petRoute = new PetRouteJoin();
            petRoute.routeId = (int) routeId;
            String s = petOnWalk.get(i);
            petRoute.petId = MainActivity.database.petDoa().getId(s);
            MainActivity.database.petRouteJoinDoa().insert(petRoute);

            String str = Integer.toString(MainActivity.database.petDoa().getId(s));
            Log.d("tag", str);
        }


        for (int i = 0; i < route.size(); i++) {
            LatLng currLatLng = route.get(i);
            double lat = currLatLng.latitude;
            double lng = currLatLng.longitude;
            String latLngPoint = (Double.toString(lat) + "," + Double.toString(lng));

            LocationPoint locationDb = new LocationPoint();
            locationDb.latlngPoint = latLngPoint;
            locationDb.routeId = (int) routeId;

            MainActivity.database.locationDoa().addLocation(locationDb);
        }
        Toast.makeText(getContext(), "Saved route!", Toast.LENGTH_SHORT).show();
    }


    private void checkLocPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else {
            mapPermissionGranted = true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapPermissionGranted = true;
                startMap();
            } else {
                Toast.makeText(getContext(), "Location permissions denied! App will not function correctly",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroyView() {
        mapView.onDestroy();
        stopLocationTracking();
        super.onDestroyView();
        Log.d("Tag", "onDestoryView");
    }


}
