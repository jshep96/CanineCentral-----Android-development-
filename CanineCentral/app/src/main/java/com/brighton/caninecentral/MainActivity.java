package com.brighton.caninecentral;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.brighton.caninecentral.caninecentral.R;
import com.brighton.caninecentral.database.Database;
import com.brighton.caninecentral.fragments.GoalFragment;
import com.brighton.caninecentral.fragments.PetFragment;
import com.brighton.caninecentral.fragments.PrevWalkFragment;
import com.brighton.caninecentral.fragments.WalkFragment;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room.databaseBuilder(getApplicationContext(), Database.class, "appDB").allowMainThreadQueries().build();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().getItem(1).setChecked(true);


        loadFrag(new WalkFragment());
    }



    private boolean loadFrag(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch(item.getItemId()) {

            case R.id.navigation_walk:
                fragment = new WalkFragment();
                break;

            case R.id.navigation_prev_walk:
                fragment = new PrevWalkFragment();
                break;

            case R.id.navigation_goal:
                fragment = new GoalFragment();
                break;

            case R.id.navigation_pets:
                fragment = new PetFragment();
                break;
        }
        return loadFrag(fragment);
    }
}
