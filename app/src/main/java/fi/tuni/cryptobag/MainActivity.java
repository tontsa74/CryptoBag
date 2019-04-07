package fi.tuni.cryptobag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE_ADD_BAG = 10;
    private static final int REQUEST_CODE_EDIT = 11;

    ArrayAdapter selectedCurrencyArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        final ListView selectedCurrenciesListView = (ListView) findViewById(R.id.selectedCurrenciesListView);
        selectedCurrenciesListView.setOnItemClickListener((parent, view, position, id) -> {
            Debug.print(TAG, "setOnItemClickListener",  "position_id: " + position + "_" + id, 2);
            Currency currency = (Currency) selectedCurrenciesListView.getItemAtPosition(position);
            editCurrency(currency, position);
        });


        Debug.print(TAG, "hmm: ", "hmm: ", 1);
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
        //intent.putExtra("bag", bag);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    public void addCurrency(View view) {
        Debug.print(TAG, "MainActivity()", "addCurrency", 1);
        Intent intent = new Intent(this, BagActivity.class);
        intent.putExtra("position", -1);
        startActivityForResult(intent, REQUEST_CODE_ADD_BAG);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.print(TAG, "onActivityResult()",
                "requestCode: " + requestCode
                + ", resultCode: " + resultCode
                + ", data: " + data, 1);
        if (requestCode == REQUEST_CODE_ADD_BAG) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                //Debug.print(TAG,"onActivityResult","buy: " + bundle.get("bag"), 3);
                //bags.add((Bag) bundle.get("bag"));
                //int bagIndex = (Integer) bundle.get("bagIndex");
                //bags.add(bags.get(bagIndex));
            }
        }
        if (requestCode == REQUEST_CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                //Debug.print(TAG,"onActivityResult","buy: " + bundle.get("bag") + bundle.get("position"), 3);
                //Bag bag = (Bag) bundle.get("bag");
                //int position = bundle.getInt("position");
                //bags.set(position, bag);
                //int bagIndex = (Integer) bundle.get("bagIndex");
                //bags.set(position, bags.get(bagIndex));
            }
        }
        Debug.print(TAG,"onActivityResult","selectedCurrencyArrayAdapter.notifyDataSetChanged", 1);
        selectedCurrencyArrayAdapter.notifyDataSetChanged();
    }
}
