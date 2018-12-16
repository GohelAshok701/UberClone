package google.com.uberclone.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "LocationDetail")
public class LocatioModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String meter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    @Override
    public String toString() {
        return "LocatioModel{" +
                "id=" + id +
                ", meter='" + meter + '\'' +
                '}';
    }
}