package fi.tuni.cryptobag;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
    List<Currency> searchCurrencies;
    Currency selectedCurrency;
    ArrayAdapter currenciesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_currency);
        Debug.print(TAG, "AddCurrencyActivity()", "onCreate", 1);

        if (currencies == null || currencies.size() <= 0) {
            Debug.print(TAG, "AddCurrencyActivity()", "currencies == null", 1);
            currencies = new ArrayList<Currency>();
            FetchDataTask process = new FetchDataTask();
            process.execute();
        }

        searchCurrencies = new ArrayList<Currency>();

        addCurrencyEditText = (EditText) findViewById(R.id.addCurrencyEditText);
        addCurrencyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Debug.print(TAG, "addCurrencyEditText.addTextChangedListener", "onTextChanged", 3);

                String search = addCurrencyEditText.getText().toString().toLowerCase().trim();
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
                Debug.print(TAG, "setOnItemClickListener", "selectedCurrency" + selectedCurrency, 3);
                onBackPressed();
            }
        });

        currenciesAdapter = new ArrayAdapter(this, R.layout.currency_item, R.id.currencyItemTextView, searchCurrencies) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.currency_item, parent, false);
                Currency currency = (Currency) getItem(position);// selectedCurrenciesListView.getItemAtPosition(position);
                TextView currencyItemTextView = (TextView) rowView.findViewById(R.id.currencyItemTextView);
                TextView profitItemTextView = (TextView) rowView.findViewById(R.id.profitItemTextView);
                //TextView priceTextView = (TextView) rowView.findViewById(R.id.priceTextView);
                ImageView imageView = (ImageView) rowView.findViewById(R.id.iconImageView);
                if(currency.getIcon() != null) {
                    //Bitmap bitmap = currency.getBitmap();
                    imageView.setImageBitmap(currency.getBitmap());
                }
                currencyItemTextView.setText(currency.toString());
                //Debug.print(TAG, "selectedCurrencyArrayAdapter",  "position: " + position + currencyItemTextView.getText(), 3);
                return rowView;//super.getView(position, convertView, parent);
            }
        };
        currenciesListView.setAdapter(currenciesAdapter);

        search("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCurrenciesFile();
    }

    public void search(String search) {
        Debug.print(TAG, "search()", "currencies " + currencies.size(), 1);
        searchCurrencies.clear();
        for (Currency currency : currencies) {
            if (currency.getId().contains(search) || currency.getSymbol().contains(search) || currency.getName().contains(search)){
                searchCurrencies.add(currency);
            }
        }
        if(searchCurrencies.size() < 5) {
            apiService.fetch(searchCurrencies, HIGH_PRIORITY);
        } else {
            apiService.fetch(searchCurrencies, LOW_PRIORITY);
        }

        currenciesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Debug.print(TAG, "onBackPressed()", "selectedCurrency " + selectedCurrency, 1);

        Intent intent = new Intent();
        int selectedCurrencyIndex = currencies.indexOf(selectedCurrency);
        Debug.print(TAG, "onBackPressed()", "selectedCurrency id: " + selectedCurrencyIndex, 1);
        intent.putExtra("selCurIndex", selectedCurrencyIndex);
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

                    Currency currency = new Currency(jsonObject.get("id").toString(), jsonObject.get("symbol").toString(),jsonObject.get("name").toString());
                    currencies.add(currency);
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
