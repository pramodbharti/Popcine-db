package com.example.pramod.popcine.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.pramod.popcine.model.Movie;
import com.example.pramod.popcine.utils.Constants;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                //Creating a new database instance.
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, Constants.DATABASE)
                        .build();
            }
        }
        //Getting the database instance.
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
