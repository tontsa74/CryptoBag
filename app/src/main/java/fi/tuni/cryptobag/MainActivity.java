package fi.tuni.cryptobag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    ArrayAdapter selectedCurrencyArrayAdapter;

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

        Intent intent = new Intent(this, FetchService.class);
        startService(intent);

        int total = 0;
        for (Currency selCur : selectedCurrencies) {
            total += Integer.parseInt(selCur.getBag().getProfit());
        }
        totalProfit.setText("Total profit: " + total);

        final ListView selectedCurrenciesListView = (ListView) findViewById(R.id.selectedCurrenciesListView);
        selectedCurrenciesListView.setOnItemClickListener((parent, view, position, id) -> {
            Debug.print(TAG, "setOnItemClickListener",  "position_id: " + position + "_" + id, 2);
            Currency currency = (Currency) selectedCurrenciesListView.getItemAtPosition(position);
            editCurrency(currency, position);
        });

        selectedCurrencyArrayAdapter = new ArrayAdapter(this, R.layout.currency_item, R.id.currencyItemTextView, selectedCurrencies);
        selectedCurrenciesListView.setAdapter(selectedCurrencyArrayAdapter);

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
    }

    public void addCurrency(View view) {
        Debug.print(TAG, "MainActivity()", "addCurrency", 1);
        Intent intent = new Intent(this, BagActivity.class);
        intent.putExtra("position", -1);
        startActivity(intent);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Debug.print(TAG, "MainActivity()", "onActivityResult", 1);
//
//        selectedCurrencyArrayAdapter.notifyDataSetChanged();
//    }
}
