package fi.tuni.cryptobag;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {


    TextView totalProfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalProfit = (TextView) findViewById(R.id.totalProfit);

        Debug.loadDebug(this);
        Debug.print(TAG, "onCreate()", "Logging my application", 1);

        loadCurrenciesFile();
        loadSelectedCurrenciesFile();

        if (currencies == null) {
            currencies = new ArrayList<Currency>();
        }
        if (selectedCurrencies == null) {
            selectedCurrencies = new ArrayList<Currency>();
        }

        for (int i=0; i < currencies.size(); i++) {
            for (Currency selCur : selectedCurrencies) {

                if (selCur.getId().equals(currencies.get(i).getId())) {
                    currencies.set(i, selCur);
                }
            }
        }

        connectionToService = new ApiServiceConnection();

        double total = 0;
        for (Currency selCur : selectedCurrencies) {
            try {
                total += Double.parseDouble(selCur.getBag().getProfit());
            } catch(Exception e) {
                Debug.print(TAG, "onCreate",  "total ex: " + selCur + "_" + e, 3);
            }

        }
        totalProfit.setText("Total profit: " + total);

        final ListView selectedCurrenciesListView = (ListView) findViewById(R.id.selectedCurrenciesListView);
        selectedCurrenciesListView.setOnItemClickListener((parent, view, position, id) -> {
            Debug.print(TAG, "setOnItemClickListener",  "position_id: " + position + "_" + id, 2);
            Currency currency = (Currency) selectedCurrenciesListView.getItemAtPosition(position);
            editCurrency(currency, position);
        });

        selectedCurrencyArrayAdapter = new ArrayAdapter(this, R.layout.currency_item, R.id.currencyItemTextView, selectedCurrencies) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.currency_item, parent, false);
                Currency currency = (Currency) getItem(position);// selectedCurrenciesListView.getItemAtPosition(position);
                TextView currencyItemTextView = (TextView) rowView.findViewById(R.id.currencyItemTextView);
                TextView profitItemTextView = (TextView) rowView.findViewById(R.id.profitItemTextView);
                currencyItemTextView.setText(currency.getName());
                Double profit = Double.parseDouble(currency.getBag().getProfit());
                if(profit > 0) {
                    profitItemTextView.setTextColor(Color.GREEN);
                } else if(profit < 0) {
                    profitItemTextView.setTextColor(Color.RED);
                }
                profitItemTextView.setText(currency.getBag().getProfit());
                Debug.print(TAG, "selectedCurrencyArrayAdapter",  "position: " + position + currencyItemTextView.getText(), 3);
                return rowView;//super.getView(position, convertView, parent);
            }
        };
        selectedCurrenciesListView.setAdapter(selectedCurrencyArrayAdapter);

    }

    @Override
    protected void onStart() {
        Debug.print(TAG, "onStart()", "onStart", 1);
        super.onStart();
        Intent intent = new Intent(this, APIService.class);
        bindService(intent, connectionToService, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        Debug.print(TAG, "onStop()", "onStop", 1);
        super.onStop();
        if (isBounded) {
            unbindService(connectionToService);
            isBounded = false;
        }
    }

    @Override
    protected void onPause() {
        Debug.print(TAG, "onPause()", "onPause", 1);
        super.onPause();
        saveCurrenciesFile();
        saveSelectedCurrenciesFile();
    }

    public void editCurrency(Currency currency, int position) {
        Debug.print(TAG, "editCurrency()", "edit currency: " + currency + ", pos: " + position, 1);
        Intent intent = new Intent(this, BagActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
        //if (isBounded) {
            Debug.print(TAG, "onCreate()", "isBounded", 2);
            //Currency[] curr = selectedCurrencies.toArray(new Currency[selectedCurrencies.size()]);
            apiService.fetch(currency, HIGH_PRIORITY);
        //}
    }

    public void addCurrency(View view) {
        Debug.print(TAG, "MainActivity()", "addCurrency", 1);
        Intent intent = new Intent(this, BagActivity.class);
        intent.putExtra("position", -1);
        startActivity(intent);
        apiService.fetch(selectedCurrencies, MEDIUM_PRIORITY);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Debug.print(TAG, "MainActivity()", "onActivityResult", 1);
//
//        selectedCurrencyArrayAdapter.notifyDataSetChanged();
//    }


}
