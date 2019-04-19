package fi.tuni.cryptobag;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static fi.tuni.cryptobag.BaseActivity.HIGH_PRIORITY;
import static fi.tuni.cryptobag.BaseActivity.LOW_PRIORITY;
import static fi.tuni.cryptobag.BaseActivity.MEDIUM_PRIORITY;
import static fi.tuni.cryptobag.BaseActivity.fetchCount;

import static fi.tuni.cryptobag.BaseActivity.initCurrencies;

public class APIService extends Service {
    private static final String TAG = "tsilve.APIService";
    private IBinder iBinder;

    FetchTask process;
    Set<Currency> currencyToFetch;
    boolean running;

    static Set<Currency> lowToFetch;
    static Set<Currency> mediumToFetch;
    static Set<Currency> highToFetch;

    LocalBroadcastManager manager;
    Intent intent;

    @Override
    public IBinder onBind(Intent intent) {
        Debug.print(TAG, "APIService", "onBind", 1);
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

        if (lowToFetch == null) {
            lowToFetch = new LinkedHashSet<>();
        }
        if (mediumToFetch == null) {
            mediumToFetch = new LinkedHashSet<>();
        }
        if (highToFetch == null) {
            highToFetch = new LinkedHashSet<>();
        }




        process = new FetchTask();
        fetchCount = 0;
        iBinder = new LocalBinder(this);

        manager = LocalBroadcastManager.getInstance(this);
        intent = new Intent("tsilve.api");
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
        if(priority == HIGH_PRIORITY) {
            highToFetch.add(fetchCurrency);
        } else if(priority == MEDIUM_PRIORITY) {
            mediumToFetch.add(fetchCurrency);
        } else if(priority == LOW_PRIORITY) {
            lowToFetch.add(fetchCurrency);
        }
        if(!running) {
            prioritizeFetchTasks();
        }
    }

    public void prioritizeFetchTasks() {
        Debug.print(TAG, "APIService", "prioritizeFetchTasks", 1);
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
                    mediumToFetch.remove(fetchNext);

                } else if(lowToFetch.size() > 0) {
                    Debug.print(TAG, "APIService", "lowToFetch", 3);
                    fetchNext = lowToFetch.iterator().next();
                    lowToFetch.remove(fetchNext);

                }
                if(fetchNext != null) {
                    new FetchTask().execute(fetchNext);
                }
            }
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
                        String imageUrl = jsonO.getJSONObject("image").getString("thumb");
                        URL tempUrl = new URL(imageUrl);
                        Bitmap bitmap = BitmapFactory.decodeStream(tempUrl.openConnection().getInputStream());
                        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                        byte[] icon = bStream.toByteArray();


                        //c.setIcon(icon);
                        Debug.print("tsilve","FetchCurrencyTask", c + ", price: " + price + " sizes: " + highToFetch.size() + ", " + mediumToFetch.size() + ", " + lowToFetch.size(),4);
                        //c.setPrice(new BigDecimal(price.toString()));

                        intent.putExtra("currency", c);
                        intent.putExtra("price", price);
                        intent.putExtra("imageUrl", imageUrl);
                        intent.putExtra("icon", icon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Debug.print("tsilve","FetchCurrencyTask", "exception: " + e ,4);
                    }
                    fetchCount--;
                    Thread.sleep(1000);

                    if(highToFetch.size()+mediumToFetch.size()+lowToFetch.size() > 0) {
                        prioritizeFetchTasks();
                    } else {
                        Thread.sleep(10000);
                        lowToFetch.addAll(initCurrencies);
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
            super.onPostExecute(aVoid);
            manager.sendBroadcast(intent);
        }
    }
}