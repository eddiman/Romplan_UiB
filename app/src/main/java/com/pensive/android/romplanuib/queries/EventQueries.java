package com.pensive.android.romplanuib.queries;

import android.content.Context;

import com.pensive.android.romplanuib.models.CalActivity;
import com.pensive.android.romplanuib.models.Room;
import com.pensive.android.romplanuib.models.messages.CalendarActivityListEvent;
import com.pensive.android.romplanuib.util.DataManager;
import com.pensive.android.romplanuib.util.io.ApiClient;
import com.pensive.android.romplanuib.util.io.ApiInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventQueries {
    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    DataManager dataManager;

    public void getEvents(final Context context, final Room room, final int weekNumber, final int year){
        dataManager = new DataManager();
        Call<List<CalActivity>> call = apiService.getWeekScheduleForRoom(room.getUniversityID(), room.getAreaID(), room.getBuildingID(), room.getRoomID(), weekNumber, year);
        call.enqueue(new Callback<List<CalActivity>>() {
            @Override
            public void onResponse(Call<List<CalActivity>> call, Response<List<CalActivity>> response) {
                if (response.code() == 200){
                    EventBus.getDefault().post(new CalendarActivityListEvent(response.body()));
                    
                }
            }

            @Override
            public void onFailure(Call<List<CalActivity>> call, Throwable t) {

            }
        });
    }
}
