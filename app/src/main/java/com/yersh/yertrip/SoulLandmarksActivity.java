package com.yersh.yertrip;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.wms.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import Data.DatabaseHandler;
import Model.VisitedPlaces;

public class SoulLandmarksActivity extends AppCompatActivity {

    private DatabaseHandler databaseHandler = null;
    private String placeName = "";
    private VisitedPlaces place = null;
    private MapView map = null;
    private Marker currentMarker = null;
    private RotationGestureOverlay mRotationGestureOverlay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_soul_landmarks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoulLandmarksActivity.this, SoulActivity.class);

                startActivity(intent);
                finish();
            }
        });

        final LinearLayout markVisitedButton = findViewById(R.id.markVisitedButton);
        final ImageView visitedCircleImage = findViewById(R.id.visitedCircle);

        final Spinner spinner = findViewById(R.id.place_spinner);

        databaseHandler = new DatabaseHandler(this);

        AddPlaceToDB(databaseHandler, "The folklore village of Bukchon",126.9804801, 37.5809226);
        AddPlaceToDB(databaseHandler, "Gyeongbokgung Palace", 126.97708021956569, 37.5785220);
        AddPlaceToDB(databaseHandler, "Changdeokgung Palace", 126.9925263, 37.5808379);
        AddPlaceToDB(databaseHandler, "Gyeonghigong Palace", 126.9696028, 37.5690734);
        AddPlaceToDB(databaseHandler, "Myeongdong Cathedral", 126.9874671, 37.5631008);

        // MAP
        ConfigureMap();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                placeName = parent.getItemAtPosition(position).toString();

                try {
                    place = databaseHandler.getPlaceByName(placeName);
                } catch (CursorIndexOutOfBoundsException e) {
                    visitedCircleImage.setImageResource(R.drawable.unvisited_place_icon);
                    place = null;
                }

                if (place != null)
                {
                    if (place.getIsVisited() == 1)
                    {
                        visitedCircleImage.setImageResource(R.drawable.visited_place_icon);

                        IMapController mapController = map.getController();
                        mapController.setZoom(20.0);
                        GeoPoint point = new GeoPoint(place.getLatitude(), place.getLongitude());
                        deleteCurrentMarker();
                        setMarker(point);
                        mapController.setCenter(point);
                    }
                    else
                    {
                        visitedCircleImage.setImageResource(R.drawable.unvisited_place_icon);

                        IMapController mapController = map.getController();
                        mapController.setZoom(20.0);
                        GeoPoint point = new GeoPoint(place.getLatitude(), place.getLongitude());
                        deleteCurrentMarker();
                        setMarker(point);
                        mapController.setCenter(point);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        HandleAdapter(spinner);

        markVisitedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (place != null)
                {
                    if (place.getIsVisited() == 1)
                    {
                        visitedCircleImage.setImageResource(R.drawable.unvisited_place_icon);
                        place.setVisited(0);
                    }
                    else
                    {
                        visitedCircleImage.setImageResource(R.drawable.visited_place_icon);
                        place.setVisited(1);
                    }

                    databaseHandler.updatePlace(place);
                }

                LogDatabaseInfo();
            }
        });
    }

    private void AddPlaceToDB(DatabaseHandler databaseHandler, String placeName, Double longitude, Double latitude)
    {
        VisitedPlaces placeToAdd = new VisitedPlaces("London", placeName, 0, longitude, latitude);

        try {
            databaseHandler.getPlaceByName(placeName);
        } catch (CursorIndexOutOfBoundsException e) {
            databaseHandler.addPlace(placeToAdd);
        }

    }

    private void LogDatabaseInfo()
    {
        List<VisitedPlaces> placesList = databaseHandler.getAllPlaces();

        for (VisitedPlaces place : placesList)
        {
            Log.d("VisitedPlaces info: ", " ID: " + place.getId() + " , City: " + place.getCity()
                    + " , Name: " + place.getName() + " , isVisited: " + place.getIsVisited() +
                    " , Longitude: " + place.getLongitude() + " , Latitude: " + place.getLatitude());
        }
    }

    private void HandleAdapter(Spinner spinner)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("The folklore village of Bukchon");
        arrayList.add("Gyeongbokgung Palace");
        arrayList.add("Changdeokgung Palace");
        arrayList.add("Gyeonghigong Palace");
        arrayList.add("Myeongdong Cathedral");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);
    }

    private void ConfigureMap()
    {
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        Configuration.getInstance().setUserAgentValue(BuildConfig.LIBRARY_PACKAGE_NAME);
        Configuration.getInstance().setOsmdroidBasePath(getFilesDir());

        IMapController mapController = map.getController();
        mapController.setZoom(20.0);
        GeoPoint startPoint = new GeoPoint(51.4993832, -0.1288624);
        mapController.setCenter(startPoint);

        setMarker(startPoint);

        mRotationGestureOverlay = new RotationGestureOverlay(map);
        mRotationGestureOverlay.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(this.mRotationGestureOverlay);
    }

    private void setMarker(GeoPoint geoPoint)
    {
        Marker marker = new Marker(map);
        marker.setPosition(geoPoint);
        //marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setIcon(getResources().getDrawable(R.drawable.map_place_icon, null));
        marker.setTitle("Start point");
        map.getOverlays().add(marker);
        currentMarker = marker;
    }

    private void deleteCurrentMarker()
    {
        if (currentMarker != null)
        {
            map.getOverlays().remove(currentMarker);
        }
    }
}