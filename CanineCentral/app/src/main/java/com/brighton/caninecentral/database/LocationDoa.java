package com.brighton.caninecentral.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LocationDoa {

    @Insert
    public void addLocation(LocationPoint locationPoint);

    @Query("select * from LocationPoint where route_id = :id")
    public List<LocationPoint> getLocations(int id);
}
