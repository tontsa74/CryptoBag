package fi.tuni.cryptobag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * The type Bag activity.
 */
public class BagActivity extends BaseActivity {
    private static final int REQUEST_CODE_ADD_CURRENCY = 20;

    /**
     * The Bag.
     */
    Bag bag;

    /**
     * The Buy amount edit text.
     */
    EditText buyAmountEditText, /**
     * The Sell amount edit text.
     */
    sellAmountEditText;
    /**
     * The Coin buy price edit text.
     */
    EditText coinBuyPriceEditText, /**
     * The Coin sell price edit text.
     */
    coinSellPriceEditText, /**
     * The Coin current price edit text.
     */
    coinCurrentPriceEditText;

    /**
     * The Bag name text view.
     */
    TextView bagNameTextView;
    /**
     * The Bag icon view.
     */
    ImageView bagIconView;

    /**
     * The Currency button.
     */
    Button currencyButton, /**
     * The Save bag button.
     */
    saveBagButton;
    /**
     * The Position.
     */
    int position;
    /**
     * The Buy currency.
     */
    Currency buyCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);

        Debug.print(TAG, "onCreate()", "BagActivity", 1);

        buyAmountEditText = findViewById(R.id.buyAmountEditText);
        sellAmountEditText = findViewById(R.id.sellAmountEditText);
        coinBuyPriceEditText = findViewById(R.id.coinBuyPriceEditText);
        coinSellPriceEditText = findViewById(R.id.coinSellPriceEditText);
        coinCurrentPriceEditText = findViewById(R.id.coinCurrentPriceEditText);

        bagNameTextView = findViewById(R.id.bagNameTextView);
        bagIconView = findViewById(R.id.bagIconView);

        currencyButton = findViewById(R.id.currencyButton);
        saveBagButton = findViewById(R.id.saveBagButton);
        saveBagButton.setEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Debug.print(TAG, "onCreate()", "getIntent" + position, 1);
            position = extras.getInt("position");

            if (position > -1) {
                bag = selectedBags.get(position);
                buyCurrency = bag.getCurrency();

                Debug.print(TAG, "onCreate()", "getIntent, bag: " + bag + ", buyCurrency: " + buyCurrency, 2);

                currencyButton.setVisibility(View.GONE);
                saveBagButton.setEnabled(true);

                updateActivity();
            }
        }
    }

    /**
     * Update activity.
     */
    public void updateActivity() {

        if(buyCurrency.getIcon() != null) {
            bagIconView.setImageBitmap(buyCurrency.getBitmap());
        }

        bagNameTextView.setText(buyCurrency.getName() + " - " + buyCurrency.getSymbol());
        buyAmountEditText.setText(bag.getBuyAmount().toString());
        sellAmountEditText.setText(bag.getSellAmount().toString());
        coinBuyPriceEditText.setText(bag.getCoinBuyPrice().toString());
        coinSellPriceEditText.setText(bag.getCoinSellPrice().toString());
        coinCurrentPriceEditText.setText("" + buyCurrency.getPrice());
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
                bagNameTextView.setText(buyCurrency.getName() + " - " + buyCurrency.getSymbol());

                saveBagButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Debug.print(TAG, "onBackPressed()", "BagActivity", 1);

        super.onBackPressed();
    }

    /**
     * Save bag.
     *
     * @param view the view
     */
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
        if (!coinSellPriceEditText.getText().toString().isEmpty()) {
            bag.setCoinSellPrice(new BigDecimal(coinSellPriceEditText.getText().toString()));
        }
        if (!coinCurrentPriceEditText.getText().toString().isEmpty()) {
            buyCurrency.setPrice(new BigDecimal(coinCurrentPriceEditText.getText().toString()));
        }


        buyCurrency.getBags().add(bag);

        if (position == -1) {
            selectedBags.add(bag);
        } else {
            selectedBags.set(position, bag);
        }

        saveSelectedFile();
        setResult(RESULT_OK);

        finish();
    }

    /**
     * Add currency.
     *
     * @param view the view
     */
    public void addCurrency(View view) {
        Debug.print(TAG, "addCurrency()", "add new currency", 1);
        Intent intent = new Intent(this, AddCurrency.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_CURRENCY);
    }
}
