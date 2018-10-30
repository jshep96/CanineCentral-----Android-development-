package com.brighton.caninecentral.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PetDoa {

    @Insert
    void addPet(Pet pet);

    @Query("select * from Pet where petName = :petName" )
    Pet getPet(String petName);

    @Query("select petName from Pet")
    List<String> getPets();

    @Query("select id from Pet where petName = :petName")
    int getId(String petName);
}
