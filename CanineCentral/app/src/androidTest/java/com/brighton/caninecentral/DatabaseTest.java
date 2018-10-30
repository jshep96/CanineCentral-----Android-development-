package com.brighton.caninecentral;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.brighton.caninecentral.database.Database;
import com.brighton.caninecentral.database.LocationDoa;
import com.brighton.caninecentral.database.LocationPoint;
import com.brighton.caninecentral.database.Pet;
import com.brighton.caninecentral.database.PetDoa;
import com.brighton.caninecentral.database.PetRouteJoin;
import com.brighton.caninecentral.database.PetRouteJoinDoa;
import com.brighton.caninecentral.database.RouteDoa;
import com.brighton.caninecentral.database.Routes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private RouteDoa routeDoa;
    private LocationDoa locationDoa;
    private PetDoa petDoa;
    private PetRouteJoinDoa petRouteJoinDoa;
    private Database mDatabase;

    @Before
    public void createDatabase() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDatabase = Room.inMemoryDatabaseBuilder(context, Database.class).build();
        routeDoa = mDatabase.routeDoa();
        locationDoa = mDatabase.locationDoa();
        petDoa = mDatabase.petDoa();
        petRouteJoinDoa = mDatabase.petRouteJoinDoa();
    }

    @After
    public void closeDatabase() {
        mDatabase.close();
    }

    @Test
    public void writeAndReadPet() {
        Pet pet = new Pet();
        pet.id = 1;
        pet.petName = "jessie";
        pet.petWeight = "50";
        pet.petSize = "small";
        pet.petBreed = "husky";

        petDoa.addPet(pet);

        List<String> allPets = petDoa.getPets();
        assertThat(allPets.get(0), equalTo(pet.petName));

        Pet byName = petDoa.getPet(pet.petName);
        assertThat(byName.petBreed, equalTo(pet.petBreed));
        assertThat(byName.id, equalTo(pet.id));
        assertThat(byName.petSize, equalTo(pet.petSize));
        assertThat(byName.petWeight, equalTo(pet.petWeight));

        int getId = petDoa.getId(pet.petName);
        assertThat(getId, equalTo(pet.id));
    }

    @Test
    public void writeAndReadRouteJoin() {
        Pet pet = new Pet();
        pet.id = 1;
        pet.petName = "jessie";
        pet.petWeight = "50";
        pet.petSize = "small";
        pet.petBreed = "husky";

        petDoa.addPet(pet);

        Routes route = new Routes();
        route.id = 1;
        route.date="03/05/2018";
        route.distance=1.56f;
        route.time = 15;

        routeDoa.addRoute(route);

        PetRouteJoin petRouteJoin = new PetRouteJoin();
        petRouteJoin.petId = pet.id;
        petRouteJoin.routeId= route.id;
        petRouteJoinDoa.insert(petRouteJoin);

        List<Routes> routesByPetId= petRouteJoinDoa.getRoutes(pet.id);
        assertThat(routesByPetId.get(0).date, equalTo(route.date));
        assertThat(routesByPetId.get(0).distance, equalTo(route.distance));
        assertThat(routesByPetId.get(0).id, equalTo(route.id));
        assertThat(routesByPetId.get(0).time, equalTo(route.time));

        float byDistance = petRouteJoinDoa.totalDistance(route.date, pet.id);
        assertThat(byDistance, equalTo(route.distance));

    }

    @Test
    public void testLocationPoints() {

        Routes route = new Routes();
        route.id = 1;
        route.date="03/05/2018";
        route.distance=1.56f;
        route.time = 15;

        routeDoa.addRoute(route);

        String[] latlngs = new String[] {"17.488280,14.635156", "15.488280,13.635156", "17.2,14.1" ,"11.488280,12.635156"};

        LocationPoint locationPoint = new LocationPoint();
        locationPoint.latlngPoint = latlngs[0];
        locationPoint.routeId = route.id;
        locationDoa.addLocation(locationPoint);

        locationPoint.latlngPoint = latlngs[1];
        locationPoint.routeId = route.id;
        locationDoa.addLocation(locationPoint);

        locationPoint.latlngPoint = latlngs[2];
        locationPoint.routeId = route.id;
        locationDoa.addLocation(locationPoint);

        locationPoint.latlngPoint = latlngs[3];
        locationPoint.routeId = route.id;
        locationDoa.addLocation(locationPoint);

        List<LocationPoint> locationById = locationDoa.getLocations(route.id);
        for(int i = 0; i<locationById.size(); i++) {
            assertThat(locationById.get(i).latlngPoint, equalTo(latlngs[i]));
        }

    }

}
