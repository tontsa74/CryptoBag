package fi.tuni.cryptobag;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static fi.tuni.cryptobag.BaseActivity.HIGH_PRIORITY;
import static fi.tuni.cryptobag.BaseActivity.LOW_PRIORITY;
import static fi.tuni.cryptobag.BaseActivity.MEDIUM_PRIORITY;
import static fi.tuni.cryptobag.BaseActivity.currenciesAdapter;
import static fi.tuni.cryptobag.BaseActivity.fetchCount;
import static fi.tuni.cryptobag.BaseActivity.highToFetch;
import static fi.tuni.cryptobag.BaseActivity.lowToFetch;
import static fi.tuni.cryptobag.BaseActivity.mediumToFetch;
import static fi.tuni.cryptobag.BaseActivity.selectedCurrencies;
import static fi.tuni.cryptobag.BaseActivity.selectedCurrencyArrayAdapter;

public class APIService extends Service {
    private static final String TAG = "tsilve.APIService";
    private IBinder iBinder;

    FetchTask process;
    Set<Currency> currencyToFetch;
    boolean running;



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
        currencyToFetch = new LinkedHashSet<>();

        lowToFetch = new LinkedHashSet<>();
        mediumToFetch = new LinkedHashSet<>();
        highToFetch = new LinkedHashSet<>();

        process = new FetchTask();
        fetchCount = 0;
        iBinder = new LocalBinder(this);
    }

    public void fetch(List<Currency> fetchList, int priority) {
        Debug.print(TAG, "APIService", "fetch, priority: " + priority, 1);
        if(priority == HIGH_PRIORITY) {
            highToFetch.addAll(fetchList);
        } else if(priority == MEDIUM_PRIORITY) {
            mediumToFetch.addAll(fetchList);
        } else if(priority == LOW_PRIORITY) {
            lowToFetch.addAll(fetchList);
        }
        if(!running) {
            prioritizeFetchTasks();
        }

    }

    public void fetch(Currency fetchCurrency, int priority) {
        Debug.print(TAG, "APIService", "fetch, priority: " + priority, 1);
        if(priority == 1) {
            highToFetch.add(fetchCurrency);
        } else if(priority == 2) {
            mediumToFetch.add(fetchCurrency);
        } else if(priority == 3) {
            lowToFetch.add(fetchCurrency);
        }
        if(!running) {
            prioritizeFetchTasks();
        }
    }

    public void prioritizeFetchTasks() {
        Debug.print(TAG, "APIService", "prioritizeFetchTasks", 1);
        //if(highToFetch.size()+mediumToFetch.size()+lowToFetch.size() > 0) {
            if(fetchCount <= 0) {
                Debug.print(TAG, "APIService", "while", 1);
                Currency fetchNext = null;
                if(highToFetch.iterator().hasNext()) {
                    Debug.print(TAG, "APIService", "highToFetch", 3);
                    fetchNext = highToFetch.iterator().next();
                    highToFetch.remove(fetchNext);

                } else if(mediumToFetch.size() > 0) {
                    Debug.print(TAG, "APIService", "mediumToFetch", 3);

                    fetchNext = mediumToFetch.iterator().next();
                    //process.execute(fetchNext);
                    mediumToFetch.remove(fetchNext);
                    //Currency[] fetchCurrencies = mediumToFetch.toArray(new Currency[mediumToFetch.size()]);
                    //process.execute(fetchCurrencies);
                    //mediumToFetch.clear();

                } else if(lowToFetch.size() > 0) {
                    Debug.print(TAG, "APIService", "lowToFetch", 3);
                    fetchNext = lowToFetch.iterator().next();
                    lowToFetch.remove(fetchNext);

                }
                if(fetchNext != null) {
                    new FetchTask().execute(fetchNext);
                }
            }
        //}
    }

    public class FetchTask extends AsyncTask<Currency,Void,Void> {
        @Override
        protected Void doInBackground(Currency... fetchCurrencies) {
            running = true;
            fetchCount += fetchCurrencies.length;
            try {
                //Debug.print(TAG, "FetchTask", "doInBackground: " + fetchCount, 2);

                for (Currency c : fetchCurrencies) {
                    //Debug.print(TAG, "FetchTask", "doInBackground: " + c, 2);
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
                        Debug.print("tsilve","FetchCurrencyTask", c.getSymbol() + ", price: " + price + " sizes: " + highToFetch.size() + ", " + mediumToFetch.size() + ", " + lowToFetch.size(),4);
                        c.setPrice(new BigDecimal(price.toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Debug.print("tsilve","FetchCurrencyTask", "price: " + e ,4);
                    }
                    fetchCount--;
                    Thread.sleep(1000);

                    if(highToFetch.size()+mediumToFetch.size()+lowToFetch.size() > 0) {
                        prioritizeFetchTasks();
                    } else {
                        Thread.sleep(10000);
                        lowToFetch.addAll(selectedCurrencies);
                        prioritizeFetchTasks();
                    }
                }
            } catch (Exception e) {
                Debug.print("tsilve", "FetchCurrencyTask()", "Exception " + e, 2);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Debug.print(TAG, "FetchTask", "onPostExecute: " + fetchCount, 2);
            super.onPostExecute(aVoid);
            if(selectedCurrencyArrayAdapter != null) {
                selectedCurrencyArrayAdapter.notifyDataSetChanged();
            }

            if(currenciesAdapter != null) {
                currenciesAdapter.notifyDataSetChanged();
            }
        }
    }
}