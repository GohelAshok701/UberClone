package google.com.uberclone.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {LocatioModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDAO locationDAO();
}