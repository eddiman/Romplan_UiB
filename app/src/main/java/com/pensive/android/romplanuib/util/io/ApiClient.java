package com.pensive.android.romplanuib.util.io;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A client for communication with the backend
 * @author Fredrik V. Heimsæter
 */

public class ApiClient {
    public static final String API_BASE_URL = "https://timeplanleggar.no/api/";

    private static Retrofit retrofit = null;

    /**
     * Get a client so you can work with the backend
     * @return The client
     */
    public static Retrofit getClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}