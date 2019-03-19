package com.yk.bike.utils;

import com.yk.bike.callback.ResponseListener;
import com.yk.bike.response.BikeTypeListResponse;
import com.yk.bike.response.BikeTypeResponse;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.yk.bike.utils.NullObjectUtils.isResponseSuccess;

public class StaticUtils {
    private static StaticUtils instance;

    private StaticUtils() {
    }

    public static StaticUtils getInstance() {
        if (instance == null) {
            synchronized (StaticUtils.class) {
                if (instance == null) {
                    instance = new StaticUtils();
                }
            }
        }
        return instance;
    }

    public void init() {
        initBikeTypeMap();
    }

    public void relese() {
        instance = null;
        bikeTypeMap = null;
    }

    private LinkedHashMap<String, BikeTypeResponse.BikeType> bikeTypeMap;

    public void initBikeTypeMap() {
        ApiUtils.getInstance().findAllBikeType(new ResponseListener<BikeTypeListResponse>() {
            @Override
            public void onStart() {
                if (bikeTypeMap == null)
                    bikeTypeMap = new LinkedHashMap<>();
                else
                    bikeTypeMap.clear();
            }

            @Override
            public void onSuccess(BikeTypeListResponse bikeTypeListResponse) {
                if (isResponseSuccess(bikeTypeListResponse)) {
                    for (BikeTypeResponse.BikeType bikeType : bikeTypeListResponse.getData()) {
                        bikeTypeMap.put(bikeType.getTypeValue(), bikeType);
                    }
                }
            }
        });
    }

    public Map<String, BikeTypeResponse.BikeType> getBikeTypeMap() {
        return bikeTypeMap;
    }

    public StaticUtils setBikeTypeMap(LinkedHashMap<String, BikeTypeResponse.BikeType> bikeTypeMap) {
        this.bikeTypeMap = bikeTypeMap;
        return this;
    }

    public BikeTypeResponse.BikeType getBikeType(String key) {
        BikeTypeResponse.BikeType bikeType = this.bikeTypeMap.get(key);
        if (bikeType == null) {
            return new BikeTypeResponse.BikeType();
        }
        return bikeType;
    }

    public String getBikeTypeName(String key) {
        BikeTypeResponse.BikeType bikeType = this.bikeTypeMap.get(key);
        if (bikeType == null) {
            return key;
        }
        return bikeType.getTypeName();
    }

    public float getBikeTypeUnitPrice(String key) {
        BikeTypeResponse.BikeType bikeType = this.bikeTypeMap.get(key);
        if (bikeType == null) {
            return 0f;
        }
        return bikeType.getUnitPrice();
    }
}
