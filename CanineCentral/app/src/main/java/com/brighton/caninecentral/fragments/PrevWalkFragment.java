package com.brighton.caninecentral.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.brighton.caninecentral.MainActivity;
import com.brighton.caninecentral.RouteAdapter;
import com.brighton.caninecentral.caninecentral.R;
import com.brighton.caninecentral.database.Pet;
import com.brighton.caninecentral.database.Routes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PrevWalkFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private TextView weekDistance;
    private TextView dayDistance;
    private TextView weekGoal;
    private TextView dayGoal;
    private Spinner pets;
    private List<Routes> dataSet;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private int petId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_prev_walk, null);

        weekDistance = v.findViewById(R.id.weekly_distance);
        dayDistance = v.findViewById(R.id.today_distance);
        weekGoal = v.findViewById(R.id.weekly_goal);
        dayGoal = v.findViewById(R.id.today_goal);
        pets = v.findViewById(R.id.pet_route_spinner);
        pets.setOnItemSelectedListener(this);
        recyclerView = v.findViewById(R.id.route_recycler_view);

        loadSpinner();
        setGoal();

        return v;
    }

    private void setDistance() {
        float weeklyDistance = 0;
        float dailyDistance = 0;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date today = calendar.getTime();
        String todayDate = formatter.format(today);

        dailyDistance = MainActivity.database.petRouteJoinDoa().totalDistance(todayDate, petId);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 7; i++) {
            Date date = calendar.getTime();
            String sDate = formatter.format(date);
            weeklyDistance += MainActivity.database.petRouteJoinDoa().totalDistance(sDate, petId);
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        dayDistance.setText(String.format("%.2f Km", dailyDistance));
        weekDistance.setText(String.format("%.2f Km", weeklyDistance));
    }


    private void setGoal() {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int dGoal = mSharedPreferences.getInt("dailyGoal", 0);
        int wGoal = mSharedPreferences.getInt("weeklyGoal", 0);

        dayGoal.setText(String.format("%d Km", dGoal));
        weekGoal.setText(String.format("%d Km", wGoal));

    }

    private void loadSpinner() {
        List<String> petNames = MainActivity.database.petDoa().getPets();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, petNames );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pets.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String label = adapterView.getItemAtPosition(i).toString();
        petId = MainActivity.database.petDoa().getId(label);

        dataSet = MainActivity.database.petRouteJoinDoa().getRoutes(petId);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Collections.reverse(dataSet);
        adapter = new RouteAdapter(getActivity(), dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        setDistance();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

