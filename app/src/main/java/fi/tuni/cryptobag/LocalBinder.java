package fi.tuni.cryptobag;

import android.os.Binder;

/**
 * The type Local binder.
 */
public class LocalBinder extends Binder {
    private static final String TAG = "tsilve.APIService";
    private APIService apiService;

    /**
     * Instantiates a new Local binder.
     *
     * @param apiService the api service
     */
    public LocalBinder(APIService apiService) {

        Debug.print(TAG, "LocalBinder", "LocalBinder", 1);
        this.apiService = apiService;
    }

    /**
     * Gets api service.
     *
     * @return the api service
     */
    public APIService getApiService() {
        Debug.print(TAG, "LocalBinder", "getApiService", 1);
        return this.apiService;
    }
}