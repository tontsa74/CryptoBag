package fi.tuni.cryptobag;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

public class BagActivity extends BaseActivity {
    private static final int REQUEST_CODE_ADD_CURRENCY = 20;

    Bag bag;

    EditText buyAmountEditText, sellAmountEditText;
    EditText coinBuyPriceEditText, coinCurrentPriceEditText;

    Button currencyButton, saveBagButton;
    int position;
    Currency buyCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);

        Intent intent = new Intent(this, FetchService.class);
        startService(intent);

        Debug.print(TAG, "onCreate()", "BagActivity", 1);

        buyAmountEditText = (EditText) findViewById(R.id.buyAmountEditText);
        sellAmountEditText = (EditText) findViewById(R.id.sellAmountEditText);
        coinBuyPriceEditText = (EditText) findViewById(R.id.coinBuyPriceEditText);
        coinCurrentPriceEditText = (EditText) findViewById(R.id.coinCurrentPriceEditText);
        currencyButton = (Button) findViewById(R.id.currencyButton);
        saveBagButton = (Button) findViewById(R.id.saveBagButton);
        saveBagButton.setEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Debug.print(TAG, "onCreate()", "getIntent" + position, 1);
            position = extras.getInt("position");

            if (position > -1) {
                buyCurrency = selectedCurrencies.get(position);
                bag = buyCurrency.getBag();
                Debug.print(TAG, "onCreate()", "getIntent, bag: " + bag + ", buyCurrency: " + buyCurrency, 2);

                currencyButton.setText(buyCurrency.getName());
                buyAmountEditText.setText(bag.getBuyAmount().toString());
                sellAmountEditText.setText(bag.getSellAmount().toString());
                coinBuyPriceEditText.setText(bag.getCoinBuyPrice().toString());
                //coinCurrentPriceEditText.setText(bag.getCoinCurrentPrice().toString());
                coinCurrentPriceEditText.setText(buyCurrency.getPrice());

                saveBagButton.setEnabled(true);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.print(TAG, "onActivityResult()",
                    "requestCode: " + requestCode
                        + ", resultCode: " + resultCode
                        + ", data: " + data, 1
                    );
        if (requestCode == REQUEST_CODE_ADD_CURRENCY) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                int selectedCurrencyIndex = (Integer) bundle.get("selCurIndex");
                buyCurrency = currencies.get(selectedCurrencyIndex);
                Debug.print(TAG,"onActivityResult","selectedCurrency: " + buyCurrency, 3);
                currencyButton.setText(buyCurrency.getName());

                saveBagButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Debug.print(TAG, "onBackPressed()", "BagActivity", 1);

        super.onBackPressed();
    }

    public void saveBag(View view) {
        Debug.print(TAG, "saveBag()",  "bag: " + bag +"buyCurrency: " + buyCurrency, 1);

        if (bag == null) {
            bag = new Bag(buyCurrency);
        } else {
            bag.setCurrency(buyCurrency);

        }

        if (!buyAmountEditText.getText().toString().isEmpty()) {
            bag.setBuyAmount(new BigDecimal(buyAmountEditText.getText().toString()));
        }
        if (!coinBuyPriceEditText.getText().toString().isEmpty()) {
            bag.setCoinBuyPrice(new BigDecimal(coinBuyPriceEditText.getText().toString()));
        }
        if (!sellAmountEditText.getText().toString().isEmpty()) {
            bag.setSellAmount(new BigDecimal(sellAmountEditText.getText().toString()));
        }
        if (!coinCurrentPriceEditText.getText().toString().isEmpty()) {
            bag.setCoinCurrentPrice(new BigDecimal(coinCurrentPriceEditText.getText().toString()));
        }


        buyCurrency.setBag(bag);

        if (position == -1) {
            selectedCurrencies.add(buyCurrency);
        } else {
            selectedCurrencies.set(position, buyCurrency);
        }

        saveSelectedCurrenciesFile();

        finish();
    }

    public void addCurrency(View view) {
        Debug.print(TAG, "addCurrency()", "add new currency", 1);
        Intent intent = new Intent(this, AddCurrency.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_CURRENCY);
    }
}
