package fi.tuni.cryptobag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TransactionActivity extends BaseActivity {

    EditText buyCurrencyEditText;
    EditText buyAmountEditText;

    int position;

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
            position = extras.getInt("position");
            Debug.print(TAG, "onCreate()", "getIntent: " + transaction + ", pos: " + position, 2);

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

        Transaction transaction = new Transaction(buyCurrencyEditText.getText().toString()
                , Double.parseDouble(buyAmountEditText.getText().toString()));

        Intent intent = new Intent();
        intent.putExtra("transaction", transaction);
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);

        onBackPressed();
    }
}
