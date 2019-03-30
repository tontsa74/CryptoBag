package fi.tuni.cryptobag;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddCurrency extends BaseActivity {
    EditText addCurrencyEditText;
    ListView currenciesListView;
    ArrayAdapter currenciesAdapter;
    List<Currency> currencies;
    List<Currency> searchCurrencies;
    Currency selectedCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_currency);
        Debug.print(TAG, "onCreate()", "AddCurrencyActivity", 1);

        currencies = new ArrayList<Currency>();
        searchCurrencies = new ArrayList<Currency>();

        addCurrencyEditText = (EditText) findViewById(R.id.addCurrencyEditText);
        addCurrencyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Debug.print(TAG, "addCurrencyEditText.addTextChangedListener", "onTextChanged", 3);

                String search = addCurrencyEditText.getText().toString().toLowerCase();
                search(search);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        currenciesListView = (ListView) findViewById(R.id.currenciesListView);
        currenciesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Debug.print(TAG, "setOnItemClickListener", "" + currenciesListView.getItemAtPosition(position), 3);
                selectedCurrency = (Currency) currenciesListView.getItemAtPosition(position);
                onBackPressed();
            }
        });

        currenciesAdapter = new ArrayAdapter<>(this, R.layout.bag_item, R.id.bagItemTextView, searchCurrencies);
        currenciesListView.setAdapter(currenciesAdapter);

        FetchDataTask process = new FetchDataTask();
        process.execute();
    }

    public void search(String search) {
        Debug.print(TAG, "search()", "currencies " + currencies.size(), 1);
        searchCurrencies.clear();
        for (Currency currency : currencies) {
            if (currency.getId().contains(search)){
                searchCurrencies.add(currency);
            }
        }
        currenciesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Debug.print(TAG, "onBackPressed()", "selectedCurrency " + selectedCurrency, 1);

        Intent intent = new Intent();
        intent.putExtra("selectedCurrency", selectedCurrency);
        if (selectedCurrency != null) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }

        super.onBackPressed();
    }

    public class FetchDataTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://api.coingecko.com/api/v3/coins/list");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String data ="";
                String line = "";
                while(line != null){
                    line = bufferedReader.readLine();
                    data = data + line;
                }

                JSONArray jsonArray = new JSONArray(data);
                for(int i = 0 ;i < jsonArray.length(); i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    //if (jsonObject.get("id").toString().contains(CurrenciesActivity.search)) {
                    Currency currency = new Currency(jsonObject.get("id").toString(), jsonObject.get("symbol").toString(),jsonObject.get("name").toString());
                    currencies.add(currency);
                    //}
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            currenciesAdapter.addAll(currencies);
            currenciesAdapter.notifyDataSetChanged();

        }
    }
}
