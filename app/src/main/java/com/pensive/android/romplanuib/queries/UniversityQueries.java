package com.pensive.android.romplanuib.queries;

import android.content.Context;
import android.widget.Toast;

import com.pensive.android.romplanuib.R;
import com.pensive.android.romplanuib.models.Area;
import com.pensive.android.romplanuib.models.University;
import com.pensive.android.romplanuib.models.messages.UniversityEvent;
import com.pensive.android.romplanuib.util.io.ApiClient;
import com.pensive.android.romplanuib.util.io.ApiInterface;
import com.pensive.android.romplanuib.util.DataManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Fredrik Heimsæter
 * @version 1.0
 */
public class UniversityQueries {
    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    DataManager dataManager;

    public void getUniversity(final Context context, final University university) {
        dataManager = new DataManager();
        Call<List<Area>> call = apiService.getUniversity(university.getCampusCode());
        call.enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                if(response.code() == 200){
                    university.setAreas(response.body());
                    EventBus.getDefault().post(new UniversityEvent(university));
                    dataManager.storeObjectInSharedPref(context, "university", university);

                }
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                Toast.makeText(context, R.string.something_wrong, Toast.LENGTH_LONG);

            }
        });
    }

}
