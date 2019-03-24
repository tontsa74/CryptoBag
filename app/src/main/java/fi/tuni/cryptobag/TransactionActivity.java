package fi.tuni.cryptobag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TransactionActivity extends BaseActivity {

    EditText buyCurrencyEditText;
    EditText buyAmountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Debug.print(TAG, "onCreate()", "TransactionActivity", 1);

        buyCurrencyEditText = (EditText) findViewById(R.id.buyCurrencyEditText);

        buyAmountEditText = (EditText) findViewById(R.id.buyAmountEditText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Transaction transaction = (Transaction) extras.get("transaction");
            Debug.print(TAG, "onCreate()", "getIntent: " + transaction, 2);

            buyCurrencyEditText.setText(transaction.getBuyCurrency());
            buyAmountEditText.setText("" + transaction.getBuyAmount());

        }
    }

    @Override
    public void onBackPressed() {
        Debug.print(TAG, "onBackPressed()", "TransactionActivity", 1);

        super.onBackPressed();
    }

    public void saveTransaction(View view) {
        Debug.print(TAG, "saveTransaction()", "save", 1);


        Intent intent = new Intent();
        intent.putExtra("buyCurrency", buyCurrencyEditText.getText().toString());
        intent.putExtra("buyAmount", Double.parseDouble(buyAmountEditText.getText().toString()));
        setResult(RESULT_OK, intent);

        onBackPressed();
    }
}
