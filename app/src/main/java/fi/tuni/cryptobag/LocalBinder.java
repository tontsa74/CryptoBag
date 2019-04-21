package fi.tuni.cryptobag;

import android.os.Binder;

/**
 * The type Local binder for APIService.
 */
class LocalBinder extends Binder {
    private static final String TAG = "fi.tuni.APIService";
    private APIService apiService;

    /**
     * Instantiates a new Local binder.
     *
     * @param apiService the api service
     */
    LocalBinder(APIService apiService) {

        Debug.print(TAG, "LocalBinder", "LocalBinder", 1);
        this.apiService = apiService;
    }

    /**
     * Gets api service.
     *
     * @return the api service
     */
    APIService getApiService() {
        Debug.print(TAG, "LocalBinder", "getApiService", 1);
        return this.apiService;
    }
}