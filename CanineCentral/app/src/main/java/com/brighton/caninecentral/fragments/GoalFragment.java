package com.brighton.caninecentral.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brighton.caninecentral.caninecentral.R;

public class GoalFragment extends Fragment {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private EditText weeklyGoal;
    private EditText dailyGoal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goal, null);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = mPreferences.edit();

        Button setDailyBtn = v.findViewById(R.id.setDailyGoal);
        Button setWeeklyBtn = v.findViewById(R.id.setWeeklyGoal);
        dailyGoal = v.findViewById(R.id.editDailyGoal);
        weeklyGoal = v.findViewById(R.id.editWeeklyGoal);


        setDailyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDaily();
            }
        });
        setWeeklyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWeekly();
            }
        });

        return v;
    }


    private void saveDaily() {
        String str = dailyGoal.getText().toString();

        if(! TextUtils.isEmpty(str)) {
            int daily = Integer.parseInt(str);
            editor.putInt("dailyGoal", daily);
            editor.commit();
            Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "No value entered!", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveWeekly() {
        String str = weeklyGoal.getText().toString();

        if(! TextUtils.isEmpty(str)) {
            int weekly = Integer.parseInt(str);
            editor.putInt("weeklyGoal", weekly);
            editor.commit();
            Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "No value entered!", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}