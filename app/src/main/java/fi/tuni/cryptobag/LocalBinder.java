package fi.tuni.cryptobag;

import android.os.Binder;

public class LocalBinder extends Binder {
    private static final String TAG = "tsilve.APIService";
    private APIService apiService;

    public LocalBinder(APIService apiService) {

        Debug.print(TAG, "LocalBinder", "LocalBinder", 1);
        this.apiService = apiService;
    }

    public APIService getApiService() {
        Debug.print(TAG, "LocalBinder", "getApiService", 1);
        return this.apiService;
    }
}