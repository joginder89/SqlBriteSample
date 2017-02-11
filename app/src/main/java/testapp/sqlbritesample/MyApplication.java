package testapp.sqlbritesample;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import rx.schedulers.Schedulers;
import testapp.sqlbritesample.database.DataManager;
import testapp.sqlbritesample.retrofit.MyApiEndpointInterface;
import testapp.sqlbritesample.retrofit.RetrofitServiceGenerator;

public class MyApplication extends Application {

    private Typeface typeface;
    private static MyApplication sMyApplication;
    private DataManager mDataManager;
    private MyApiEndpointInterface apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        sMyApplication = this;
        mDataManager = new DataManager(this, Schedulers.io());
    }

    @Override
    public void onTerminate() {
        sMyApplication = null;
        super.onTerminate();
    }

    public static MyApplication get() {
        return sMyApplication;
    }

    public DataManager getDataManager() { return mDataManager; }

    public MyApiEndpointInterface getApiService() {
        if(apiService == null) {
            apiService = RetrofitServiceGenerator.createService(MyApiEndpointInterface.class);
        }

        return apiService;
    }

    public SharedPreferences getDefaultSharedPref() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }
}
