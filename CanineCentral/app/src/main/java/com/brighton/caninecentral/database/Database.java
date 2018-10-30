package com.brighton.caninecentral.database;

import android.arch.persistence.room.RoomDatabase;

@android.arch.persistence.room.Database(entities = {Routes.class, LocationPoint.class, Pet.class, PetRouteJoin.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    public abstract RouteDoa routeDoa();
    public abstract LocationDoa locationDoa();
    public abstract PetDoa petDoa();
    public abstract PetRouteJoinDoa petRouteJoinDoa();
}
