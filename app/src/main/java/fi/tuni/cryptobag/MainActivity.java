package fi.tuni.cryptobag;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE_ADD_BAG = 15;

    ArrayAdapter<Currency> selectedCurrencyArrayAdapter;

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

        selectedCurrenciesListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Debug.print(TAG, "setOnItemClickListener",  "position_id: " + position + "_" + id, 2);
            Currency currency = (Currency) selectedCurrenciesListView.getItemAtPosition(position);
            deleteCurrency(currency, position);
            return true;
        });

        selectedCurrencyArrayAdapter = new ArrayAdapter(this, R.layout.currency_item, R.id.currencyItemTextView, selectedCurrencies) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.currency_item, parent, false);
                Currency currency = (Currency) getItem(position);// selectedCurrenciesListView.getItemAtPosition(position);
                TextView currencyItemTextView = (TextView) rowView.findViewById(R.id.currencyItemTextView);
                TextView profitItemTextView = (TextView) rowView.findViewById(R.id.profitItemTextView);
                TextView priceTextView = (TextView) rowView.findViewById(R.id.priceTextView);
                currencyItemTextView.setText(currency.getName());
                Double profit = Double.parseDouble(currency.getBag().getProfit());
                if(profit > 0) {
                    profitItemTextView.setTextColor(Color.GREEN);
                } else if(profit < 0) {
                    profitItemTextView.setTextColor(Color.RED);
                }
                profitItemTextView.setText(currency.getBag().getProfit());
                priceTextView.setText("" + currency.getPrice());
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
        if(!isBounded) {
            Debug.print(TAG, "onStart()", "!isBounded", 2);
            bindService(intent, connectionToService, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        Debug.print(TAG, "onStop()", "onStop", 1);
        super.onStop();
        if (isBounded) {
            Debug.print(TAG, "onStop()", "isBounded", 2);
            unbindService(connectionToService);
            isBounded = false;
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Currency currency = (Currency) intent.getSerializableExtra("currency");
            double price = intent.getDoubleExtra("price", 0);

            Debug.print(TAG, "BroadcastReceiver()", "currency: " + currency, 2);
            Debug.print(TAG, "BroadcastReceiver()", "intent: " + intent, 2);
            currency.setPrice(new BigDecimal(""+price));
            selectedCurrencyArrayAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onResume() {
        Debug.print(TAG, "onResume()", "onResume", 1);
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("tsilve.api"));

    }

    @Override
    protected void onPause() {
        Debug.print(TAG, "onPause()", "onPause", 1);
        super.onPause();
        saveCurrenciesFile();
        saveSelectedCurrenciesFile();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public void editCurrency(Currency currency, int position) {
        Debug.print(TAG, "editCurrency()", "edit currency: " + currency + ", pos: " + position, 1);
        Intent intent = new Intent(this, BagActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
        //if (isBounded) {
            //Debug.print(TAG, "editCurrency()", "isBounded", 2);
            //Currency[] curr = selectedCurrencies.toArray(new Currency[selectedCurrencies.size()]);
            apiService.fetch(currency, HIGH_PRIORITY);
        //}
    }

    public void addCurrency(View view) {
        Debug.print(TAG, "MainActivity()", "addCurrency", 1);
        Intent intent = new Intent(this, BagActivity.class);
        intent.putExtra("position", -1);
        startActivityForResult(intent, REQUEST_CODE_ADD_BAG);
        apiService.fetch(selectedCurrencies, MEDIUM_PRIORITY);
    }

    public void deleteCurrency(Currency currency, int position) {
        Debug.print(TAG, "deleteCurrency()", "edit currency: " + currency + ", pos: " + position, 1);

        AlertDialog.Builder alert = new AlertDialog.Builder(
                MainActivity.this);

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete " + currency.getName());
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCurrencies.remove(position);
                selectedCurrencyArrayAdapter.notifyDataSetChanged();

            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.print(TAG, "MainActivity()", "onActivityResult", 1);

        if (requestCode == REQUEST_CODE_ADD_BAG) {
            Debug.print(TAG, "onActivityResult()", "REQUEST_CODE_ADD_BAG", 1);
        }

        selectedCurrencyArrayAdapter.notifyDataSetChanged();
    }


}
