package com.brighton.caninecentral.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Routes {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;

    public float distance;

    public int time;
}


