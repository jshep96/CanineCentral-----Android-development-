package com.brighton.caninecentral.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface  RouteDoa {

    @Insert
    long addRoute(Routes route);
}
