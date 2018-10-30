package com.brighton.caninecentral;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

import com.brighton.caninecentral.MainActivity;
import com.brighton.caninecentral.caninecentral.R;
import com.brighton.caninecentral.database.LocationPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapPopupActivity extends FragmentActivity {
    private GoogleMap map;
    private List<LocationPoint> route;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map_popup);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapViewPopup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;
        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.6));

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    getIncomingIntent();
                    drawRoute(id);
                }
            });
        }
    }


    private void drawRoute(int id) {
        route = MainActivity.database.locationDoa().getLocations(id);
        map.clear();
        LatLng startLocation = null;
        PolylineOptions options = new PolylineOptions().width(15).color(this.getResources().getColor(R.color.colorAccent)).geodesic(true);

        for (int i = 0; i < route.size(); i++) {
            LocationPoint locationPoint = route.get(i);
            String str = locationPoint.latlngPoint;
            String split[] = str.split(",");

            double lat = Double.parseDouble(split[0]);
            double lng = Double.parseDouble(split[1]);
            LatLng point = new LatLng(lat, lng);
            options.add(point);

            if(i == 0) {
                startLocation = point;
            }
            Log.d("points" , str);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 15f));
        map.setMinZoomPreference(15f);
        map.addPolyline(options);
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("route_id")) {
            String id = getIntent().getStringExtra("route_id");
            this.id = Integer.parseInt(id);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}
