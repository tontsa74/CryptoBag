package fi.tuni.cryptobag;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BagActivity extends BaseActivity {
    private static final int REQUEST_CODE_ADD_CURRENCY = 20;

    EditText buyAmountEditText, sellAmountEditText;
    EditText coinBuyPriceEditText, coinCurrentPriceEditText;
    TextWatcher textWatcher;
    TextView profitTextview;
    Button currencyButton, saveBagButton;
    int position;
    Currency buyCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);

        Debug.print(TAG, "onCreate()", "BagActivity", 1);

        buyAmountEditText = (EditText) findViewById(R.id.buyAmountEditText);
        sellAmountEditText = (EditText) findViewById(R.id.sellAmountEditText);
        coinBuyPriceEditText = (EditText) findViewById(R.id.coinBuyPriceEditText);
        coinCurrentPriceEditText = (EditText) findViewById(R.id.coinCurrentPriceEditText);
        profitTextview = (TextView) findViewById(R.id.profitTextview);
        currencyButton = (Button) findViewById(R.id.currencyButton);
        saveBagButton = (Button) findViewById(R.id.saveBagButton);
        saveBagButton.setEnabled(false);

        textWatcher();

        buyAmountEditText.addTextChangedListener(textWatcher);
        sellAmountEditText.addTextChangedListener(textWatcher);
        coinBuyPriceEditText.addTextChangedListener(textWatcher);
        coinCurrentPriceEditText.addTextChangedListener(textWatcher);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bag bag = (Bag) extras.get("bag");
            position = extras.getInt("position");
            Debug.print(TAG, "onCreate()", "getIntent: " + bag + ", pos: " + position, 2);

            buyCurrency = bag.getCurrency();
            currencyButton.setText(buyCurrency.getName() + " - " + buyCurrency.getSymbol());
            buyAmountEditText.setText(bag.getBuyAmount());
            sellAmountEditText.setText(bag.getSellAmount());
            coinBuyPriceEditText.setText(bag.getCoinBuyPrice());
            coinCurrentPriceEditText.setText(bag.getCoinCurrentPrice());

            profitTextview.setText(bag.getProfit());

            saveBagButton.setEnabled(true);
        }
    }

    private void textWatcher() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Debug.print(TAG, "textWatcher()", "onTextChanged", 3);
                if(count < 1) {
                    saveBagButton.setEnabled(false);
                } else {
                    saveBagButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
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
                buyCurrency = (Currency) bundle.get("selectedCurrency");
                Debug.print(TAG,"onActivityResult","selectedCurrency: " + buyCurrency.getName(), 3);
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
        Debug.print(TAG, "saveBag()", "save", 1);

        Bag bag = new Bag(buyCurrency);
        bag.setBuyAmount(buyAmountEditText.getText().toString());
        bag.setCoinBuyPrice(coinBuyPriceEditText.getText().toString());
        bag.setSellAmount(sellAmountEditText.getText().toString());
        bag.setCoinCurrentPrice(coinCurrentPriceEditText.getText().toString());

        Intent intent = new Intent();
        intent.putExtra("bag", bag);
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);

        finish();
    }

    public void addCurrency(View view) {
        Debug.print(TAG, "addCurrency()", "add new currency", 1);
        Intent intent = new Intent(this, AddCurrency.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_CURRENCY);
    }
}
