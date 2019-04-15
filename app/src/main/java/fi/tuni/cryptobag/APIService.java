package fi.tuni.cryptobag;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static fi.tuni.cryptobag.BaseActivity.selectedCurrencies;

public class APIService extends Service {
    private static final String TAG = "tsilve.APIService";
    private IBinder iBinder;
    @Override
    public IBinder onBind(Intent intent) {
        Debug.print(TAG, "APIService", "onBind", 1);
        // Returns IBinder, which "wraps" APIService inside..
        return iBinder;
    }
    @Override
    public void onDestroy() {
        Debug.print(TAG, "APIService", "onDestroy", 1);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Debug.print(TAG, "APIService", "onStartCommand", 1);
        return START_STICKY;
    }
    @Override
    public void onCreate() {
        Debug.print(TAG, "APIService", "onCreate", 1);
        iBinder = new LocalBinder(this);
    }
    public double getRandomNumber() {
        Debug.print(TAG, "APIService", "getRandomNumber", 1);
        return Math.random();
    }

    public void fetch() {
        Debug.print(TAG, "APIService", "fetch", 1);

        FetchTask process = new FetchTask();
        process.execute();
    }
    public class FetchTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (Currency c : selectedCurrencies) {
                    String str = "https://api.coingecko.com/api/v3/coins/" + c.getId();
                    URL url = new URL(str);
                    HttpURLConnection httpURLConnection2 = (HttpURLConnection) url.openConnection();
                    InputStream inputStream2 = httpURLConnection2.getInputStream();
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2));
                    String data ="";
                    String line = "";
                    while(line != null){
                        line = bufferedReader2.readLine();
                        data = data + line;
                    }

                    try {
                        JSONObject jsonO = new JSONObject(data);
                        Double price = jsonO.getJSONObject("market_data").getJSONObject("current_price").getDouble("eur");
                        Debug.print("tsilve","FetchCurrencyTask", c.getSymbol() + ", price: " + price ,4);
                        c.setPrice(price.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Debug.print("tsilve","FetchCurrencyTask", "price: " + e ,4);
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                Debug.print("tsilve", "FetchCurrencyTask()", "Exception " + e, 2);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}