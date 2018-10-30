package com.brighton.caninecentral.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity (primaryKeys = {"petId", "routeId"},
         foreignKeys = {@ForeignKey(entity = Pet.class,
                                    parentColumns = "id",
                                    childColumns = "petId"),
                        @ForeignKey(entity = Routes.class,
                                    parentColumns = "id",
                                    childColumns = "routeId")
         })
public class PetRouteJoin {

    public int petId;

    public int routeId;
}
