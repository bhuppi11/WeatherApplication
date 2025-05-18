package com.zai.weather.enums;

import java.util.HashMap;
import java.util.Map;

public enum City {

    BRISBANE(-27, 153),
    MELBOURNE(44, 10),
    PERTH(-31, 115),
    SYDNEY(-33, 151);

    private final int latitude;
    private final int longitude;

    City(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLatitude() {
        return this.latitude;
    }

    public int getLongitude() {
        return this.longitude;
    }

    public static final Map<String, City> cityMap = new HashMap<>() {{
        put("brisbane", City.BRISBANE);
        put("melbourne", City.MELBOURNE);
        put("perth", City.PERTH);
        put("sydney", City.SYDNEY);
    }};

}