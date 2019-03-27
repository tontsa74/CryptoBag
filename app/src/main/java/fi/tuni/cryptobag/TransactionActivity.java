package fi.tuni.cryptobag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TransactionActivity extends BaseActivity {

    EditText buyCurrencyEditText, buyAmountEditText;
    EditText sellCurrencyEditText, sellAmountEditText;
    EditText coinBuyPriceEditText, coinCurrentPriceEditText;
    TextView profitTextview;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Debug.print(TAG, "onCreate()", "TransactionActivity", 1);

        buyCurrencyEditText = (EditText) findViewById(R.id.buyCurrencyEditText);
        buyAmountEditText = (EditText) findViewById(R.id.buyAmountEditText);
        sellCurrencyEditText = (EditText) findViewById(R.id.sellCurrencyEditText);
        sellAmountEditText = (EditText) findViewById(R.id.sellAmountEditText);
        coinBuyPriceEditText = (EditText) findViewById(R.id.coinBuyPriceEditText);
        coinCurrentPriceEditText = (EditText) findViewById(R.id.coinCurrentPriceEditText);
        profitTextview = (TextView) findViewById(R.id.profitTextview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Transaction transaction = (Transaction) extras.get("transaction");
            position = extras.getInt("position");
            Debug.print(TAG, "onCreate()", "getIntent: " + transaction + ", pos: " + position, 2);

            buyCurrencyEditText.setText(transaction.getBuyCurrency().getName());
            buyAmountEditText.setText("" + transaction.getBuyAmount());
            sellCurrencyEditText.setText(transaction.getSellCurrency().getName());
            sellAmountEditText.setText("" + transaction.getSellAmount());
            coinBuyPriceEditText.setText("" + transaction.getCoinBuyPrice());
            coinCurrentPriceEditText.setText("" + transaction.getCoinCurrentPrice());

            profitTextview.setText("" + transaction.getProfit()); //TODO: implement proper calculate
        }
    }

    @Override
    public void onBackPressed() {
        Debug.print(TAG, "onBackPressed()", "TransactionActivity", 1);

        super.onBackPressed();
    }

    public void saveTransaction(View view) {
        Debug.print(TAG, "saveTransaction()", "save", 1);
        String buyCurrencyName = buyCurrencyEditText.getText().toString();
        Currency buyCurrency = new Currency("id", buyCurrencyName, "symbol");

        String sellCurrencyName = sellCurrencyEditText.getText().toString();
        Currency sellCurrency = new Currency("id", sellCurrencyName, "symbol");

        Transaction transaction = new Transaction(
                buyCurrency
                , Double.parseDouble(buyAmountEditText.getText().toString())
                , sellCurrency
                , Double.parseDouble(sellAmountEditText.getText().toString())
                , Double.parseDouble(coinBuyPriceEditText.getText().toString())
                , Double.parseDouble(coinCurrentPriceEditText.getText().toString())
        );

        Intent intent = new Intent();
        intent.putExtra("transaction", transaction);
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);

        onBackPressed();
    }
}
