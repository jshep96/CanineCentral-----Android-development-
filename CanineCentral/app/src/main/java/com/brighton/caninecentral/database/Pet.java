package com.brighton.caninecentral.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Pet {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String petName;
    @ColumnInfo
    public String petBreed;
    @ColumnInfo
    public String petWeight;
    @ColumnInfo
    public String petSize;





}
