package com.example.mostafaeisam.mvcpattrenweather.responses;

import com.example.mostafaeisam.mvcpattrenweather.classes.Current;
import com.example.mostafaeisam.mvcpattrenweather.classes.Location;
import com.google.gson.annotations.SerializedName;

import java.util.Observable;

public class GetWeatherResponseModel {
    @SerializedName("location")
    private Location location;
    @SerializedName("current")
    private Current current;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }


}
