package com.brighton.caninecentral.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PetRouteJoinDoa {

    @Insert
    void insert(PetRouteJoin petRouteJoin);

    @Query("select * from Routes inner join PetRouteJoin on" +
            " Routes.id = PetRouteJoin.routeId" +
            " where PetRouteJoin.petId = :petId")
    List<Routes> getRoutes(int petId);

    @Query("select sum(distance) from Routes  inner join PetRouteJoin " +
            "on Routes.id = PetRouteJoin.routeId " +
            "where PetRouteJoin.petId = :petId and Routes.date = :date")
    float totalDistance(String date, int petId);
}
