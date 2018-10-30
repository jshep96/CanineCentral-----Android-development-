package com.brighton.caninecentral.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.brighton.caninecentral.MainActivity;
import com.brighton.caninecentral.caninecentral.R;
import com.brighton.caninecentral.database.Pet;

public class AddPetFragment extends Fragment {

    EditText petName;
    EditText petBreed;
    EditText petWeight;
    Spinner petSize;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_pet, null);

        petName = v.findViewById(R.id.pet_name);
        petBreed = v.findViewById(R.id.pet_breed);
        petWeight = v.findViewById(R.id.pet_weight);
        petSize = v.findViewById(R.id.pet_size);
        Button addPet = v.findViewById(R.id.add_pet);

        loadSpinner();

        addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPet();
            }
        });

        return v;
    }


    private void loadSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.pet_size_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petSize.setAdapter(adapter);
    }


    private void addPet() {
        Pet newPet = new Pet();
        newPet.petName = petName.getText().toString();
        newPet.petBreed = petBreed.getText().toString();
        newPet.petWeight = petWeight.getText().toString();
        newPet.petSize = petSize.getSelectedItem().toString();

        MainActivity.database.petDoa().addPet(newPet);
        Toast.makeText(getActivity(), "Added pet", Toast.LENGTH_SHORT).show();
    }
}
