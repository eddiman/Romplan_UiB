package com.pensive.android.romplanuib.util.io;

import com.pensive.android.romplanuib.models.Area;
import com.pensive.android.romplanuib.models.CalActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ApiInterface {

    /**
     *
     * @param university
     * @return
     */
    @GET("uni/{university}")
    Call<List<Area>> getUniversity(@Path("university") String university);

    /**
     *
     * @param university
     * @param area
     * @param building
     * @param room
     * @param week
     * @param year
     * @return
     */
    @GET("schedule/{university}/{area}/{building}/{room}/{week}/{year}")
    Call<List<CalActivity>> getWeekScheduleForRoom(@Path("university") String university, @Path("area") String area, @Path("building") String building, @Path("room") String room, @Path("week") int week, @Path("year") int year);

}
