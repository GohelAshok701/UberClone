package google.com.uberclone.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LocationDAO {
    @Insert
    public void insert(LocatioModel locatioModel);

    @Query("SELECT * FROM LocationDetail")
    public List<LocatioModel> getLocationDetails();

    @Query("DELETE FROM LocationDetail")
    public void deleteTable();

    @Query("SELECT SUM(meter) FROM LocationDetail")
    public String getSum();

}