package com.brighton.caninecentral.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Routes.class,
        parentColumns = "id",
        childColumns = "route_id"),
        indices = {@Index("locationPointId"), @Index(value = {"route_id"})})
public class LocationPoint {

    @PrimaryKey(autoGenerate = true)
    public int locationPointId;

    @ColumnInfo(name = "route_id")
    public int routeId;

    public String latlngPoint;
}