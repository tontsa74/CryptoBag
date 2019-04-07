package fi.tuni.cryptobag;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import static fi.tuni.cryptobag.BaseActivity.selectedCurrencies;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FetchService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "fi.tuni.cryptobag.action.FOO";
    private static final String ACTION_BAZ = "fi.tuni.cryptobag.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "fi.tuni.cryptobag.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "fi.tuni.cryptobag.extra.PARAM2";

    public FetchService() {
        super("FetchService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FetchService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FetchService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
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
                    System.out.println("tsilve. " + price);
                    c.setPrice(price.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Debug.print("tsilve","FetchCurrencyTask", "price: " + e ,3);
                }

            }
        } catch (Exception e) {
            Debug.print("tsilve", "FetchCurrencyTask()", "Exception " + e, 1);
        }


    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
