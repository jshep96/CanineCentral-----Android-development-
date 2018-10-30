package com.brighton.caninecentral.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.brighton.caninecentral.MainActivity;
import com.brighton.caninecentral.caninecentral.R;
import com.brighton.caninecentral.database.Pet;

import java.util.List;

public class PetFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private Spinner pets;
    private TextView breed;
    private TextView weight;
    private TextView size;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pet, null);

        breed = v.findViewById(R.id.show_breed);
        weight = v.findViewById(R.id.show_weight);
        size = v.findViewById(R.id.show_size);
        pets = v.findViewById(R.id.select_pet);
        pets.setOnItemSelectedListener(this);

        loadSpinner();

        ImageButton openAdd = v.findViewById(R.id.open_add_pet);
        openAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AddPetFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    }

    private void loadSpinner() {
        List<String> petNames = MainActivity.database.petDoa().getPets();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, petNames );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pets.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String label = adapterView.getItemAtPosition(i).toString();
        Pet pet = MainActivity.database.petDoa().getPet(label);
        breed.setText(pet.petBreed);
        weight.setText(pet.petWeight);
        size.setText(pet.petSize);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
