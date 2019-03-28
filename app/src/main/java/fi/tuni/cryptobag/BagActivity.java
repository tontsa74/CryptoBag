package fi.tuni.cryptobag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class BagActivity extends BaseActivity {

    EditText currencyEditText, buyAmountEditText;
    EditText sellAmountEditText;
    EditText coinBuyPriceEditText, coinCurrentPriceEditText;
    TextView profitTextview;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);

        Debug.print(TAG, "onCreate()", "BagActivity", 1);

        currencyEditText = (EditText) findViewById(R.id.currencyEditText);
        buyAmountEditText = (EditText) findViewById(R.id.buyAmountEditText);
        sellAmountEditText = (EditText) findViewById(R.id.sellAmountEditText);
        coinBuyPriceEditText = (EditText) findViewById(R.id.coinBuyPriceEditText);
        coinCurrentPriceEditText = (EditText) findViewById(R.id.coinCurrentPriceEditText);
        profitTextview = (TextView) findViewById(R.id.profitTextview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bag bag = (Bag) extras.get("bag");
            position = extras.getInt("position");
            Debug.print(TAG, "onCreate()", "getIntent: " + bag + ", pos: " + position, 2);

            currencyEditText.setText(bag.getCurrency().getName());
            buyAmountEditText.setText(bag.getBuyAmount());
            sellAmountEditText.setText(bag.getSellAmount());
            coinBuyPriceEditText.setText(bag.getCoinBuyPrice());
            coinCurrentPriceEditText.setText(bag.getCoinCurrentPrice());

            profitTextview.setText(bag.getProfit());
        }
    }

    @Override
    public void onBackPressed() {
        Debug.print(TAG, "onBackPressed()", "BagActivity", 1);

        super.onBackPressed();
    }

    public void saveBag(View view) {
        Debug.print(TAG, "saveBag()", "save", 1);
        String buyCurrencyName = currencyEditText.getText().toString();
        Currency buyCurrency = new Currency("id", buyCurrencyName, "symbol");

        Bag bag = new Bag(
                buyCurrency
                , buyAmountEditText.getText().toString()
                , sellAmountEditText.getText().toString()
                , coinBuyPriceEditText.getText().toString()
                , coinCurrentPriceEditText.getText().toString()
        );

        Intent intent = new Intent();
        intent.putExtra("bag", bag);
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);

        onBackPressed();
    }
}
